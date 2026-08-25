import java.util.Set;

/**
 * The commands that Gatsby understands from user input.
 *
 * Each command stores the input words that can be used to invoke it. The
 * {@link Parser} uses these aliases while interpreting user input.
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
     * Checks whether this command can be invoked by the supplied normalized word.
     *
     * @param input a lowercase command word or whole-line alias
     * @return true when this command has the supplied alias
     */
    boolean matchesAlias(String input) {
        return aliases.contains(input);
    }
}
