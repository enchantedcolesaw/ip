package gatsby.exception;

/**
 * Signals that a mark or unmark command was given without a task number.
 */
public class EmptyMarkingException extends GatsbyException {
    public EmptyMarkingException(String message) {
        super(message);
    }
}
