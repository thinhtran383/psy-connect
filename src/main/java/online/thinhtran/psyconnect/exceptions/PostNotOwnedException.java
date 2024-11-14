package online.thinhtran.psyconnect.exceptions;

public class PostNotOwnedException extends RuntimeException {
    public PostNotOwnedException(String message) {
        super(message);
    }
}
