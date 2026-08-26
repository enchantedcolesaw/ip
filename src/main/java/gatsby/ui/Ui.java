package gatsby.ui;

import java.util.Scanner;

/**
 * Handles Gatsby's interaction with the console.
 *
 * Keeping input and output here allows the command logic to focus on deciding
 * what Gatsby should do, while this class focuses on how Gatsby communicates it.
 */
public class Ui {
    private static final String LINE = "____________________________________________________________";
    private static final String START_JOKE = "When did the Japanese invent eggs? A long tamago :)";

    /** Reads commands from standard input for the current Gatsby session. */
    private final Scanner scanner;

    /** Creates a console UI connected to standard input. */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /** Prints Gatsby's banner and welcome message. */
    public void showWelcome() {
        String banner = "************************************\n"
                + "*              Gatsby              *\n"
                + "************************************\n";
        System.out.println(banner);
        String welcome = "Wassup! I'm Gatsby.\n"
                + START_JOKE + "\n"
                + "What can I do for you?\n";
        System.out.print(welcome);
    }

    /**
     * Returns whether another command is available on standard input.
     *
     * @return true when another input line can be read
     */
    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next command line.
     *
     * @return the raw line entered by the user
     */
    public String readLine() {
        return scanner.nextLine();
    }

    /** Prints the separator used around each command interaction. */
    public void showSeparator() {
        printLine(LINE);
    }

    /**
     * Prints a message followed by a newline.
     *
     * @param message the message to display
     */
    public void printLine(String message) {
        System.out.println(message);
    }

    /**
     * Prints a message without adding a newline.
     *
     * @param message the message to display
     */
    public void print(String message) {
        System.out.print(message);
    }

    /** Closes the input reader at the end of the session. */
    public void close() {
        scanner.close();
    }
}
