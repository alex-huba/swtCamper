package swtcamper.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.IUserController;
import swtcamper.api.contract.UserDTO;
import swtcamper.backend.entities.User;
import swtcamper.backend.entities.userRole;
import swtcamper.backend.services.UserService;
import swtcamper.backend.services.exceptions.GenericServiceException;

import java.util.LinkedList;
import java.util.List;

@Component
public class UserController implements IUserController {

  @Autowired
  UserService userService;

  @Autowired
  ModelMapper modelMapper;

  // (1) in RegisterView werden Daten eingegeben, RegisterView kommuniziert dann mit RegisterViewController
  // (2) RegisterViewController kommuniziert mit UserController (HIER!) und ruft create() Methode auf
  // (3) UserController ruft UserService über modelmapper auf und gibt UserDTO an
  // userService.create() repräsentiert User (und nicht DTO!)
//  public UserDTO create(
//
//  ) {
//    // User user = userService.create();
//    // return modelMapper.userToUserDTO(user);
////    return modelMapper.userToUserDTO(userService.create());
//  }


  public UserDTO register(UserDTO userDTO) throws GenericServiceException {
//          String username,
//          String name,
//          String surname,
//          String email,
//          String phone,
//          String password,
//          userRole userRole
    try {
//    return modelMapper.userToUserDTO(userService.create(username,name,surname,email,phone,password,userRole));
        return modelMapper.userToUserDTO(userService.create(userDTO));
    } catch (GenericServiceException e){
      // TODO: Check exception handling
      throw new GenericServiceException("Could not register user.");
    }
  }

  public void login(
          String username,
          String password
  ) throws GenericServiceException {
    boolean loginSuccessfull = false;

    try {


      // Get list of all users in database
      List<User> allUser = new LinkedList<>();
      allUser = userService.user();

      User user;

      // Search for user in database
      for (User searchedUser : allUser){
        if(searchedUser.getUsername() == username){
          user = searchedUser;
          if(user.getPassword() == password){
            loginSuccessfull = true;
          } else {
            throw new GenericServiceException("Wrong password.");
          }
        } else {
          throw new GenericServiceException("Username " + username + " not found. Please register.");
        }
      }

      // Login successfull
      // TODO: implement successfull login
      // View-Wechsel?
    } catch (GenericServiceException e){
      // TODO: Check exception handling
      throw new GenericServiceException("No users found. Please register.");
    }
  }
}