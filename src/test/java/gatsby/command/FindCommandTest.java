package gatsby.command;

import gatsby.exception.EmptyPayloadException;
import gatsby.model.Deadline;
import gatsby.model.Event;
import gatsby.model.TaskList;
import gatsby.model.Todo;
import gatsby.testutil.RecordingUi;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Tests searching task descriptions and reporting the matching results. */
class FindCommandTest extends AbstractCommandTest {

    /** Verifies that matching descriptions from different task types are listed in order. */
    @Test
    void execute_matchingDescriptions_printsNumberedMatches() throws Exception {
        TaskList tasks = new TaskList(List.of(
                new Todo("read book"),
                new Deadline("return report", LocalDateTime.of(2019, 12, 2, 18, 0)),
                new Event("book club", LocalDateTime.of(2019, 12, 3, 14, 0),
                        LocalDateTime.of(2019, 12, 3, 16, 0))));
        RecordingUi ui = recordingUi();

        new FindCommand("book").execute(tasks, ui, null);

        assertEquals(List.of(" Here are the matching tasks in your list:",
                " 1. [T][ ] read book",
                " 2. [E][ ] book club (from: Dec 03 2019 14:00:00 to: Dec 03 2019 16:00:00)"),
                ui.messages());
    }

    /** Verifies that dates and other display details are not searched as descriptions. */
    @Test
    void execute_keywordOnlyInDate_reportsNoMatches() throws Exception {
        TaskList tasks = new TaskList(List.of(
                new Deadline("return report", LocalDateTime.of(2019, 12, 2, 18, 0))));
        RecordingUi ui = recordingUi();

        new FindCommand("2019").execute(tasks, ui, null);

        assertEquals(List.of(" No matching tasks found :("), ui.messages());
    }

    /** Verifies that a missing search keyword is rejected without producing results. */
    @Test
    void execute_emptyKeyword_throwsException() {
        RecordingUi ui = recordingUi();

        EmptyPayloadException exception = assertThrows(EmptyPayloadException.class,
                () -> new FindCommand("").execute(new TaskList(), ui, null));

        assertEquals("OOPS! How do I even find nothing??", exception.getMessage());
        assertEquals(List.of(), ui.messages());
    }
}
