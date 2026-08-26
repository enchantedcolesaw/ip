package gatsby.command;

import gatsby.exception.EmptyPayloadException;
import gatsby.model.TaskList;
import gatsby.testutil.RecordingUi;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Tests todo creation and payload validation. */
class TodoCommandTest extends AbstractCommandTest {

    /** Verifies that a valid description creates, saves, and reports a todo. */
    @Test
    void execute_validDescription_addsTodoAndReportsSuccess() throws Exception {
        TaskList tasks = new TaskList();
        RecordingUi ui = recordingUi();

        new TodoCommand("  buy milk  ").execute(tasks, ui, null);

        assertEquals(1, tasks.size());
        assertEquals("buy milk", tasks.get(0).getTaskName());
        assertEquals(List.of(" Got it. I've added this task:",
                "  [T][ ] buy milk", " Now you have 1 task in the list."), ui.messages());
    }

    /** Verifies that an empty description is rejected without changing the list. */
    @Test
    void execute_emptyDescription_throwsException() {
        TaskList tasks = new TaskList();

        EmptyPayloadException exception = assertThrows(EmptyPayloadException.class,
                () -> new TodoCommand("  ").execute(tasks, recordingUi(), null));

        assertEquals(" son the description of a todo cannot be empty -_-!", exception.getMessage());
        assertEquals(0, tasks.size());
    }

    /** Verifies that descriptions cannot corrupt the save-file field structure. */
    @Test
    void execute_descriptionContainsSeparator_throwsException() {
        EmptyPayloadException exception = assertThrows(EmptyPayloadException.class,
                () -> new TodoCommand("read | book").execute(new TaskList(), recordingUi(), null));

        assertEquals(" OOPS! Please leave out the \"|\" character; I use it to separate fields in my save file.",
                exception.getMessage());
    }
}
