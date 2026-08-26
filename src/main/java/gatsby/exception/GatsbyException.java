package gatsby.exception;

/**
 * Base class for every error Gatsby expects and can explain to the user.
 *
 * Having one parent lets the main loop catch all of these with a single
 * {@code catch} block and simply print the message, instead of repeating one
 * catch block per exception type.
 */
public class GatsbyException extends Exception {
    public GatsbyException(String message) {
        super(message);
    }
}
