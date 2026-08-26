package gatsby.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Represents a task that takes place during a specific time range. */
public class Event extends Task {
    /** The date and time when this event starts. */
    private final LocalDateTime start;

    /** The date and time when this event ends. */
    private final LocalDateTime end;

    /**
     * Creates an event task.
     *
     * @param description the task description
     * @param start the date and time when the event starts
     * @param end the date and time when the event ends
     */
    public Event(String description, LocalDateTime start, LocalDateTime end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    /**
     * Returns this event in the format shown to Gatsby users.
     *
     * @return the formatted event task
     */
    @Override
    public String toString() {
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm:ss");
        String formattedStart = this.start.format(outputFormatter);
        String formattedEnd = this.end.format(outputFormatter);
        return "[E]" + super.toString() + " (from: " + formattedStart + " to: " + formattedEnd + ")";
    }

    /**
     * Returns this event in the format used by Gatsby's save file.
     *
     * @return the serialized event task
     */
    @Override
    public String toFileFormat() {
        return "E | " + super.toFileFormat() + " | " + this.start + " | " + this.end;
    }
}
