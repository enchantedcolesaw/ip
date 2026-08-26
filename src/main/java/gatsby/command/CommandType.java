package gatsby.command;

import java.util.Set;

/**
 * The command words that Gatsby understands from user input.
 *
 * This vocabulary is separate from executable {@link Command} objects so the
 * parser can identify a command before Gatsby chooses its implementation.
 */
public enum CommandType {
    BYE("bye", "byebye", "bye bye"),
    LIST("list"),
    MARK("mark"),
    UNMARK("unmark"),
    TODO("todo"),
    DEADLINE("deadline"),
    EVENT("event"),
    DELETE("delete"),
    FIND("find"),
    UNKNOWN();

    private final Set<String> aliases;

    CommandType(String... aliases) {
        this.aliases = Set.of(aliases);
    }

    /**
     * Checks whether this command can be invoked by the supplied normalized word.
     *
     * @param input a lowercase command word or whole-line alias
     * @return true when this command has the supplied alias
     */
    public boolean matchesAlias(String input) {
        return aliases.contains(input);
    }
}
