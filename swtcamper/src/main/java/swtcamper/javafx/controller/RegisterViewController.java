package swtcamper.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.UserDTO;
import swtcamper.api.contract.UserRoleDTO;
import swtcamper.api.controller.UserController;
import swtcamper.backend.entities.userRole;
import swtcamper.backend.services.exceptions.GenericServiceException;

import java.util.ArrayList;

import static swtcamper.backend.entities.userRole.OPERATOR;
import static swtcamper.backend.entities.userRole.USER;

@Component
public class RegisterViewController {

    @Autowired
    private MainViewController mainViewController;

    @Autowired
    private UserController userController;

    @FXML
    public TextField inputLabel;

    @FXML
    public PasswordField passwordTf;

    @FXML
    public TextField emailTf;

    @FXML
    public TextField phoneTf;

    @FXML
    public TextField nameTf;

    @FXML
    public TextField surnameTf;

    @FXML
    public RadioButton operatorRb;

    @FXML
    public Button cancelBtn;

    @FXML
    public Button registerBtn;

    @FXML
    public void handleCancelBtn(ActionEvent actionEvent) {
        mainViewController.changeView("login");
    }

    public userRole userRoleRb;

    @FXML
    public void handleRegisterBtn(ActionEvent actionEvent) {
        boolean isInputValid = true;

        String username = inputLabel.getText();
        String password = passwordTf.getText();
        String email = emailTf.getText();
        String phone = phoneTf.getText();
        String name = nameTf.getText();
        String surname = surnameTf.getText();

        // UserDTO erstellen mit Attributen username, name etc.
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setPassword(password);
        userDTO.setEmail(email);
        userDTO.setPhone(phone);
        userDTO.setName(name);
        userDTO.setSurname(surname);

        if (username.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Fill in your username please!");
            alert.show();
        }
        if (password.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Fill in your password please!");
            alert.show();
        }
        if (password.length() <= 5){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Password is too short!");
            alert.show();
        }
        if (email.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Fill in your email please!");
            alert.show();
        }
        if (phone.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Fill in your phone please!");
            alert.show();
        }
        if (name.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Fill in your name please!");
            alert.show();
        }
        if (surname.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Fill in your surname please!");
            alert.show();
        }

        if(operatorRb.isSelected()){
            userDTO.setUserRole(OPERATOR);
        }else{
            userDTO.setUserRole(USER);
        }
        try{
//            userController.register(username, name, surname, email, phone, password, userRoleRb);
            userController.register(userDTO);
        } catch (GenericServiceException e){
            // TODO: Implement exception handling
            e.printStackTrace();
        }
        if (isInputValid){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Your data will be checked by operator shortly.");
            alert.setTitle("Thank you for signing up!");
            alert.setHeaderText("We've got your sign up request.");
            alert.show();
        }
        //

    }
    /*
    private void checkInput(
            String username,
            String password,
            String email,
            String phone,
            String name,
            String surname
    ) {
        UserDTO userDTO = new UserDTO();

        if(username.isEmpty() || username.length() < 5){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You can't create such username. Try again!");
            alert.show();
        } else {
            userDTO.setUsername(username);
        }

        if (password.isEmpty() || password.length() < 5){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You can't create such password. Try again!");
            alert.show();
        } else{
            userDTO.setPassword(password);
        }
    }
    */


}
