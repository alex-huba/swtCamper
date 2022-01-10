package swtcamper.api.contract;

import swtcamper.backend.entities.User;
import swtcamper.backend.entities.UserRole;
import swtcamper.backend.services.UserService;
import swtcamper.backend.services.exceptions.GenericServiceException;
import swtcamper.backend.services.exceptions.UserDoesNotExistException;
import swtcamper.backend.services.exceptions.WrongPasswordException;

public interface IUserController {
  /**
   * see {@link UserService#create}
   */
  UserDTO register(
    User user
  );

  User getLoggedInUser();

  /**
   * see {@link UserService#login}
   */
  UserRoleDTO login(String username, String password)
    throws GenericServiceException, WrongPasswordException, UserDoesNotExistException;

  /**
   * see {@link UserService#isUsernameFree}
   */
  boolean isUsernameFree(String username);

  /**
   * see {@link UserService#isEmailFree}
   */
  boolean isEmailFree(String email);

  /**
   * see {@link UserService#resetPassword}
   */
  void resetPassword(String username, String email, String password)
    throws GenericServiceException;

  /**
   * see {@link UserService#countUser()}
   */
  long countUser();

  /**
   * see {@link UserService#isEnabled}
   */
  boolean isEnabled(String username) throws UserDoesNotExistException;
}
