/**
 * An executable action requested by the user.
 *
 * Concrete commands gradually take responsibility for one user action. This
 * base class gives Gatsby a common execution protocol and an exit signal.
 */
public abstract class Command {
    /** The character reserved for separating fields in the save file. */
    private static final String FIELD_SEPARATOR = "|";

    /**
     * Performs this command using Gatsby's collaborators.
     *
     * @param tasks the current task list
     * @param ui the console interaction handler
     * @param storage the task persistence handler
     * @throws GatsbyException when the command cannot be completed
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws GatsbyException;

    /**
     * Checks whether this command ends the current Gatsby session.
     *
     * @return false for ordinary commands
     */
    public boolean isExit() {
        return false;
    }

    /**
     * Adds a newly created task, saves it, and prints Gatsby's standard success
     * response for task-creation commands.
     *
     * @param tasks the current task list
     * @param ui the console interaction handler
     * @param task the newly created task
     */
    protected void addTask(TaskList tasks, Ui ui, Task task) {
        tasks.add(task);
        Storage.save(tasks.asList());
        ui.printLine(" Got it. I've added this task:");
        ui.printLine("  " + task);
        ui.printLine(" Now you have " + tasks.size()
                + (tasks.size() == 1 ? " task" : " tasks") + " in the list.");
    }

    /**
     * Validates a description or date fragment shared by task-creation commands.
     *
     * @param text the candidate text
     * @param errorMessage the message shown when the text is empty
     * @return the trimmed, valid text
     * @throws EmptyPayloadException when the text is empty or contains the field separator
     */
    protected String requireText(String text, String errorMessage) throws EmptyPayloadException {
        String trimmedText = text.strip();
        if (trimmedText.isEmpty()) {
            throw new EmptyPayloadException(errorMessage);
        }
        if (trimmedText.contains(FIELD_SEPARATOR)) {
            throw new EmptyPayloadException(" OOPS! Please leave out the \"" + FIELD_SEPARATOR
                    + "\" character; I use it to separate fields in my save file.");
        }
        return trimmedText;
    }

    /**
     * Splits a payload around a case-insensitive keyword such as {@code /by}.
     *
     * @param payload the text to split
     * @param keyword the keyword to split on
     * @param errorMessage the message shown when the keyword is absent
     * @return the text before and after the keyword
     * @throws EmptyPayloadException when the keyword does not appear
     */
    protected String[] splitOnKeyword(String payload, String keyword, String errorMessage)
            throws EmptyPayloadException {
        String[] parts = payload.split("(?i)\\s*" + keyword + "\\s*", 2);
        if (parts.length < 2) {
            throw new EmptyPayloadException(errorMessage);
        }
        return parts;
    }

    /**
     * Resolves a one-based user task number to the corresponding zero-based index.
     *
     * @param tasks the current task list
     * @param payload the text entered after a task-selection command
     * @return the zero-based index of the selected task
     * @throws InvalidTaskException when the payload is not a valid task number
     */
    protected int parseTaskIndex(TaskList tasks, String payload) throws InvalidTaskException {
        int taskNumber;
        try {
            taskNumber = Integer.parseInt(payload);
        } catch (NumberFormatException e) {
            throw new InvalidTaskException(" OOPS! \"" + payload + "\" isn't a task number! :(");
        }
        if (tasks.isEmpty()) {
            throw new InvalidTaskException(" OOPS! Your list is empty, so there's no task "
                    + taskNumber + "!");
        }
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new InvalidTaskException(" OOPS! There's no task " + taskNumber
                    + "! Pick a number from 1 to " + tasks.size() + ".");
        }
        return taskNumber - 1;
    }

    /**
     * Marks or unmarks one task and reports whether its state actually changed.
     *
     * @param tasks the current task list
     * @param ui the console interaction handler
     * @param payload the selected task number
     * @param isMarking true to mark the task, false to unmark it
     * @throws GatsbyException when the task number is missing or invalid
     */
    protected void updateTaskStatus(TaskList tasks, Ui ui, String payload, boolean isMarking)
            throws GatsbyException {
        if (payload.isEmpty()) {
            throw new EmptyMarkingException(isMarking
                    ? " OOPS! We can't be marking nothing as done!"
                    : " OOPS! We can't be marking nothing as undone!");
        }

        Task task = tasks.get(parseTaskIndex(tasks, payload));
        boolean wasAlreadyInState = task.isDone() == isMarking;
        if (isMarking) {
            task.markDone();
        } else {
            task.markUndone();
        }
        Storage.save(tasks.asList());

        if (wasAlreadyInState) {
            ui.printLine(isMarking
                    ? " That one was already done, but sure:"
                    : " That one wasn't done yet, but sure:");
        } else {
            ui.printLine(isMarking
                    ? " Nice! I've marked this task as done:"
                    : " OK, I've marked this task as not done yet:");
        }
        ui.printLine("  " + task);
    }

    /**
     * Prints the current task count with correct singular/plural wording.
     *
     * @param tasks the current task list
     * @param ui the console interaction handler
     */
    protected void printTaskCount(TaskList tasks, Ui ui) {
        ui.printLine(" Now you have " + tasks.size()
                + (tasks.size() == 1 ? " task" : " tasks") + " in the list.");
    }
}
