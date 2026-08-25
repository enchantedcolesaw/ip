import java.util.Locale;

/**
 * Converts a raw console line into a command and its payload.
 *
 * The parser recognizes commands case-insensitively and preserves the payload's
 * original content so descriptions and dates are handled exactly as entered.
 */
public class Parser {
    /**
     * Parses one line of user input.
     *
     * @param input the raw line entered by the user
     * @return the recognized command and the text after it
     */
    public ParsedCommand parse(String input) {
        String trimmedInput = input.strip();
        String[] commandParts = trimmedInput.split("\\s+", 2);
        String payload = commandParts.length > 1 ? commandParts[1].strip() : "";
        return new ParsedCommand(identifyCommand(trimmedInput), payload);
    }

    /**
     * Identifies a command from a complete input line.
     *
     * Goodbye and list are whole-line commands. The remaining commands are
     * recognized from the first word so they can be followed by a payload.
     *
     * @param input the trimmed input line
     * @return the matching command, or {@link Command#UNKNOWN}
     */
    private Command identifyCommand(String input) {
        String normalizedInput = input.toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", " ");

        if (Command.BYE.matchesAlias(normalizedInput)) {
            return Command.BYE;
        }
        if (Command.LIST.matchesAlias(normalizedInput)) {
            return Command.LIST;
        }

        String action = normalizedInput.split("\\s+", 2)[0];
        for (Command command : Command.values()) {
            if (command != Command.BYE && command != Command.LIST
                    && command.matchesAlias(action)) {
                return command;
            }
        }

        return Command.UNKNOWN;
    }

    /**
     * Holds the result of parsing one input line.
     */
    public static class ParsedCommand {
        /** The command recognized from the input. */
        private final Command command;

        /** The text after the command, or an empty string when there is none. */
        private final String payload;

        /**
         * Creates a parsed command result.
         *
         * @param command the recognized command
         * @param payload the text after the command
         */
        public ParsedCommand(Command command, String payload) {
            this.command = command;
            this.payload = payload;
        }

        /**
         * Returns the recognized command.
         *
         * @return the command type
         */
        public Command getCommand() {
            return command;
        }

        /**
         * Returns the text after the command.
         *
         * @return the command payload
         */
        public String getPayload() {
            return payload;
        }
    }
}
