package swtcamper.api.controller;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.IUserController;
import swtcamper.api.contract.UserDTO;
import swtcamper.backend.entities.User;
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

  public User getLoggedInUser() {
    return userService.getLoggedInUser();
  }

  public ArrayList<User> getAllUsers() throws GenericServiceException {
    return new ArrayList<>(userService.user());
  }

  @Override
  public UserDTO login(String username, String password)
    throws WrongPasswordException, UserDoesNotExistException, GenericServiceException {
    try {
      return modelMapper.userToUserDTO(userService.login(username, password));
    } catch (WrongPasswordException e) {
      throw new WrongPasswordException(e.getMessage());
    } catch (UserDoesNotExistException e) {
      throw new UserDoesNotExistException(e.getMessage());
    }
  }

  public void logout() {
    userService.setLoggedInUser(null);
  }

  @Override
  public boolean isUsernameFree(String username) {
    return userService.isUsernameFree(username);
  }

  @Override
  public boolean isEmailFree(String email) {
    return userService.isEmailFree(email);
  }

  @Override
  public void resetPassword(String username, String email, String password)
    throws GenericServiceException {
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

  @Override
  public User getUserById(long id) throws GenericServiceException {
    return userService.getUserById(id);
  }

  @Override
  public void enableUserById(long id) {
    userService.enable(id, getLoggedInUser().getUsername());
  }

  @Override
  public void ignoreUserById(long id) {
    userService.ignore(id, getLoggedInUser().getUsername());
  }

  @Override
  public void blockUserById(long id) {
    userService.lock(id, getLoggedInUser().getUsername());
  }

  @Override
  public void unblockUserById(long id) {
    userService.unlock(id, getLoggedInUser().getUsername());
  }

  @Override
  public void degradeUserById(long id) throws GenericServiceException {
    userService.degradeUser(id, getLoggedInUser().getUsername());
  }

  @Override
  public void promoteUserById(long id) throws GenericServiceException {
    userService.promoteUser(id, getLoggedInUser().getUsername());
  }
}
