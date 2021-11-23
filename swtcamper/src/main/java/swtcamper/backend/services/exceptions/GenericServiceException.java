package swtcamper.backend.services.exceptions;

/**
 * Used to wrap any failure happening on the backend side of the application.
 */
public class GenericServiceException extends Exception {

    private static final long serialVersionUID = 1L;

    public GenericServiceException(String errorMessage) {
        super(errorMessage);
    }

    public GenericServiceException(String string, Exception e) {
        super(string, e);
    }
}
