/**
 * An executable action requested by the user.
 *
 * Concrete commands gradually take responsibility for one user action. This
 * base class gives Gatsby a common execution protocol and an exit signal.
 */
public abstract class Command {
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
}
