package gatsby;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gatsby.testutil.TestSupport;

/** Tests the public application entry point through a short command session. */
class GatsbyTest {
    private Path dataBackup;
    private final java.io.InputStream originalInput = System.in;
    private final PrintStream originalOutput = System.out;

    @BeforeEach
    void isolateDataDirectory() throws Exception {
        dataBackup = TestSupport.isolateDataDirectory();
    }

    @AfterEach
    void restoreEnvironment() throws Exception {
        System.setIn(originalInput);
        System.setOut(originalOutput);
        TestSupport.restoreDataDirectory(dataBackup);
    }

    /** Verifies that the main loop accepts a command, executes it, and exits cleanly. */
    @Test
    void main_shortSession_processesTodoAndBye() {
        System.setIn(new ByteArrayInputStream("todo read book\nbye\n".getBytes(StandardCharsets.UTF_8)));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        Gatsby.main(new String[0]);

        String printed = output.toString(StandardCharsets.UTF_8);
        assertTrue(printed.contains("Got it. I've added this task:"));
        assertTrue(printed.contains("[T][ ] read book"));
        assertTrue(printed.contains("Bye. Hope to see you again soon!"));
    }
}
