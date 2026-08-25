/**
 * Ends the current Gatsby session after displaying the goodbye message.
 */
public class ExitCommand extends Command {
    /**
     * Displays the goodbye message. The collaborators are unused for this
     * command, but are accepted to keep the common command protocol uniform.
     *
     * @param tasks the current task list
     * @param ui the console interaction handler
     * @param storage the task persistence handler
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.printLine(" Bye. Hope to see you again soon!");
    }

    /**
     * Indicates that Gatsby should stop reading input after this command.
     *
     * @return true because this command exits the session
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
