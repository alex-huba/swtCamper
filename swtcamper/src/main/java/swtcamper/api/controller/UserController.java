package swtcamper.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.IUserController;
import swtcamper.api.contract.UserDTO;
import swtcamper.backend.services.UserService;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class UserController implements IUserController {

  @Autowired
  UserService userService;

  @Autowired
  ModelMapper modelMapper;

  public UserDTO register(UserDTO userDTO) throws GenericServiceException {
    try {
      return modelMapper.userToUserDTO(userService.create(userDTO));
    } catch (GenericServiceException e) {
      // TODO: Check exception handling
      throw new GenericServiceException("Could not register user.");
    }
  }

  public boolean login(UserDTO userDTO) throws GenericServiceException {
    try {
      return userService.login(userDTO);
    } catch (GenericServiceException e) {
      throw new GenericServiceException("User doesn't exist.");
    }
  }

  public boolean isUsernameFree(UserDTO userDTO) throws GenericServiceException {
    return userService.isUsernameFree(userDTO);
  }

  public boolean isEmailFree(UserDTO userDTO) throws GenericServiceException {
    return userService.isEmailFree(userDTO);
  }

  public void resetPassword(UserDTO userDTO) throws GenericServiceException {
    try {
      userService.resetPassword(userDTO);
    } catch (GenericServiceException e) {
      throw new GenericServiceException(e.getMessage());
    }
  }
}