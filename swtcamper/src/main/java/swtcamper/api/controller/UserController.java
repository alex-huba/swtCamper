package swtcamper.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.IUserController;
import swtcamper.api.contract.UserDTO;
import swtcamper.backend.entities.UserRole;
import swtcamper.backend.services.UserService;
import swtcamper.backend.services.exceptions.GenericServiceException;
import swtcamper.backend.services.exceptions.UserDoesNotExistException;
import swtcamper.backend.services.exceptions.WrongPasswordException;

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
      throw new GenericServiceException("Could not register user.");
    }
  }

  @Override
  public UserRole login(UserDTO userDTO)
    throws WrongPasswordException, UserDoesNotExistException {
    try {
      return userService.login(userDTO);
    } catch (WrongPasswordException e) {
      throw new WrongPasswordException(e.getMessage());
    } catch (UserDoesNotExistException e) {
      throw new UserDoesNotExistException(e.getMessage());
    }
  }

  @Override
  public boolean isUsernameFree(UserDTO userDTO)
    throws GenericServiceException {
    return userService.isUsernameFree(userDTO);
  }

  public Long getLoggedInUserID(){
    return userService.getLoggedInUserID();
  }


  @Override
  public boolean isEmailFree(UserDTO userDTO) throws GenericServiceException {
    return userService.isEmailFree(userDTO);
  }

  @Override
  public void resetPassword(UserDTO userDTO) throws GenericServiceException {
    try {
      userService.resetPassword(userDTO);
    } catch (GenericServiceException e) {
      throw new GenericServiceException(e.getMessage());
    }
  }

  @Override
  public long countUser() {
    return userService.countUser();
  }

  @Override
  public boolean isEnabled(UserDTO userDTO) throws UserDoesNotExistException {
    return userService.isEnabled(userDTO);
  }
}
