package gatsby.exception;

/**
 * Signals that a command was given without the details it needs, such as a description or a date.
 */
public class EmptyPayloadException extends GatsbyException {
    public EmptyPayloadException(String message) {
        super(message);
    }
}
