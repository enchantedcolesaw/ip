package gatsby.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** A task that takes place during a specified date-time range. */
public class Event extends Task {
    /** The date and time at which the event starts. */
    private final LocalDateTime start;

    /** The date and time at which the event ends. */
    private final LocalDateTime end;

    /**
     * Creates an event task.
     *
     * @param description the task description
     * @param start the event start date and time
     * @param end the event end date and time
     */
    public Event(String description, LocalDateTime start, LocalDateTime end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    /**
     * Returns the event with its date-time range formatted for console display.
     *
     * @return the human-readable event representation
     */
    @Override
    public String toString() {
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm:ss");
        String formattedStart = this.start.format(outputFormatter);
        String formattedEnd = this.end.format(outputFormatter);
        return "[E]" + super.toString() + " (from: " + formattedStart + " to: " + formattedEnd + ")";
    }

    /**
     * Returns the event in the pipe-separated format used by storage.
     *
     * @return the saved event representation
     */
    @Override
    public String toFileFormat() {
        return "E | " + super.toFileFormat() + " | " + this.start + " | " + this.end;
    }
}
