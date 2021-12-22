package swtcamper.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.IUserController;
import swtcamper.api.contract.UserDTO;
import swtcamper.api.contract.UserRoleDTO;
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

  public UserDTO register(
    String username,
    String password,
    String email,
    String phone,
    String name,
    String surname,
    UserRole userRole,
    boolean enabled
  ) {
    return modelMapper.userToUserDTO(
      userService.create(
        username,
        password,
        email,
        phone,
        name,
        surname,
        userRole,
        enabled
      )
    );
  }

  public Long getLoggedInUserID() {
    return userService.getLoggedInUserID();
  }

  @Override
  public UserRoleDTO login(String username, String password)
    throws WrongPasswordException, UserDoesNotExistException, GenericServiceException {
    try {
      return modelMapper.toUserRoleDTO(userService.login(username, password));
    } catch (WrongPasswordException e) {
      throw new WrongPasswordException(e.getMessage());
    } catch (UserDoesNotExistException e) {
      throw new UserDoesNotExistException(e.getMessage());
    } catch (GenericServiceException e) {
      throw new GenericServiceException(e.getMessage());
    }
  }

  @Override
  public boolean isUsernameFree(String username) {
    return userService.isUsernameFree(username);
  }


  @Override
  public boolean isEmailFree(UserDTO userDTO) throws GenericServiceException {
    return userService.isEmailFree(userDTO);
  }

  @Override
  public void resetPassword(UserDTO userDTO) throws GenericServiceException {
    try {
      userService.resetPassword(username, email, password);
    } catch (GenericServiceException e) {
      throw new GenericServiceException(e.getMessage());
    }
  }

  @Override
  public long countUser() {
    return userService.countUser();
  }

  @Override
  public boolean isEnabled(String username) throws UserDoesNotExistException {
    return userService.isEnabled(username);
  }
}
