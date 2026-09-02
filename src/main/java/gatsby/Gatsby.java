package gatsby;

import java.util.ArrayList;
import java.util.List;

import gatsby.command.Command;
import gatsby.command.CommandType;
import gatsby.command.DeadlineCommand;
import gatsby.command.DeleteCommand;
import gatsby.command.EventCommand;
import gatsby.command.ExitCommand;
import gatsby.command.FindCommand;
import gatsby.command.ListCommand;
import gatsby.command.MarkCommand;
import gatsby.command.TodoCommand;
import gatsby.command.UnmarkCommand;
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
    private final TaskList tasks;

    /** Converts raw input lines into commands and payloads. */
    private final Parser parser;

    /** Creates a Gatsby application instance. */
    public Gatsby() {
        tasks = new TaskList(Storage.load());
        parser = new Parser();
    }

    /**
     * Starts Gatsby and processes commands entered through standard input.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        Gatsby gatsby = new Gatsby();
        Ui ui = new Ui();
        ui.showWelcome();
        while (ui.hasNextLine()) {
            ui.showSeparator();
            String input = ui.readLine();
            ui.showSeparator();

            if (gatsby.handleInput(input, ui, true)) {
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
    private boolean handleInput(String input, Ui ui, boolean showSeparators) {
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
            if (showSeparators) {
                ui.showSeparator();
            }
        }
        return false;
    }

    /**
     * Processes one command and returns the user-facing response.
     *
     * @param input the command entered by the user
     * @return Gatsby's response, without console separators
     */
    public String getResponse(String input) {
        List<String> messages = new ArrayList<>();
        Ui responseUi = new Ui(messages::add);
        handleInput(input, responseUi, false);
        responseUi.close();
        return String.join(System.lineSeparator(), messages);
    }

    /**
     * Checks whether an input line asks Gatsby to end the session.
     *
     * @param input the command entered by the user
     * @return true when the input is one of Gatsby's goodbye aliases
     */
    public boolean isExitCommand(String input) {
        return parser.parse(input).getCommand() == CommandType.BYE;
    }

}
