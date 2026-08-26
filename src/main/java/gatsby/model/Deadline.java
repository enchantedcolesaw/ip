package gatsby.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Represents a task that must be completed by a specific date and time. */
public class Deadline extends Task {
    /** The date and time by which this task should be completed. */
    private final LocalDateTime deadline;

    /**
     * Creates a deadline task.
     *
     * @param description the task description
     * @param deadline the date and time by which the task should be completed
     */
    public Deadline(String description, LocalDateTime deadline) {
        super(description);
        this.deadline = deadline;
    }

    /**
     * Returns this deadline in the format shown to Gatsby users.
     *
     * @return the formatted deadline task
     */
    @Override
    public String toString() {
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm:ss");
        String formattedDeadline = this.deadline.format(outputFormatter);
        return "[D]" + super.toString() + " (by: " + formattedDeadline + ")";
    }

    /**
     * Returns this deadline in the format used by Gatsby's save file.
     *
     * @return the serialized deadline task
     */
    @Override
    public String toFileFormat() {
        // Save the ISO value so Storage can reload it without depending on the
        // human-readable format used by toString().
        return "D | " + super.toFileFormat() + " | " + this.deadline;
    }
}
