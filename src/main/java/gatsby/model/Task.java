package gatsby.model;

/** The common description and completion state shared by every task type. */
public class Task {
    /** The text describing the task. */
    private String taskName;

    /** Whether the task has been marked as complete. */
    private boolean isDone;

    /**
     * Creates an unfinished task with the supplied description.
     *
     * @param taskName the task description
     */
    public Task(String taskName) {
        this.taskName = taskName;
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
        return this.taskName;
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
    public String toFileFormat(){
        return getStatusFlag() + " | " + this.taskName;
    }

    @Override
    public String toString() {
        if (this.isDone){
            return "[X] " + this.taskName;
        } else{
            return "[ ] " + this.taskName;
        }
    }
}
