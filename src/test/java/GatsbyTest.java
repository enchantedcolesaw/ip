import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * Small dependency-free regression tests for Gatsby's command loop.
 *
 * <p>Run this class after compiling the main and test source files with Java 25.</p>
 */
public class GatsbyTest {
    /**
     * Runs all regression tests and fails if any expected behavior is missing.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        testCommandsAreEchoedAndByeStopsTheChatbot();
        testAllGoodbyeVariantsStopTheChatbot();
        testEndOfInputDoesNotThrowAnException();
        System.out.println("Gatsby tests passed.");
    }

    private static void testCommandsAreEchoedAndByeStopsTheChatbot() {
        String output = runGatsby("list\nblah\nBYE \nignored\n");

        assertContains(output, "When did the Japanese invent eggs? A long tamago :)");
        assertContains(output, " list");
        assertContains(output, " blah");
        assertContains(output, " Bye. Hope to see you again soon!");
        assertNotContains(output, " ignored");
    }

    private static void testAllGoodbyeVariantsStopTheChatbot() {
        String[] goodbyeCommands = {"bye", "BYE", "bye bye", "BYE  BYE", "byebye", "BYEBYE",
                "  bye\t \tbye  "};

        for (String goodbyeCommand : goodbyeCommands) {
            String output = runGatsby(goodbyeCommand + "\nignored\n");

            assertContains(output, " Bye. Hope to see you again soon!");
            assertNotContains(output, " ignored");
        }
    }

    private static void testEndOfInputDoesNotThrowAnException() {
        String output = runGatsby("list\n");

        assertContains(output, " list");
        assertNotContains(output, "Bye. Hope to see you again soon!");
    }

    private static String runGatsby(String input) {
        ByteArrayInputStream testInput = new ByteArrayInputStream(
                input.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
        InputStream originalInput = System.in;
        PrintStream originalOutput = System.out;

        System.setIn(testInput);
        System.setOut(new PrintStream(testOutput, true, StandardCharsets.UTF_8));
        try {
            Gatsby.main(new String[0]);
        } finally {
            System.setIn(originalInput);
            System.setOut(originalOutput);
        }

        return testOutput.toString(StandardCharsets.UTF_8);
    }

    private static void assertContains(String output, String expected) {
        if (!output.contains(expected)) {
            throw new AssertionError("Expected output to contain: " + expected);
        }
    }

    private static void assertNotContains(String output, String unexpected) {
        if (output.contains(unexpected)) {
            throw new AssertionError("Expected output not to contain: " + unexpected);
        }
    }
}
