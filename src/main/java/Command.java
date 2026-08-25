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
}
