package gatsby.exception;

/**
 * Signals that a command referred to a task number that does not exist.
 */
public class InvalidTaskException extends GatsbyException {
    public InvalidTaskException(String message) {
        super(message);
    }
}
