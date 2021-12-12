package swtcamper.api.contract;

import swtcamper.backend.entities.UserRole;
import swtcamper.backend.services.UserService;
import swtcamper.backend.services.exceptions.GenericServiceException;
import swtcamper.backend.services.exceptions.UserDoesNotExistException;
import swtcamper.backend.services.exceptions.WrongPasswordException;

public interface IUserController {
  /**
   * see {@link UserService#create}
   */
  public UserDTO register(UserDTO userDTO) throws GenericServiceException;

  /**
   * see {@link UserService#login}
   */
  public UserRole login(UserDTO userDTO)
    throws GenericServiceException, WrongPasswordException, UserDoesNotExistException;

  /**
   * see {@link UserService#isUsernameFree}
   */
  public boolean isUsernameFree(UserDTO userDTO) throws GenericServiceException;

  /**
   * see {@link UserService#isEmailFree}
   */
  public boolean isEmailFree(UserDTO userDTO) throws GenericServiceException;

  /**
   * see {@link UserService#resetPassword}
   */
  public void resetPassword(UserDTO userDTO) throws GenericServiceException;

  /**
   * see {@link UserService#countUser()}
   */
  public long countUser();

  /**
   * see {@link UserService#isEnabled}
   */
  public boolean isEnabled(UserDTO userDTO) throws UserDoesNotExistException;
}
