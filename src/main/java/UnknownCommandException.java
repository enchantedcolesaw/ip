/**
 * Signals that the user typed something Gatsby does not recognise.
 */
public class UnknownCommandException extends GatsbyException {
    public UnknownCommandException(String message) {
        super(message);
    }
}
