import java.util.Locale;
import java.util.Scanner;
import java.util.Set;

/**
 * A simple command-line chatbot that echoes commands until the user enters the goodbye command.
 */
public class Gatsby {
    private static final String LINE = "____________________________________________________________";
    private static final Set<String> GOODBYE_COMMANDS = Set.of("bye", "byebye", "bye bye");
    private static final String GOODBYE_MESSAGE = " Bye. Hope to see you again soon!";
    private static final String START_JOKE = "When did the Japanese invent eggs? A long tamago :)";

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
            System.out.println(LINE);

            if (isGoodbyeCommand(command)) {
                System.out.println(GOODBYE_MESSAGE);
                System.out.println(LINE);
                break;
            }

            System.out.println(" " + command);
            System.out.println(LINE);
        }
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
}
