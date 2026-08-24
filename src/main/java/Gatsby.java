import java.util.ArrayList;
import java.util.Scanner;

/**
 * A simple command-line chatbot that stores tasks and remembers them between runs.
 *
 * Tasks are loaded from the save file at startup and saved again after every change.
 * Every expected problem (bad input, an unknown command, a corrupted save file) is
 * reported as a friendly message; the chatbot keeps running.
 */
public class Gatsby {
    private static final String LINE = "____________________________________________________________";
    private static final String GOODBYE_MESSAGE = " Bye. Hope to see you again soon!";
    private static final String START_JOKE = "When did the Japanese invent eggs? A long tamago :)";

    /**
     * The vertical bar separates fields in the save file, so it cannot appear
     * inside a description or a date without making the saved line ambiguous.
     */
    private static final String FIELD_SEPARATOR = "|";

    /** Tasks are loaded from the save file so the list survives between runs. */
    private static ArrayList<Task> tasks = Storage.load();

    /**
     * Starts Gatsby and processes commands entered through standard input.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        String banner = "************************************\n"
                + "*              Gatsby              *\n"
                + "************************************\n";
        System.out.println(banner);
        String welcome = "Wassup! I'm Gatsby.\n"
                + START_JOKE + "\n"
                + "What can I do for you?\n";
        System.out.print(welcome);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            System.out.println(LINE);
            String input = scanner.nextLine();
            System.out.println(LINE);

            if (handleInput(input)) {
                break;
            }
        }
        scanner.close();
    }

    /**
     * Handles one line of user input and prints the response.
     *
     * All expected problems are caught here so that a single bad command never ends
     * the session. Unexpected runtime problems are caught too, because losing the
     * whole chat over an unforeseen bug would be worse than reporting it.
     *
     * @param input the raw line the user typed
     * @return true when the user asked to quit, false to keep going
     */
    private static boolean handleInput(String input) {
        try {
            String trimmedInput = input.strip();
            if (trimmedInput.isEmpty()) {
                System.out.println(" You didn't type anything! Try \"todo read book\" or \"list\".");
                return false;
            }

            String[] commandParts = trimmedInput.split("\\s+", 2);
            String payload = commandParts.length > 1 ? commandParts[1].strip() : "";
            Command commandType = Command.fromInput(trimmedInput);

            if (commandType == Command.BYE) {
                System.out.println(GOODBYE_MESSAGE);
                return true;
            } else if (commandType == Command.LIST) {
                printList();
            } else if (commandType == Command.MARK || commandType == Command.UNMARK) {
                setDone(commandType, payload);
            } else if (commandType == Command.TODO
                    || commandType == Command.DEADLINE
                    || commandType == Command.EVENT) {
                addToList(commandType, payload);
            } else if (commandType == Command.DELETE) {
                deleteFromList(payload);
            } else {
                throw new UnknownCommandException(" Wait I don't recognise that yet :(\n"
                        + " I know: todo, deadline, event, list, mark, unmark, delete, bye.");
            }
        } catch (GatsbyException e) {
            System.out.println(e.getMessage());
        } catch (RuntimeException e) {
            System.out.println(" Yikes, something unexpected went wrong: " + e);
        } finally {
            System.out.println(LINE);
        }
        return false;
    }

    /**
     * Marks a task as done or not done.
     *
     * @param command either {@link Command#MARK} or {@link Command#UNMARK}
     * @param payload the text typed after the command, expected to be a task number
     * @throws GatsbyException when the number is missing, not a number, or out of range
     */
    private static void setDone(Command command, String payload) throws GatsbyException {
        boolean isMarking = command == Command.MARK;
        if (payload.isEmpty()) {
            throw new EmptyMarkingException(isMarking
                    ? " OOPS! We can't be marking nothing as done!"
                    : " OOPS! We can't be marking nothing as undone!");
        }

        Task task = tasks.get(parseTaskIndex(payload));
        boolean wasAlreadyInState = task.isDone() == isMarking;

        if (isMarking) {
            task.markDone();
        } else {
            task.markUndone();
        }
        Storage.save(tasks);

        if (wasAlreadyInState) {
            System.out.println(isMarking
                    ? " That one was already done, but sure:"
                    : " That one wasn't done yet, but sure:");
        } else {
            System.out.println(isMarking
                    ? " Nice! I've marked this task as done:"
                    : " OK, I've marked this task as not done yet:");
        }
        System.out.println("  " + task);
    }

