package swtcamper.api.contract;

import swtcamper.backend.services.exceptions.GenericServiceException;
import swtcamper.backend.services.exceptions.UserDoesNotExistException;
import swtcamper.backend.services.exceptions.WrongPasswordException;

public interface IUserController {
  public UserDTO register(UserDTO userDTO) throws GenericServiceException;

  public boolean login(UserDTO userDTO) throws GenericServiceException, WrongPasswordException, UserDoesNotExistException;

  public boolean isUsernameFree(UserDTO userDTO) throws GenericServiceException;

  public void resetPassword(UserDTO userDTO) throws GenericServiceException;
}
