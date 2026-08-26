package gatsby.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** A task that must be completed by a specified date and time. */
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
     * Returns the task with its deadline formatted for console display.
     *
     * @return the human-readable deadline representation
     */
    @Override
    public String toString() {
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm:ss");
        String formattedDeadline = this.deadline.format(outputFormatter);
        return "[D]" + super.toString() + " (by: " + formattedDeadline + ")";
    }

    /**
     * Returns the task in the pipe-separated format used by storage.
     *
     * @return the saved deadline representation
     */
    @Override
    public String toFileFormat() {
        // Save the ISO value so Storage can reload it without depending on the
        // human-readable format used by toString().
        return "D | " + super.toFileFormat() + " | " + this.deadline;
    }
}
