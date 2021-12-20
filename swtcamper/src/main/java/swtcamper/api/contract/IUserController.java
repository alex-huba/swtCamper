package swtcamper.api.contract;

import swtcamper.backend.entities.UserRole;
import swtcamper.backend.services.UserService;
import swtcamper.backend.services.exceptions.GenericServiceException;
import swtcamper.backend.services.exceptions.UserDoesNotExistException;
import swtcamper.backend.services.exceptions.WrongPasswordException;

public interface IUserController {
  //  /**
  //   * see {@link UserService#create}
  //   */
  //  public UserDTO register(UserDTO userDTO) throws GenericServiceException;

  /**
   * see {@link UserService#create}
   */
  public UserDTO register(
    String username,
    String password,
    String email,
    String phone,
    String name,
    String surname,
    UserRole userRole,
    boolean enabled
  );

  //  /**
  //   * see {@link UserService#login}
  //   */
  //  public UserRole login(UserDTO userDTO)
  //          throws GenericServiceException, WrongPasswordException, UserDoesNotExistException;

  /**
   * see {@link UserService#login}
   */
  public UserRoleDTO login(String username, String password)
    throws GenericServiceException, WrongPasswordException, UserDoesNotExistException;

  /**
   * see {@link UserService#isUsernameFree}
   */
  public boolean isUsernameFree(String username);

  /**
   * see {@link UserService#isEmailFree}
   */
  public boolean isEmailFree(String email);

  /**
   * see {@link UserService#resetPassword}
   */
  public void resetPassword(String username, String email, String password)
    throws GenericServiceException;

  /**
   * see {@link UserService#countUser()}
   */
  public long countUser();

  //  /**
  //   * see {@link UserService#isEnabled}
  //   */
  //  public boolean isEnabled(UserDTO userDTO) throws UserDoesNotExistException;

  /**
   * see {@link UserService#isEnabled}
   */
  public boolean isEnabled(String username) throws UserDoesNotExistException;
}
