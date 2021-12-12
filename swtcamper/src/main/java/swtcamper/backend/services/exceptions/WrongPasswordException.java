package swtcamper.backend.services.exceptions;

/**
 * Covers any failure happening while trying to access a non-existing password in the database.
 */
public class WrongPasswordException extends Exception {

  private static final long serialVersionUID = 1L;

  public WrongPasswordException(String errorMessage) {
    super(errorMessage);
  }

  public WrongPasswordException(String string, Exception e) {
    super(string, e);
  }
}
