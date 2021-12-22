package swtcamper.backend.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swtcamper.backend.entities.User;
import swtcamper.backend.entities.UserRole;
import swtcamper.backend.repositories.UserRepository;
import swtcamper.backend.services.exceptions.GenericServiceException;
import swtcamper.backend.services.exceptions.UserDoesNotExistException;
import swtcamper.backend.services.exceptions.WrongPasswordException;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  private User loggedInUser;

  public User getLoggedInUser() {
    return loggedInUser;
  }

  public void setLoggedInUser(User loggedInUser) {
    this.loggedInUser = loggedInUser;
  }

  /**
   * Creates and stores a new user in the database with the provided username, name, surname, email, phone number and
   * password.
   * @param username
   * @param password
   * @param email
   * @param phone
   * @param name
   * @param surname
   * @param userRole
   * @param enabled
   * @return
   * @throws GenericServiceException
   */
  public User create(
    String username,
    String password,
    String email,
    String phone,
    String name,
    String surname,
    UserRole userRole,
    boolean enabled
  ) {
    User user = new User();
    user.setUsername(username);
    user.setName(name);
    user.setSurname(surname);
    user.setEmail(email);
    user.setPhone(phone);
    user.setPassword(password);
    user.setUserRole(userRole);
    user.setEnabled(enabled);
    return userRepository.save(user);
  }

  public void delete(User user) {
    // TODO: implement user deletion
  }

  public void update(User user) {
    // TODO: implement user update
  }

  /**
   * Finds all user.
   * @return
   * @throws GenericServiceException if there are no user in the database
   */
  public List<User> user() throws GenericServiceException {
    if (userRepository.findAll().isEmpty()) {
      throw new GenericServiceException(
        "No users found. User database is empty."
      );
    }
    return userRepository.findAll();
  }

  /**
   * Checks if user exists in database and gets information about user role of the user if it does.
   * @param username
   * @param password
   * @return the user role of the user if user already exists in database
   * @throws WrongPasswordException if the password doesn't match with the username
   * @throws UserDoesNotExistException if username wasn't found in the database
   */
  public UserRole login(String username, String password)
    throws WrongPasswordException, UserDoesNotExistException {
    // Check if username and password are matching
    if (userRepository.existsByUsernameAndPassword(username, password)) {
      User user;
      Optional<User> userOptional = userRepository.findByUsername(username);
      if (userOptional.isPresent()) {
        user = userOptional.get();
        this.setLoggedInUser(user);
      } else {
        throw new UserDoesNotExistException("User doesn't exist.");
      }
      // Username and password are matching
      return user.getUserRole();
    }
    // Check if either username or password exists to see if user typed one of them wrong
    if (userRepository.existsByUsername(username)) {
      throw new WrongPasswordException("Wrong password. Please try again.");
    }
    throw new UserDoesNotExistException("Username doesn't exist.");
  }

  /**
   * Checks if username is already existing in database.
   * @param username
   * @return true if username doesn't exist in database yet
   * @return false if username is already taken in database
   */
  public boolean isUsernameFree(String username) {
    return !userRepository.existsByUsername(username);
  }

  /**
   * Checks if email is already existing in database.
   * @param email
   * @return true if email doesn't exist in database yet
   * @return false if email is already taken in database
   */
  public boolean isEmailFree(String email) {
    return !userRepository.existsByEmail(email);
  }

  /**
   * Locks an user account.
   * @param userID
   */
  public void lock(Long userID) {
    Optional<User> userOptional = userRepository.findById(userID);
    if (userOptional.isPresent()) {
      User userToLock = userOptional.get();
      userToLock.setLocked(true);
      userRepository.save(userToLock);
    }
  }

  /**
   * Unlocks an user account.
   * @param userID
   */
  public void unlock(Long userID) {
    Optional<User> userOptional = userRepository.findById(userID);
    if (userOptional.isPresent()) {
      User userToUnlock = userOptional.get();
      userToUnlock.setLocked(false);
      userRepository.save(userToUnlock);
    }
  }

  /**
   * Enables an user account.
   * @param userID
   */
  public void enable(Long userID) {
    Optional<User> userOptional = userRepository.findById(userID);
    if (userOptional.isPresent()) {
      User userToEnable = userOptional.get();
      userToEnable.setEnabled(true);
      userRepository.save(userToEnable);
    }
  }

  /**
   * Checks if an user account is enabled.
   * @param username
   * @return
   * @throws UserDoesNotExistException if there is no user account found in database
   */
  public boolean isEnabled(String username) throws UserDoesNotExistException {
    Optional<User> userOptional = userRepository.findByUsername(username);
    if (userOptional.isPresent()) {
      return userRepository.findByUsername(username).get().isEnabled();
    }
    throw new UserDoesNotExistException("User does not exist");
  }

  /**
   * Changes an users' password.
   * @param username
   * @param email
   * @param password
   * @throws GenericServiceException if user account doesn't exist in database
   */
  public void resetPassword(String username, String email, String password)
    throws GenericServiceException {
    // Check if user exists in database
    if (userRepository.existsByUsernameAndEmail(username, email)) {
      // Get user if it exists in database, change password and save it back on database
      User user = userRepository.findByUsername(username).get();
      user.setPassword(password);
      userRepository.save(user);
    } else {
      throw new GenericServiceException(
        "Couldn't change password. Username or password is not correct."
      );
    }
  }

  /**
   * Counts the number of user accounts that exist in the database.
   * @return
   */
  public long countUser() {
    return userRepository.count();
  }
}
