package swtcamper.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swtcamper.backend.entities.User;
import swtcamper.backend.entities.userRole;
import swtcamper.backend.repositories.UserRepository;
import swtcamper.backend.services.exceptions.GenericServiceException;

import java.util.List;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  /**
   * Creates and stores a new user in the database with the provided username, name, surname, email, phone number and
   * password.
   * @param username
   * @param name
   * @param surname
   * @param email
   * @param phone
   * @param password
   * @param userRole
//   * @param locked
//   * @param enabled
   * @return
   * @throws GenericServiceException if the username, name, surname, email, phone number or password is invalid
   */
  public User create(
          String username,
          String name,
          String surname,
          String email,
          String phone,
          String password,
          userRole userRole
//          boolean locked,
//          boolean enabled
          ) throws GenericServiceException {
    validateUsername(username);
    validateName(name);
    validateSurname(surname);
    validateEmail(email);
    validatePhone(phone);
    validatePassword(password);

    User user = new User();
    user.setUsername(username);
    user.setName(name);
    user.setSurname(surname);
    user.setEmail(email);
    user.setPhone(phone);
    user.setPassword(password);
    user.setLocked(false);
    user.setEnabled(true);
    return userRepository.save(user);
  }

  private void validateUsername(String username) {
    // TODO: validate username
  }

  private void validateName(String name) {
    // TODO: validate name
  }

  private void validateSurname(String surname) {
    // TODO: validate surname
  }

  private void validateEmail(String email) {
    // TODO: validate email
  }

  private void validatePhone(String phone) {
    // TODO: validate phone
  }

  private void validatePassword(String password) {
    // TODO: validate password
  }


  public void delete(User user){
    // TODO: implement user deletion
  }

  public void update(User user){
    // TODO: implement user update
  }

  public List<User> user(){
    // TODO: implement user listing
    return null;
  }

  public void lock(User user){
    // TODO: implement user lock
  }

  public void enable(User user){
    // TODO: implement user activation
  }

  public void checkIfUserAlreadyExists(){
    // TODO: implement check if user already exists in database
  }
}
