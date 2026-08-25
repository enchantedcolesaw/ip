/**
 * A simple command-line chatbot that stores tasks and remembers them between runs.
 *
 * Tasks are loaded from the save file at startup and saved again after every change.
 * Every expected problem (bad input, an unknown command, a corrupted save file) is
 * reported as a friendly message; the chatbot keeps running.
 */
public class Gatsby {
    /** Tasks are loaded from the save file so the list survives between runs. */
    private static TaskList tasks = new TaskList(Storage.load());

    /** Handles Gatsby's console input and output. */
    private static final Ui ui = new Ui();

    /** Converts raw input lines into commands and payloads. */
    private static final Parser parser = new Parser();

    /**
     * Starts Gatsby and processes commands entered through standard input.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        ui.showWelcome();
        while (ui.hasNextLine()) {
            ui.showSeparator();
            String input = ui.readLine();
            ui.showSeparator();

            if (handleInput(input)) {
                break;
            }
        }
        ui.close();
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
                ui.printLine(" You didn't type anything! Try \"todo read book\" or \"list\".");
                return false;
            }

            Parser.ParsedCommand parsedCommand = parser.parse(input);
            CommandType commandType = parsedCommand.getCommand();
            String payload = parsedCommand.getPayload();

            if (commandType == CommandType.BYE) {
                Command command = new ExitCommand();
                command.execute(tasks, ui, null);
                return command.isExit();
            } else if (commandType == CommandType.LIST) {
                Command command = new ListCommand();
                command.execute(tasks, ui, null);
            } else if (commandType == CommandType.MARK || commandType == CommandType.UNMARK) {
                setDone(commandType, payload);
            } else if (commandType == CommandType.TODO
                    || commandType == CommandType.DEADLINE
                    || commandType == CommandType.EVENT) {
                Command command = switch (commandType) {
                    case TODO -> new TodoCommand(payload);
                    case DEADLINE -> new DeadlineCommand(payload);
                    case EVENT -> new EventCommand(payload);
                    default -> throw new UnknownCommandException(" I don't recognise this command :'((");
                };
                command.execute(tasks, ui, null);
            } else if (commandType == CommandType.DELETE) {
                deleteFromList(payload);
            } else {
                throw new UnknownCommandException(" Wait I don't recognise that yet :(\n"
                        + " I know: todo, deadline, event, list, mark, unmark, delete, bye.");
            }
        } catch (GatsbyException e) {
            ui.printLine(e.getMessage());
        } catch (RuntimeException e) {
            ui.printLine(" Yikes, something unexpected went wrong: " + e);
        } finally {
            ui.showSeparator();
        }
        return false;
    }

    /**
     * Marks a task as done or not done.
     *
     * @param command either {@link CommandType#MARK} or {@link CommandType#UNMARK}
     * @param payload the text typed after the command, expected to be a task number
     * @throws GatsbyException when the number is missing, not a number, or out of range
     */
    private static void setDone(CommandType command, String payload) throws GatsbyException {
        boolean isMarking = command == CommandType.MARK;
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
        Storage.save(tasks.asList());

        if (wasAlreadyInState) {
            ui.printLine(isMarking
                    ? " That one was already done, but sure:"
                    : " That one wasn't done yet, but sure:");
        } else {
            ui.printLine(isMarking
                    ? " Nice! I've marked this task as done:"
                    : " OK, I've marked this task as not done yet:");
        }
        ui.printLine("  " + task);
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
        Storage.save(tasks.asList());
        ui.printLine(" Noted. I've removed this task:");
        ui.printLine("  " + removed);
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

    /** Prints how many tasks are in the list, using correct singular/plural wording. */
    private static void printTaskCount() {
        ui.printLine(" Now you have " + tasks.size()
                + (tasks.size() == 1 ? " task" : " tasks") + " in the list.");
    }

}
