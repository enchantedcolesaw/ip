package gatsby.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** A task that takes place during a specified date-time range. */
public class Event extends Task {
    /** The date and time at which the event starts. */
    private LocalDateTime from;

    /** The date and time at which the event ends. */
    private LocalDateTime to;

    /**
     * Creates an event task.
     *
     * @param description the task description
     * @param from the event start date and time
     * @param to the event end date and time
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the event with its date-time range formatted for console display.
     *
     * @return the human-readable event representation
     */
    @Override
    public String toString() {
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm:ss");
        String formattedStart = this.from.format(outputFormatter);
        String formattedEnd = this.to.format(outputFormatter);
        return "[E]" + super.toString() + " (from: " + formattedStart + " to: " + formattedEnd + ")";
    }

    /**
     * Returns the event in the pipe-separated format used by storage.
     *
     * @return the saved event representation
     */
    @Override
    public String toFileFormat() {
        return "E | " + super.toFileFormat() + " | " + this.from + " | " + this.to;
    }
}
