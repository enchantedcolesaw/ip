import java.util.Locale;
import java.util.Scanner;
import java.util.Set;
import java.util.ArrayList;

/**
 * A simple command-line chatbot that stores and lists tasks until the user enters the goodbye command.
 * Task list may be accessed when user enters the list command.
 */
public class Gatsby {
    private static final String LINE = "____________________________________________________________";
    private static final Set<String> GOODBYE_COMMANDS = Set.of("bye", "byebye", "bye bye");
    private static final String LIST_COMMAND = "list";
    private static final String MARK_COMMAND = "mark";
    private static final String UNMARK_COMMAND = "unmark";
    private static final Set<String> ADD_COMMANDS = Set.of("todo", "deadline", "event");
    private static final String DELETE_COMMAND = "delete";
    private static final String GOODBYE_MESSAGE = " Bye. Hope to see you again soon!";
    private static final String START_JOKE = "When did the Japanese invent eggs? A long tamago :)";
    private static final int MAX_TASKS = 100;
    private static ArrayList<Task> tasks = new ArrayList<>(MAX_TASKS);
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
            String command = scanner.nextLine();
            String[] commandParts = command.strip().split("\\s+", 2);
            String action = commandParts[0].toLowerCase(Locale.ROOT);
            String payload = commandParts.length > 1 ? commandParts[1] : "";
            System.out.println(LINE);
            if (isGoodbyeCommand(command)) {
                System.out.println(GOODBYE_MESSAGE);
                System.out.println(LINE);
                break;
            } else if(isListCommand(command)) {
                printList();
            } else if (action.equals(MARK_COMMAND)) {
                try {
                    if (commandParts.length < 2){
                        throw new EmptyMarkingException("OOPS! We can't be marking nothing as done!");
                    }
                    int taskNum = Integer.parseInt(commandParts[1]);
                    int taskIndex = taskNum - 1;
                    if (taskNum > tasks.size() || taskIndex < 0 || tasks.get(taskIndex) == null){
                        throw new InvalidTaskException("OOPS! There's no such task in your list right now!");
                    }
                    tasks.get(taskIndex).markDone();
                    System.out.println(" Nice! I've marked this task as done:");
                    System.out.println("  " + tasks.get(taskIndex).toString());
                } catch (InvalidTaskException e){
                    System.out.println(e.getMessage());
                } catch (EmptyMarkingException e) {
                    System.out.println(e.getMessage());
                } catch (NumberFormatException e) {
                    System.out.println("OOPS! That's not a number! :(");
                } finally{
                    System.out.println(LINE);
                }
            } else if (action.equals(UNMARK_COMMAND)) {
                try {
                    if (commandParts.length < 2){
                        throw new EmptyMarkingException("OOPS! We can't be marking nothing as undone!");
                    }
                    int taskNum = Integer.parseInt(commandParts[1]);
                    int taskIndex = taskNum - 1;
                    if (taskNum > tasks.size() || taskIndex < 0 || tasks.get(taskIndex) == null){
                        throw new InvalidTaskException("OOPS! There's no such task in your list right now!");
                    }
                    tasks.get(taskIndex).markUndone();
                    System.out.println(" OK, I've marked this task as not done yet:");
                    System.out.println("  " + tasks.get(taskIndex).toString());
                } catch (InvalidTaskException e){
                    System.out.println(e.getMessage());
                } catch (EmptyMarkingException e) {
                    System.out.println(e.getMessage());
                } catch (NumberFormatException e) {
                    System.out.println("OOPS! That's not a number! :(");
                } finally{
                    System.out.println(LINE);
                }
            } else if (ADD_COMMANDS.contains(action)) {
                try{
                    addToList(action, payload);
                } catch (UnknownCommandException e){
                    System.out.println(e.getMessage());
                } catch (EmptyPayloadException e){
                    System.out.println(e.getMessage());
                } finally {
                    System.out.println(LINE);
                }
            } else if (action.equals(DELETE_COMMAND)) {
                try{
                    if (commandParts.length < 2){
                        throw new EmptyPayloadException("OOPS! How do I even delete nothing??");
                    }
                    int taskNum = Integer.parseInt(commandParts[1]);
                    deleteFromList(taskNum);
                } catch (EmptyPayloadException e){
                    System.out.println(e.getMessage());
                } catch (InvalidTaskException e){
                    System.out.println(e.getMessage());
                } catch (NumberFormatException e){
                    System.out.println("OOPS! That's not a number! :(");
                } finally{
                    System.out.println(LINE);
                }
            } else{
                System.out.println("Wait I don't recognise that yet :(");
                System.out.println(LINE);
            }
        }
    }
    /**
     * Adds a user command into the list.
     *
     * @param action the  command entered by the user
     */
    private static void addToList(String action, String payload) throws UnknownCommandException, EmptyPayloadException {
        if (action.equals("todo")){
            if (payload.length() == 0){
                throw new EmptyPayloadException("son the description of a todo cannot be empty -_-!");
            }
            tasks.add(tasks.size(),new Todo(payload));
        } else if (action.equals("deadline")){
            String[] parts = payload.split("(?i)\\s+/by\\s+", 2);
            if (parts.length == 1){
                throw new EmptyPayloadException("son the there's no name or deadline for this deadline -_-!");
            }
            tasks.add(tasks.size(), new Deadline(parts[0], parts[1]));
        } else if (action.equals("event")){
            String[] eventParts = payload.split("(?i)\\s+/from\\s+", 2);
            if (eventParts.length == 1){
                throw new EmptyPayloadException("son there's no event name/timing for this event -_-!");
            }
            String[] timeParts = eventParts[1].split("(?i)\\s+/to\\s+", 2);
            if (timeParts.length == 1){
                throw new EmptyPayloadException("son this event has no end time, it's infinite! -_-!");
            }
            tasks.add(tasks.size(), new Event(eventParts[0], timeParts[0], timeParts[1]));
        } else {
            throw  new UnknownCommandException("I don't recognise this command :'((");
        }
        System.out.println(" Got it. I've added this task:");
        System.out.println("  " + tasks.get(tasks.size() - 1).toString());
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
    }

    private static void deleteFromList(int taskNum) throws InvalidTaskException {
        if (taskNum < 1 || taskNum > tasks.size()){
            throw  new InvalidTaskException("OOPS! Can't delete something that doesn't exist!");
        }
        Task removed = tasks.get(taskNum - 1);
        tasks.remove(taskNum - 1);
        System.out.println(" Noted. I've removed this task:");
        System.out.println("  " + removed.toString());
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
    }

    /**
     * Checks whether a command is one of Gatsby's accepted goodbye commands.
     * Repeated whitespace and differences in letter casing are ignored.
     *
     * @param command the raw command entered by the user
     * @return true when the command should terminate the chat
     */
    private static boolean isGoodbyeCommand(String command) {
        String normalizedCommand = command.strip()
                .toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", " ");
        return GOODBYE_COMMANDS.contains(normalizedCommand);
    }
    /**
     * Checks whether a command is one of Gatsby's accepted list commands.
     * Repeated whitespace and differences in letter casing are ignored.
     *
     * @param command the raw command entered by the user
     * @return true when the command should print out the internal list
     */
    private static boolean isListCommand(String command) {
        String normalizedCommand = command.strip()
                .toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", " ");
        return LIST_COMMAND.equals(normalizedCommand);
    }

    /**
     * Prints the latest snapshot of the task list at the time of this method call.
     */
    private static void printList() {
        System.out.println(" Here are the tasks in your list:");
        if (tasks.isEmpty()){
            System.out.println(" There's nothing here yet! Go ahead and add any tasks you'd like! :)");
        }
        for (int i = 0; i < tasks.size(); i++){
            System.out.println(" " + (i+1) + ". " +  tasks.get(i).toString());
        }
        System.out.println(LINE);
    }
}
