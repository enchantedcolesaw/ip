import java.util.Locale;
import java.util.Scanner;
import java.util.Set;

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
    private static final String GOODBYE_MESSAGE = " Bye. Hope to see you again soon!";
    private static final String START_JOKE = "When did the Japanese invent eggs? A long tamago :)";
    private static final int MAX_TASKS = 100;
    private static Task[] tasks = new Task[MAX_TASKS];
    private static int taskCount = 0;
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
            String[] commandParts = command.strip().split("\\s+");
            String action = commandParts[0].toLowerCase(Locale.ROOT);
            System.out.println(LINE);

            if (isGoodbyeCommand(command)) {
                System.out.println(GOODBYE_MESSAGE);
                System.out.println(LINE);
                break;
            } else if(isListCommand(command)) {
                printList();
            } else if (action.equals(MARK_COMMAND)) {
                int taskNum = Integer.parseInt(commandParts[1]);
                int taskIndex = taskNum - 1;
                tasks[taskIndex].markDone();
                System.out.println(" Nice! I've marked this task as done:");
                System.out.println("  " + tasks[taskIndex].toString());
                System.out.println(LINE);
            } else if (action.equals(UNMARK_COMMAND)) {
                int taskNum = Integer.parseInt(commandParts[1]);
                int taskIndex = taskNum - 1;
                tasks[taskIndex].markUndone();
                System.out.println(" OK, I've marked this task as not done yet:");
                System.out.println("  " + tasks[taskIndex].toString());
                System.out.println(LINE);
            }
            else {
                System.out.println(" added: " + command);
                addToList(command);
                System.out.println(LINE);
            }
        }
    }
    /**
     * Adds a user command into the list.
     *
     * @param command the raw command entered by the user
     */
    private static void addToList(String command){
        tasks[taskCount] = new Task(command);
        taskCount++;
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
        for (int i = 0; i < taskCount; i++){
            System.out.println(" " + (i+1) + ". " +  tasks[i].toString());
        }
        System.out.println(LINE);
    }
}
