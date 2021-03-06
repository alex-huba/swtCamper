package swtcamper.api.controller;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.UserDTO;
import swtcamper.api.contract.interfaces.IUserController;
import swtcamper.backend.entities.User;
import swtcamper.backend.entities.UserRole;
import swtcamper.backend.services.UserService;
import swtcamper.backend.services.exceptions.GenericServiceException;
import swtcamper.backend.services.exceptions.UserDoesNotExistException;
import swtcamper.backend.services.exceptions.WrongPasswordException;

@Component
public class UserController implements IUserController {

  @Autowired
  private UserService userService;

  @Autowired
  private ModelMapper modelMapper;

  @Override
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

  @Override
  public User getLoggedInUser() {
    return userService.getLoggedInUser();
  }

  public void setLoggedInUser(User user) {
    userService.setLoggedInUser(user);
  }

  @Override
  public ArrayList<User> getAllUsers() throws GenericServiceException {
    return new ArrayList<>(userService.user());
  }

  @Override
  public void excludeRenterForCurrentlyLoggedInUser(long idOfRenterToExclude)
    throws GenericServiceException {
    userService.excludeRenterForCurrentlyLoggedInUser(idOfRenterToExclude);
  }

  @Override
  public void removeExcludedRenterForCurrentlyLoggedInUser(
    long idOfRenterToInclude
  ) throws GenericServiceException {
    userService.removeExcludedRenterForCurrentlyLoggedInUser(
      idOfRenterToInclude
    );
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

  @Override
  public void logout() {
    userService.logout();
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
  public boolean isThereAnyDisabledUser() throws GenericServiceException {
    return userService.isThereAnyDisabledUser();
  }

  @Override
  public User getUserById(long id) throws GenericServiceException {
    return userService.getUserById(id);
  }

  @Override
  public User getUserByUsername(String username)
    throws GenericServiceException {
    return userService.getUserByUsername(username);
  }

  @Override
  public void enableUserById(long id) {
    userService.enable(id, getLoggedInUser().getUsername());
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