    /**
     * Adds a new task of the requested type to the list.
     *
     * @param command the type of task to create
     * @param payload the text typed after the command
     * @throws GatsbyException when required parts of the description are missing
     */
    private static void addToList(Command command, String payload) throws GatsbyException {
        Task newTask;
        if (command == Command.TODO) {
            newTask = new Todo(requireText(payload,
                    " son the description of a todo cannot be empty -_-!"));
        } else if (command == Command.DEADLINE) {
            String[] parts = splitOnKeyword(payload, "/by",
                    " son there's no name or deadline for this deadline -_-!");
            newTask = new Deadline(
                    requireText(parts[0], " son this deadline has no description -_-!"),
                    requireText(parts[1], " son this deadline has no date after /by -_-!"));
        } else if (command == Command.EVENT) {
            String[] eventParts = splitOnKeyword(payload, "/from",
                    " son there's no event name/timing for this event -_-!");
            String[] timeParts = splitOnKeyword(eventParts[1], "/to",
                    " son this event has no end time, it's infinite! -_-!");
            newTask = new Event(
                    requireText(eventParts[0], " son this event has no description -_-!"),
                    requireText(timeParts[0], " son this event has no start time after /from -_-!"),
                    requireText(timeParts[1], " son this event has no end time after /to -_-!"));
        } else {
            throw new UnknownCommandException(" I don't recognise this command :'((");
        }

        tasks.add(newTask);
        Storage.save(tasks);
        System.out.println(" Got it. I've added this task:");
        System.out.println("  " + newTask);
        printTaskCount();
    }

    /**
     * Removes a task from the list.
     *
     * @param payload the text typed after the command, expected to be a task number
     * @throws GatsbyException when the number is missing, not a number, or out of range
     */
    private static void deleteFromList(String payload) throws GatsbyException {
        if (payload.isEmpty()) {
            throw new EmptyPayloadException(" OOPS! How do I even delete nothing??");
        }
        Task removed = tasks.remove(parseTaskIndex(payload));
        Storage.save(tasks);
        System.out.println(" Noted. I've removed this task:");
        System.out.println("  " + removed);
        printTaskCount();
    }

    /**
     * Converts the text after a command into a valid index into the task list.
     *
     * Doing this in one place means every command reports the same problems the same
     * way: not a number, out of range, or given when the list is empty.
     *
     * @param payload the text typed after the command
     * @return the zero-based index of the task the user meant
     * @throws InvalidTaskException when the text is not a task number that exists
     */
    private static int parseTaskIndex(String payload) throws InvalidTaskException {
        int taskNumber;
        try {
            taskNumber = Integer.parseInt(payload);
        } catch (NumberFormatException e) {
            throw new InvalidTaskException(" OOPS! \"" + payload + "\" isn't a task number! :(");
        }
        if (tasks.isEmpty()) {
            throw new InvalidTaskException(" OOPS! Your list is empty, so there's no task " + taskNumber + "!");
        }
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new InvalidTaskException(" OOPS! There's no task " + taskNumber
                    + "! Pick a number from 1 to " + tasks.size() + ".");
        }
        return taskNumber - 1;
    }

    /**
     * Checks that a description or date is usable before a task is built from it.
     *
     * @param text the candidate text
     * @param errorMessage the message shown when the text is empty
     * @return the text with surrounding spaces removed
     * @throws EmptyPayloadException when the text is empty or contains the field separator
     */
    private static String requireText(String text, String errorMessage) throws EmptyPayloadException {
        String trimmedText = text.strip();
        if (trimmedText.isEmpty()) {
            throw new EmptyPayloadException(errorMessage);
        }
        if (trimmedText.contains(FIELD_SEPARATOR)) {
            throw new EmptyPayloadException(" OOPS! Please leave out the \"" + FIELD_SEPARATOR
                    + "\" character; I use it to separate fields in my save file.");
        }
        return trimmedText;
    }

    /**
     * Splits a payload around a keyword such as {@code /by} or {@code /from}.
     *
     * The split tolerates missing spaces around the keyword and any capitalisation,
     * so "deadline report/by Friday" and "deadline report /BY Friday" both work.
     *
     * @param payload the text to split
     * @param keyword the keyword to split on
     * @param errorMessage the message shown when the keyword is absent
     * @return the text before and after the keyword, in that order
     * @throws EmptyPayloadException when the keyword does not appear
     */
    private static String[] splitOnKeyword(String payload, String keyword, String errorMessage)
            throws EmptyPayloadException {
        String[] parts = payload.split("(?i)\\s*" + keyword + "\\s*", 2);
        if (parts.length < 2) {
            throw new EmptyPayloadException(errorMessage);
        }
        return parts;
    }

    /** Prints how many tasks are in the list, using correct singular/plural wording. */
    private static void printTaskCount() {
        System.out.println(" Now you have " + tasks.size()
                + (tasks.size() == 1 ? " task" : " tasks") + " in the list.");
    }

    /**
     * Prints the latest snapshot of the task list at the time of this method call.
     */
    private static void printList() {
        System.out.println(" Here are the tasks in your list:");
        if (tasks.isEmpty()) {
            System.out.println(" There's nothing here yet! Go ahead and add any tasks you'd like! :)");
            return;
        }
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + ". " + tasks.get(i));
        }
    }
}
