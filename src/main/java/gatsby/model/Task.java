package gatsby.model;

/** Represents a task that can be marked as done or left unfinished. */
public class Task {
    /** The text describing the task. */
    private final String description;

    /** Whether the task has been marked as complete. */
    private boolean isDone;

    /**
     * Creates an unfinished task with the supplied description.
     *
     * @param description the task description
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /** Marks this task as complete. */
    public void markDone() {
        this.isDone = true;
    }

    /** Marks this task as incomplete. */
    public void markUndone() {
        this.isDone = false;
    }

    /**
     * Returns whether this task is already marked done.
     *
     * @return true when the task is done
     */
    public boolean isDone() {
        return this.isDone;
    }

    /**
     * Returns the task name, used when writing the task to the save file.
     *
     * @return the description of this task
     */
    public String getTaskName() {
        return this.description;
    }

    /**
     * Returns the status of this task as it is stored on disk.
     *
     * @return "1" when the task is done, "0" otherwise
     */
    public String getStatusFlag() {
        return this.isDone ? "1" : "0";
    }

    /**
     * Returns this task encoded as a single line for the save file.
     * Subclasses prefix their own type letter and extra fields.
     *
     * @return the encoded form of this task
     */
    public String toFileFormat() {
        return getStatusFlag() + " | " + this.description;
    }

    /**
     * Returns this task in the format shown to Gatsby users.
     *
     * @return the formatted task
     */
    @Override
    public String toString() {
        if (this.isDone) {
            return "[X] " + this.description;
        } else {
            return "[ ] " + this.description;
        }
    }
}
