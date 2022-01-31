package swtcamper.backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swtcamper.api.controller.HashHelper;
import swtcamper.api.controller.LoggingController;
import swtcamper.backend.entities.LoggingLevel;
import swtcamper.backend.entities.LoggingMessage;
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

  @Autowired
  private LoggingController loggingController;

  @Autowired
  private HashHelper hashHelper;

  private User loggedInUser;

  /**
   * Creates and stores a new user in the database with the provided username, name, surname, email, phone number and
   * password.
   *
   * @param username Unique username
   * @param password password with at least length of 8
   * @param email    unique email address (gets validated in {@link swtcamper.javafx.controller.RegisterViewController})
   * @param phone    unique phone number
   * @param name     first name of user
   * @param surname  last name of user
   * @param userRole {@link UserRole} the user wants to start with
   * @param enabled  if user wants to be a Provider, he/she is disabled by default until an Operator accepts him/her
   * @return created User
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
    user.setPassword(HashHelper.hashIt(password));
    user.setUserRole(userRole);
    user.setEnabled(enabled);
    userRepository.save(user);

    // no is-present check because userRepository.save(user) will have definitely created this user
    long newId = userRepository.findByUsername(username).get().getId();
    loggingController.log(
      new LoggingMessage(
        LoggingLevel.INFO,
        String.format(
          "New user with ID %s and username '%s' registered.",
          newId,
          username
        )
      )
    );
    return userRepository.findById(newId).get();
  }

  /**
   * Deletes a user by his/her ID
   *
   * @param id of the user to delete
   */
  public void delete(long id) {
    loggingController.log(
      new LoggingMessage(
        LoggingLevel.INFO,
        String.format("User with ID %s deleted.", id)
      )
    );
    userRepository.deleteById(id);
  }

  /**
   * Overwrites an existing user
   *
   * @param user to get updates with new values
   */
  public User update(User user) {
    loggingController.log(
      new LoggingMessage(
        LoggingLevel.INFO,
        String.format("User with ID %s updated.", user.getId())
      )
    );
    return userRepository.save(user);
  }

  public boolean isThereAnyDisabledUser() throws GenericServiceException {
    return user().stream().anyMatch(user -> !user.isEnabled());
  }

  /**
   * Finds a specific user by its id.
   *
   * @param userId ID of user to find
   * @return User with specified ID
   * @throws GenericServiceException
   */
  public User getUserById(long userId) throws GenericServiceException {
    Optional<User> userOptional = userRepository.findById(userId);
    if (userOptional.isPresent()) {
      return userOptional.get();
    }
    throw new GenericServiceException(
      "There is no user with ID " + userId + "."
    );
  }

  /**
   * Finds a specific user by its username.
   *
   * @param username of user to find
   * @return User with specified username
   * @throws GenericServiceException
   */
  public User getUserByUsername(String username)
    throws GenericServiceException {
    Optional<User> userOptional = userRepository.findByUsername(username);
    if (userOptional.isPresent()) {
      return userOptional.get();
    }
    throw new GenericServiceException(
      "There is no user with username '" + username + "'."
    );
  }

  /**
   * Finds all users.
   *
   * @return List of all available users
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
   * Get the currently logged-in User
   *
   * @return User of the person who is currently using the application
   */
  public User getLoggedInUser() {
    return loggedInUser;
  }

  /**
   * Sets the currently logged-in User
   *
   * @param loggedInUser User that is logged-in from now on
   */
  public void setLoggedInUser(User loggedInUser) {
    this.loggedInUser = loggedInUser;
  }

  /**
   * Adds the User that has the given ID to the list of excluded renters of the currently logged-in User
   * ==> Given User will no longer see offers from the currently logged-in User, if logged-in
   *
   * @param idOfRenterToExclude ID of the User to exclude
   * @throws GenericServiceException
   */
  public void excludeRenterForCurrentlyLoggedInUser(long idOfRenterToExclude)
    throws GenericServiceException {
    User user = getLoggedInUser();
    ArrayList<Long> excludedRenters = getLoggedInUser().getExcludedRenters() ==
      null ||
      getLoggedInUser().getExcludedRenters().isEmpty()
      ? new ArrayList<>()
      : getLoggedInUser().getExcludedRenters();
    excludedRenters.add(idOfRenterToExclude);
    user.setExcludedRenters(excludedRenters);

    loggingController.log(
      new LoggingMessage(
        LoggingLevel.INFO,
        String.format(
          "User %s was excluded by %s.",
          getUserById(idOfRenterToExclude).getUsername(),
          loggedInUser.getUsername()
        )
      )
    );
    userRepository.save(user);
  }

  /**
   * Removes the User that has the given ID from the list of excluded renters of the currently logged-in User
   * ==> Given User will see offers from the currently logged-in User again, if logged-in
   *
   * @param idOfRenterToInclude ID of the User to include
   * @throws GenericServiceException
   */
  public void removeExcludedRenterForCurrentlyLoggedInUser(
    long idOfRenterToInclude
  ) throws GenericServiceException {
    User user = getLoggedInUser();
    ArrayList<Long> excludedRenters = getLoggedInUser().getExcludedRenters();
    excludedRenters.remove(idOfRenterToInclude);
    user.setExcludedRenters(excludedRenters);

    loggingController.log(
      new LoggingMessage(
        LoggingLevel.INFO,
        String.format(
          "User %s was included by %s.",
          getUserById(idOfRenterToInclude).getUsername(),
          loggedInUser.getUsername()
        )
      )
    );
    userRepository.save(user);
  }

  /**
   * Checks if user exists in database and gets information about user role of the user if it does.
   *
   * @param username
   * @param password
   * @return the user role of the user if user already exists in database
   * @throws WrongPasswordException    if the password doesn't match with the username
   * @throws UserDoesNotExistException if username wasn't found in the database
   */
  public User login(String username, String password)
    throws WrongPasswordException, UserDoesNotExistException {
    // Check if username and password are matching
    String hashedPassword = HashHelper.hashIt(password);
    if (userRepository.existsByUsernameAndPassword(username, hashedPassword)) {
      User user = new User();
      Optional<User> userOptional = userRepository.findByUsername(username);
      if (userOptional.isPresent()) {
        user = userOptional.get();
        this.setLoggedInUser(user);
      }
      // Username and password are matching
      loggingController.log(
        new LoggingMessage(
          LoggingLevel.INFO,
          String.format("User %s logged in.", username)
        )
      );
      return user;
    }
    // Check if either username or password exists to see if user typed one of them wrong
    if (userRepository.existsByUsername(username)) {
      loggingController.log(
        new LoggingMessage(
          LoggingLevel.WARNING,
          String.format("Wrong password entered for user %s.", username)
        )
      );
      throw new WrongPasswordException("Wrong password. Please try again.");
    }
    loggingController.log(
      new LoggingMessage(
        LoggingLevel.WARNING,
        String.format(
          "Username %s tried to log in, but does not exist.",
          username
        )
      )
    );
    throw new UserDoesNotExistException("Username doesn't exist.");
  }

  /**
   * Sets the current logged-in user to null
   */
  public void logout() {
    loggingController.log(
      new LoggingMessage(
        LoggingLevel.INFO,
        String.format("User %s logged out.", loggedInUser.getUsername())
      )
    );
    setLoggedInUser(null);
  }

  /**
   * Checks if username is already existing in database.
   *
   * @param username
   * @return true if username doesn't exist in database yet
   * @return false if username is already taken in database
   */
  public boolean isUsernameFree(String username) {
    return !userRepository.existsByUsername(username);
  }

  /**
   * Checks if email is already existing in database.
   *
   * @param email
   * @return true if email doesn't exist in database yet
   * @return false if email is already taken in database
   */
  public boolean isEmailFree(String email) {
    return !userRepository.existsByEmail(email);
  }

  /**
   * Locks an user account.
   *
   * @param userID
   * @param operator
   */
  public void lock(Long userID, String operator) {
    Optional<User> userOptional = userRepository.findById(userID);
    if (userOptional.isPresent()) {
      User userToLock = userOptional.get();
      userToLock.setLocked(true);

      loggingController.log(
        new LoggingMessage(
          LoggingLevel.INFO,
          String.format(
            "User %s was locked by operator %s.",
            userToLock.getUsername(),
            operator
          )
        )
      );
      userRepository.save(userToLock);
    }
  }

  /**
   * Unlocks an user account.
   *
   * @param userID
   * @param operator
   */
  public void unlock(Long userID, String operator) {
    Optional<User> userOptional = userRepository.findById(userID);
    if (userOptional.isPresent()) {
      User userToUnlock = userOptional.get();
      userToUnlock.setLocked(false);

      loggingController.log(
        new LoggingMessage(
          LoggingLevel.INFO,
          String.format(
            "User %s was unlocked by operator %s.",
            userToUnlock.getUsername(),
            operator
          )
        )
      );
      userRepository.save(userToUnlock);
    }
  }

  /**
   * Enables an user account.
   *
   * @param userID
   * @param operator
   */
  public void enable(Long userID, String operator) {
    Optional<User> userOptional = userRepository.findById(userID);
    if (userOptional.isPresent()) {
      User userToEnable = userOptional.get();
      userToEnable.setEnabled(true);

      loggingController.log(
        new LoggingMessage(
          LoggingLevel.INFO,
          String.format(
            "User %s was enabled by operator %s.",
            userToEnable.getUsername(),
            operator
          )
        )
      );
      userRepository.save(userToEnable);
    }
  }

  /**
   * Checks if a user account is enabled.
   *
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
   * Promotes a specified user to the next best user role.
   *
   * @param id of the user to promote
   * @return promoted user
   * @throws GenericServiceException if there is no user with such ID
   */
  public User promoteUser(long id, String operator)
    throws GenericServiceException {
    User user = getUserById(id);
    if (user.getUserRole().equals(UserRole.RENTER)) {
      user.setUserRole(UserRole.PROVIDER);
      loggingController.log(
        new LoggingMessage(
          LoggingLevel.INFO,
          String.format(
            "User %s was promoted to UserRole 'Provider' by operator %s.",
            user.getUsername(),
            operator
          )
        )
      );
    } else if (user.getUserRole().equals(UserRole.PROVIDER)) {
      user.setUserRole(UserRole.OPERATOR);
      loggingController.log(
        new LoggingMessage(
          LoggingLevel.INFO,
          String.format(
            "User %s was promoted to UserRole 'Operator' by operator %s.",
            user.getUsername(),
            operator
          )
        )
      );
    }
    return userRepository.save(user);
  }

  /**
   * Degrades a specified user to the next worse user role.
   *
   * @param id of the user to degrade
   * @return degraded user
   * @throws GenericServiceException if there is no user with such ID
   */
  public User degradeUser(long id, String operator)
    throws GenericServiceException {
    User user = getUserById(id);
    if (user.getUserRole().equals(UserRole.PROVIDER)) {
      user.setUserRole(UserRole.RENTER);
      loggingController.log(
        new LoggingMessage(
          LoggingLevel.INFO,
          String.format(
            "User %s was degraded to UserRole 'Renter' by operator %s.",
            user.getUsername(),
            operator
          )
        )
      );
    } else if (user.getUserRole().equals(UserRole.OPERATOR)) {
      user.setUserRole(UserRole.PROVIDER);
      loggingController.log(
        new LoggingMessage(
          LoggingLevel.INFO,
          String.format(
            "User %s was degraded to UserRole 'Provider' by operator %s.",
            user.getUsername(),
            operator
          )
        )
      );
    }
    return userRepository.save(user);
  }

  /**
   * Changes a users' password.
   *
   * @param username Username of the user whose password shall be reset
   * @param email    Email of the user whose password shall be reset (for validation)
   * @param password new password
   * @throws GenericServiceException if user account doesn't exist in database
   */
  public void resetPassword(String username, String email, String password)
    throws GenericServiceException {
    // Check if user exists in database
    if (userRepository.existsByUsernameAndEmail(username, email)) {
      // Get user if it exists in database, change password and save it back on database
      User user = userRepository.findByUsername(username).get();
      String hashedPassword = HashHelper.hashIt(password);
      user.setPassword(hashedPassword);
      userRepository.save(user);

      loggingController.log(
        new LoggingMessage(
          LoggingLevel.INFO,
          String.format("User %s's password got reset.", username)
        )
      );
    } else {
      throw new GenericServiceException(
        "Couldn't change password. Username or password is not correct."
      );
    }
  }

  /**
   * Counts the number of user accounts that exist in the database.
   *
   * @return the amount of registered users
   */
  public long countUser() {
    return userRepository.count();
  }
}
