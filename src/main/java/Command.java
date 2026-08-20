import java.util.Locale;
import java.util.Set;

/**
 * The commands that Gatsby understands from user input.
 *
 * Each command stores the input words that can be used to invoke it. The
 * parser keeps goodbye and list commands as whole-line commands, matching
 * Gatsby's existing input behaviour.
 */
public enum Command {
    BYE("bye", "byebye", "bye bye"),
    LIST("list"),
    MARK("mark"),
    UNMARK("unmark"),
    TODO("todo"),
    DEADLINE("deadline"),
    EVENT("event"),
    DELETE("delete"),
    UNKNOWN();

    private final Set<String> aliases;

    Command(String... aliases) {
        this.aliases = Set.of(aliases);
    }

    /**
     * Converts a raw user command into the corresponding command type.
     *
     * @param input the complete line entered by the user
     * @return the matching command, or {@link #UNKNOWN} when no command matches
     */
    public static Command fromInput(String input) {
        String normalizedInput = input.strip()
                .toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", " ");

        if (BYE.aliases.contains(normalizedInput)) {
            return BYE;
        }

        if (LIST.aliases.contains(normalizedInput)) {
            return LIST;
        }

        String action = normalizedInput.split("\\s+", 2)[0];
        for (Command command : values()) {
            if (command != BYE && command != LIST && command.aliases.contains(action)) {
                return command;
            }
        }

        return UNKNOWN;
    }
}
