package gatsby.model;

/** Represents a task without a deadline or event time range. */
public class Todo extends Task {

    /**
     * Creates an unfinished todo task.
     *
     * @param description the task description
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns this todo in the format shown to Gatsby users.
     *
     * @return the formatted todo task
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    /**
     * Returns this todo in the format used by Gatsby's save file.
     *
     * @return the serialized todo task
     */
    @Override
    public String toFileFormat() {
        return "T | " + super.toFileFormat();
    }
}
