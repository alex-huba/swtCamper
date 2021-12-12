package swtcamper.backend.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swtcamper.api.contract.UserDTO;
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

  /**
   * Creates and stores a new user in the database with the provided username, name, surname, email, phone number and
   * password.
   * @param userDTO
  //   * @param username
  //   * @param name
  //   * @param surname
  //   * @param email
  //   * @param phone
  //   * @param password
  //   * @param userRole
  //   * @param locked
  //   * @param enabled
   * @return
   * @throws GenericServiceException if the username, name, surname, email, phone number or password is invalid
   */
  public User create(UserDTO userDTO) throws GenericServiceException {
    User user = new User();
    user.setUsername(userDTO.getUsername());
    user.setName(userDTO.getName());
    user.setSurname(userDTO.getSurname());
    user.setEmail(userDTO.getEmail());
    user.setPhone(userDTO.getPhone());
    user.setPassword(userDTO.getPassword());
    user.setUserRole(userDTO.getUserRole());
    user.setLocked(userDTO.isLocked());
    user.setEnabled(userDTO.isEnabled());
    return userRepository.save(user);
  }

  public void delete(User user) {
    // TODO: implement user deletion
  }

  public void update(User user) {
    // TODO: implement user update
  }

  public List<User> user() throws GenericServiceException {
    if (userRepository.findAll().isEmpty()) {
      throw new GenericServiceException(
        "No users found. User database is empty."
      );
    }
    return userRepository.findAll();
  }

  public boolean login(UserDTO userDTO) throws WrongPasswordException, UserDoesNotExistException {
    // Check if username and password are matching
    if (
      userRepository.existsByUsernameAndPassword(
        userDTO.getUsername(),
        userDTO.getPassword()
      )
    ) {
      // Username and password are matching
      return true;
    }
    // Check if either username or password exists to see if user typed one of them wrong
    if(userRepository.existsByUsername(userDTO.getUsername())){
      throw new WrongPasswordException("Wrong password. Please try again.");
    }
    throw new UserDoesNotExistException("Username doesn't exist.");
  }

  /**
   * Method to check if username is free
   * @param userDTO
   * @return
   * @throws GenericServiceException when the username is already taken
   */
  public boolean isUsernameFree(UserDTO userDTO)
    throws GenericServiceException {
    return !userRepository.existsByUsername(userDTO.getUsername());
  }

  public boolean isEmailFree(UserDTO userDTO) throws GenericServiceException {
    return !userRepository.existsByEmail(userDTO.getEmail());
  }

  public void lock(User user) {
    // TODO: implement user lock
  }

  public void enable(User user) {
    // TODO: implement user activation
  }

  public void resetPassword(UserDTO userDTO) throws GenericServiceException {
    if (
      userRepository.existsByUsernameAndEmail(
        userDTO.getUsername(),
        userDTO.getEmail()
      )
    ) {
      // Get user if it exists in database

      User user = userRepository.findByUsername(userDTO.getUsername()).get();

      user.setPassword(userDTO.getPassword());

      userRepository.save(user);
    } else {
      throw new GenericServiceException(
        "Couldn't change password. Username or password is not correct."
      );
    }
  }
}
