package swtcamper.backend.services.exceptions;

public class UserDoesNotExistException extends Exception {

    private static final long serialVersionUID = 1L;

    public UserDoesNotExistException(String errorMessage) {
        super(errorMessage);
    }

    public UserDoesNotExistException(String string, Exception e) {
        super(string, e);
    }
}