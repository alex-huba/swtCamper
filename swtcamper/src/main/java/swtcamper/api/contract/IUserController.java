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
    String username,
    String password,
    String email,
    String phone,
    String name,
    String surname,
    UserRole userRole,
    boolean enabled
  );

  User getLoggedInUser();

  /**
   * see {@link UserService#login}
   */
  UserRoleDTO login(UserDTO userDTO)
    throws GenericServiceException, WrongPasswordException, UserDoesNotExistException;

  /**
   * see {@link UserService#isUsernameFree}
   */
  public boolean isUsernameFree(UserDTO userDTO) throws GenericServiceException;

  /**
   * see {@link UserService#isEmailFree}
   */
  boolean isEmailFree(UserDTO userDTO) throws GenericServiceException;

  /**
   * see {@link UserService#resetPassword}
   */
  void resetPassword(UserDTO userDTO)
    throws GenericServiceException;

  /**
   * see {@link UserService#countUser()}
   */
  long countUser();

  /**
   * see {@link UserService#isEnabled}
   */
  boolean isEnabled(UserDTO userDTO) throws UserDoesNotExistException;
}
