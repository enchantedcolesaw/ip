package gatsby.command;

import gatsby.exception.EmptyPayloadException;
import gatsby.model.Event;
import gatsby.model.TaskList;
import gatsby.testutil.RecordingUi;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Tests event time-range parsing, creation, and validation. */
class EventCommandTest extends AbstractCommandTest {

    /** Verifies that a valid event creates the expected typed task. */
    @Test
    void execute_validEvent_addsEventAndReportsSuccess() throws Exception {
        TaskList tasks = new TaskList();
        RecordingUi ui = recordingUi();

        new EventCommand("project meeting /from 2019-12-02 1400 /to 2019-12-02 1600")
                .execute(tasks, ui, null);

        assertEquals(1, tasks.size());
        Event event = assertInstanceOf(Event.class, tasks.get(0));
        assertEquals("project meeting", event.getTaskName());
        assertEquals("[E][ ] project meeting (from: Dec 02 2019 14:00:00 "
                + "to: Dec 02 2019 16:00:00)", event.toString());
        assertEquals(List.of(" Got it. I've added this task:",
                "  [E][ ] project meeting (from: Dec 02 2019 14:00:00 to: Dec 02 2019 16:00:00)",
                " Now you have 1 task in the list."), ui.messages());
    }

    /** Verifies that an event without a start keyword is rejected. */
    @Test
    void execute_missingFromKeyword_throwsException() {
        EmptyPayloadException exception = assertThrows(EmptyPayloadException.class,
                () -> new EventCommand("project meeting").execute(new TaskList(), recordingUi(), null));

        assertEquals(" son there's no event name/timing for this event -_-!", exception.getMessage());
    }

    /** Verifies that an event without an end keyword is rejected. */
    @Test
    void execute_missingToKeyword_throwsException() {
        EmptyPayloadException exception = assertThrows(EmptyPayloadException.class,
                () -> new EventCommand("project meeting /from 2019-12-02 1400")
                        .execute(new TaskList(), recordingUi(), null));

        assertEquals(" son this event has no end time, it's infinite! -_-!", exception.getMessage());
    }

    /** Verifies that malformed event times are not silently accepted. */
    @Test
    void execute_invalidDate_throwsDateParseException() {
        assertThrows(DateTimeParseException.class,
                () -> new EventCommand("meeting /from today /to tomorrow")
                        .execute(new TaskList(), recordingUi(), null));
    }
}
