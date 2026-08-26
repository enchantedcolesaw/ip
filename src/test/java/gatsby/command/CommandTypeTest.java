package gatsby.command;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Tests command alias matching. */
class CommandTypeTest {

    /** Verifies that each supported alias is recognized by its command type. */
    @Test
    void matchesAlias_supportedAliases_returnsTrue() {
        assertTrue(CommandType.BYE.matchesAlias("bye"));
        assertTrue(CommandType.BYE.matchesAlias("bye bye"));
        assertTrue(CommandType.LIST.matchesAlias("list"));
        assertTrue(CommandType.TODO.matchesAlias("todo"));
        assertTrue(CommandType.DEADLINE.matchesAlias("deadline"));
        assertTrue(CommandType.EVENT.matchesAlias("event"));
        assertTrue(CommandType.MARK.matchesAlias("mark"));
        assertTrue(CommandType.UNMARK.matchesAlias("unmark"));
        assertTrue(CommandType.DELETE.matchesAlias("delete"));
    }

    /** Verifies that aliases are exact and do not accept unknown words. */
    @Test
    void matchesAlias_unknownOrWrongCaseInput_returnsFalse() {
        assertFalse(CommandType.TODO.matchesAlias("TODO"));
        assertFalse(CommandType.UNKNOWN.matchesAlias("todo"));
        assertFalse(CommandType.LIST.matchesAlias("listing"));
    }
}
