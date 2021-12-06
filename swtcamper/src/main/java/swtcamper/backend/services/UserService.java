package swtcamper.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swtcamper.api.contract.UserDTO;
import swtcamper.backend.entities.User;
import swtcamper.backend.entities.userRole;
import swtcamper.backend.repositories.UserRepository;
import swtcamper.backend.services.exceptions.GenericServiceException;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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
  public User create(
          UserDTO userDTO
//          String username,
//          String name,
//          String surname,
//          String email,
//          String phone,
//          String password,
//          userRole userRole
//          boolean locked,
//          boolean enabled
          ) throws GenericServiceException {

//    validateUsername(username);
//    validateName(name);
//    validateSurname(surname);
//    validateEmail(email);
//    validatePhone(phone);
//    validatePassword(password);

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

//    user.setUsername(username);
//    user.setName(name);
//    user.setSurname(surname);
//    user.setEmail(email);
//    user.setPhone(phone);
//    user.setPassword(password);
//    user.setLocked(false);
//    user.setEnabled(true);
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

  public List<User> user() throws GenericServiceException {
    if (userRepository.findAll().isEmpty()){
      throw new GenericServiceException("No users found. User database is empty.");
    }
    return userRepository.findAll();
  }

  public boolean login(UserDTO userDTO) throws GenericServiceException{
    return userRepository.existsByUsernameAndPassword(userDTO.getUsername(), userDTO.getPassword());
  }

  public void changePassword(UserDTO userDTO) throws GenericServiceException{
    if(userRepository.existsByUsername(userDTO.getUsername())){

      // Get user if it exists in database
      Optional<User> userOptional = userRepository.findByUsername(userDTO.getUsername());
      User user = userOptional.get();

      // Change password
      user.setPassword(userDTO.getPassword());
      return;
    }
    throw new GenericServiceException("Couldn't change password. Please try again.");
  }

  public void lock(User user){
    // TODO: implement user lock
  }

  public void enable(User user){
    // TODO: implement user activation
  }

  public void checkIfUserExists(User user){
    // TODO: implement check if user already exists in database

  }
}
