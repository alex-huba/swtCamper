package swtcamper.api.contract;

import swtcamper.backend.services.exceptions.GenericServiceException;

public interface IUserController {
  public UserDTO register(UserDTO userDTO) throws GenericServiceException;

  public boolean login(UserDTO userDTO) throws GenericServiceException;

  public void resetPassword(UserDTO userDTO) throws GenericServiceException;
}
