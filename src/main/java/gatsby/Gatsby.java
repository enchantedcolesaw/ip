package gatsby;

import gatsby.command.*;
import gatsby.exception.GatsbyException;
import gatsby.exception.UnknownCommandException;
import gatsby.model.TaskList;
import gatsby.parser.Parser;
import gatsby.storage.Storage;
import gatsby.ui.Ui;

/**
 * A simple command-line chatbot that stores tasks and remembers them between runs.
 *
 * Tasks are loaded from the save file at startup and saved again after every change.
 * Every expected problem (bad input, an unknown command, a corrupted save file) is
 * reported as a friendly message; the chatbot keeps running.
 */
public class Gatsby {
    /** Tasks are loaded from the save file so the list survives between runs. */
    private static TaskList tasks = new TaskList(Storage.load());

    /** Handles Gatsby's console input and output. */
    private static final Ui ui = new Ui();

    /** Converts raw input lines into commands and payloads. */
    private static final Parser parser = new Parser();

    /**
     * Starts Gatsby and processes commands entered through standard input.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        ui.showWelcome();
        while (ui.hasNextLine()) {
            ui.showSeparator();
            String input = ui.readLine();
            ui.showSeparator();

            if (handleInput(input)) {
                break;
            }
        }
        ui.close();
    }

    /**
     * Handles one line of user input and prints the response.
     *
     * All expected problems are caught here so that a single bad command never ends
     * the session. Unexpected runtime problems are caught too, because losing the
     * whole chat over an unforeseen bug would be worse than reporting it.
     *
     * @param input the raw line the user typed
     * @return true when the user asked to quit, false to keep going
     */
    private static boolean handleInput(String input) {
        try {
            String trimmedInput = input.strip();
            if (trimmedInput.isEmpty()) {
                ui.printLine(" You didn't type anything! Try \"todo read book\" or \"list\".");
                return false;
            }

            Parser.ParsedCommand parsedCommand = parser.parse(input);
            CommandType commandType = parsedCommand.getCommand();
            String payload = parsedCommand.getPayload();

            if (commandType == CommandType.BYE) {
                Command command = new ExitCommand();
                command.execute(tasks, ui, null);
                return command.isExit();
            } else if (commandType == CommandType.LIST) {
                Command command = new ListCommand();
                command.execute(tasks, ui, null);
            } else if (commandType == CommandType.MARK) {
                Command command = new MarkCommand(payload);
                command.execute(tasks, ui, null);
            } else if (commandType == CommandType.UNMARK) {
                Command command = new UnmarkCommand(payload);
                command.execute(tasks, ui, null);
            } else if (commandType == CommandType.TODO
                    || commandType == CommandType.DEADLINE
                    || commandType == CommandType.EVENT
                    || commandType == CommandType.FIND) {
                Command command = switch (commandType) {
                    case TODO -> new TodoCommand(payload);
                    case DEADLINE -> new DeadlineCommand(payload);
                    case EVENT -> new EventCommand(payload);
                    case FIND -> new FindCommand(payload);
                    default -> throw new UnknownCommandException(" I don't recognise this command :'((");
                };
                command.execute(tasks, ui, null);
            } else if (commandType == CommandType.DELETE) {
                Command command = new DeleteCommand(payload);
                command.execute(tasks, ui, null);
            } else {
                throw new UnknownCommandException(" Wait I don't recognise that yet :(\n"
                        + " I know: todo, deadline, event, list, find, mark, unmark, delete, bye.");
            }
        } catch (GatsbyException e) {
            ui.printLine(e.getMessage());
        } catch (RuntimeException e) {
            ui.printLine(" Yikes, something unexpected went wrong: " + e);
        } finally {
            ui.showSeparator();
        }
        return false;
    }

}
