package gatsby.command;

import gatsby.exception.EmptyPayloadException;
import gatsby.exception.GatsbyException;
import gatsby.model.TaskList;
import gatsby.storage.Storage;
import gatsby.ui.Ui;

/**
 * Finds tasks whose descriptions contain a user-supplied keyword.
 */
public class FindCommand extends Command {

    /** The keyword to search for in each task description. */
    private final String payload;

    /**
     * Creates a find command for the supplied keyword.
     *
     * @param payload the keyword entered after {@code find}
     */
    public FindCommand(String payload) {
        this.payload = payload;
    }

    /**
     * Searches the task descriptions and displays the matching tasks.
     *
     * The search uses the task description only, so task metadata such as
     * deadline dates and event times is not considered.
     *
     * @param tasks the current task list
     * @param ui the console interaction handler
     * @param storage the task persistence handler, unused because searching does not change data
     * @throws GatsbyException when the search keyword is empty
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws GatsbyException {
        if (payload.isEmpty()) {
            throw new EmptyPayloadException("OOPS! How do I even find nothing??");
        }

        TaskList matches = new TaskList();
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getTaskName().contains(payload)) {
                matches.add(tasks.get(i));
            }
        }

        if (matches.isEmpty()) {
            ui.printLine(" No matching tasks found :(");
        } else {
            ui.printLine(" Here are the matching tasks in your list:");
            for (int i = 0; i < matches.size(); i++) {
                ui.printLine(" " + (i + 1) + ". " + matches.get(i));
            }
        }
    }
}
