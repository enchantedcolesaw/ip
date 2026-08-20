public class UnknownCommandException extends Exception{
    public UnknownCommandException(String message){
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
