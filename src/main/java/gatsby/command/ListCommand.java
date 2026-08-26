package gatsby.command;

import gatsby.model.TaskList;
import gatsby.storage.Storage;
import gatsby.ui.Ui;

/**
 * Displays every task currently held by Gatsby.
 */
public class ListCommand extends Command {
    /** Creates the command that displays the current task list. */
    public ListCommand() {
    }

    /**
     * Prints the task list, including the empty-list message when appropriate.
     *
     * @param tasks the current task list
     * @param ui the console interaction handler
     * @param storage the task persistence handler, unused because listing does not change data
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.printLine(" Here are the tasks in your list:");
        if (tasks.isEmpty()) {
            ui.printLine(" There's nothing here yet! Go ahead and add any tasks you'd like! :)");
            return;
        }
        for (int i = 0; i < tasks.size(); i++) {
            ui.printLine(" " + (i + 1) + ". " + tasks.get(i));
        }
    }
}
