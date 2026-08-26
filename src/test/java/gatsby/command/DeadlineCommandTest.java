package gatsby.command;

import gatsby.exception.EmptyPayloadException;
import gatsby.model.Deadline;
import gatsby.model.TaskList;
import gatsby.testutil.RecordingUi;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Tests deadline parsing, creation, and validation. */
class DeadlineCommandTest extends AbstractCommandTest {

    /** Verifies that a valid deadline creates the expected typed task. */
    @Test
    void execute_validDeadline_addsDeadlineAndReportsSuccess() throws Exception {
        TaskList tasks = new TaskList();
        RecordingUi ui = recordingUi();

        new DeadlineCommand("return book /by 2019-12-02 1800").execute(tasks, ui, null);

        assertEquals(1, tasks.size());
        Deadline deadline = assertInstanceOf(Deadline.class, tasks.get(0));
        assertEquals("return book", deadline.getTaskName());
        assertEquals("[D][ ] return book (by: Dec 02 2019 18:00:00)", deadline.toString());
        assertEquals(List.of(" Got it. I've added this task:",
                "  [D][ ] return book (by: Dec 02 2019 18:00:00)",
                " Now you have 1 task in the list."), ui.messages());
    }

    /** Verifies that the required {@code /by} keyword cannot be omitted. */
    @Test
    void execute_missingByKeyword_throwsException() {
        EmptyPayloadException exception = assertThrows(EmptyPayloadException.class,
                () -> new DeadlineCommand("return book").execute(new TaskList(), recordingUi(), null));

        assertEquals(" son there's no name or deadline for this deadline -_-!", exception.getMessage());
    }

    /** Verifies that malformed dates are not silently converted into deadlines. */
    @Test
    void execute_invalidDate_throwsDateParseException() {
        assertThrows(DateTimeParseException.class,
                () -> new DeadlineCommand("return book /by tomorrow").execute(
                        new TaskList(), recordingUi(), null));
    }
}
