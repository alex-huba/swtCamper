package swtcamper.javafx.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.UserDTO;
import swtcamper.api.controller.UserController;
import swtcamper.backend.services.exceptions.GenericServiceException;


import static swtcamper.backend.entities.userRole.OPERATOR;
import static swtcamper.backend.entities.userRole.USER;

@Component
public class RegisterViewController implements EventHandler<KeyEvent> {

    @Autowired
    private MainViewController mainViewController;

    @Autowired
    private UserController userController;

    @FXML
    public TextField usernameTf;

    @FXML
    public PasswordField passwordTf;

    @FXML
    public PasswordField repeatPasswordTf;

    @FXML
    public TextField emailTf;

    @FXML
    public TextField phoneTf;

    @FXML
    public TextField nameTf;

    @FXML
    public TextField surnameTf;

    @FXML
    public CheckBox operatorRb;

    @FXML
    public Button cancelBtn;

    @FXML
    public Button registerBtn;

    @FXML
    public Label errorLabel;

    @FXML
    public void handleCancelBtn(ActionEvent actionEvent) {
        mainViewController.changeView("login");
    }

    @FXML
    private void initialize(){
        usernameTf.setOnKeyTyped(this);
        passwordTf.setOnKeyTyped(this);
        emailTf.setOnKeyTyped(this);
        phoneTf.setOnKeyTyped(this);
        nameTf.setOnKeyTyped(this);
        surnameTf.setOnKeyTyped(this);
        registerBtn.setDisable(true);
    }

    @Override
    public void handle(KeyEvent event) {

        // TODO: Add comment
        String source = "";
        source = event.getSource().toString();
        if (source.contains("usernameTf")) {
            source = "username";
        }
        if (source.contains("passwordTf")) {
            source = "password";
        }
        if (source.contains("repeatPasswordTf")) {
            source = "repeatPassword";
        }

        if (source.contains("emailTf")) {
            source = "email";
        }
        if (source.contains("phoneTf")) {
            source = "phone";
        }
        if(source.contains("sur")) {
            source = "surnameTf";
        }
        if(source.contains("nameTf")) {
            if (source.equals("surnameTf")){
                return;
            } else {
                source = "name";
            }
        }

        boolean isUsernameOk = false;
        boolean isPasswordOk = false;
        boolean isRepeatPasswordOk = false;
        boolean isEmailOk = false;
        boolean isPhoneOk = false;
        boolean isNameOk = false;
        boolean isSurnameOk = false;


        // Validate input and disable login button if input of one field is invalid
        switch (source){
            case "username":
                String inputUsername = usernameTf.getText();
                if(inputUsername.length() < 5 || inputUsername.matches(" ")) {
                    errorLabel.setText("Invalid username: 5 characters minimum");
                    usernameTf.setBackground(new Background(new BackgroundFill(Color.LIGHTPINK, CornerRadii.EMPTY, Insets.EMPTY)));
                    isUsernameOk = false;
                } else {
                    errorLabel.setText("");
                    usernameTf.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                    isUsernameOk = true;
                }
                break;

            case "password":
                String inputPassword = passwordTf.getText();
                if(inputPassword.length() < 5 || inputPassword.matches(" ")) {
                    errorLabel.setText("Invalid password: 5 characters minimum");
                    passwordTf.setBackground(new Background(new BackgroundFill(Color.LIGHTPINK, CornerRadii.EMPTY, Insets.EMPTY)));
                    isPasswordOk = false;
                } else {
                    errorLabel.setText("");
                    passwordTf.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                    isPasswordOk = true;
                }
                break;

            case "repeatedPassword":
                String inputRepeatPassword = passwordTf.getText();
                if(inputRepeatPassword.length() < 5 || inputRepeatPassword.matches(" ")) {
                    errorLabel.setText("Invalid password: 5 characters minimum");
                    passwordTf.setBackground(new Background(new BackgroundFill(Color.LIGHTPINK, CornerRadii.EMPTY, Insets.EMPTY)));
                    isRepeatPasswordOk = false;
                }
                if (!inputRepeatPassword.equals(passwordTf.getText())) {
                    errorLabel.setText("Passwords are not the same!");
                    passwordTf.setBackground(new Background(new BackgroundFill(Color.LIGHTPINK, CornerRadii.EMPTY, Insets.EMPTY)));
                    isRepeatPasswordOk = false;
                } else {
                    errorLabel.setText("");
                    passwordTf.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                    isPasswordOk = true;
                }
                break;

            case "email":
                String inputEmail = emailTf.getText();
                if(inputEmail.length() < 5 || inputEmail.matches(" ")) {
                    errorLabel.setText("Invalid email");
                    emailTf.setBackground(new Background(new BackgroundFill(Color.LIGHTPINK, CornerRadii.EMPTY, Insets.EMPTY)));
                    isEmailOk = false;
                } else {
                    errorLabel.setText("");
                    emailTf.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                    isEmailOk = true;
                }
                break;

            case "phone":
                String inputPhone = phoneTf.getText();
                if (inputPhone.matches("[a-zA-Z]+")) {
                    errorLabel.setText("Invalid phone number");
                    phoneTf.setBackground(new Background(new BackgroundFill(Color.LIGHTPINK, CornerRadii.EMPTY, Insets.EMPTY)));
                    isPhoneOk = false;
                } else {
                    errorLabel.setText("");
                    phoneTf.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                    isPhoneOk = true;
                }
                break;

            case "name":
                String inputName = nameTf.getText();
                if (!inputName.matches("[a-zA-Z]+") || inputName.length() < 3) {
                    errorLabel.setText("Invalid name");
                    nameTf.setBackground(new Background(new BackgroundFill(Color.LIGHTPINK, CornerRadii.EMPTY, Insets.EMPTY)));
                    isNameOk = false;
                } else {
                    errorLabel.setText("");
                    nameTf.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                    isNameOk = true;
                }
                break;

            case "surnameTf":
                String inputSurname = surnameTf.getText();
                if (!inputSurname.matches("[a-zA-Z]+") || inputSurname.length() < 3) {
                    errorLabel.setText("Invalid surname");
                    surnameTf.setBackground(new Background(new BackgroundFill(Color.LIGHTPINK, CornerRadii.EMPTY, Insets.EMPTY)));
                    isSurnameOk = false;
                } else {
                    errorLabel.setText("");
                    surnameTf.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                    isSurnameOk = true;
                }
                break;
        }
        if (isUsernameOk && isPasswordOk && isRepeatPasswordOk && isEmailOk && isPhoneOk && isNameOk && isSurnameOk) {
            registerBtn.setDisable(false);
        }
    }



    @FXML
    public void handleRegisterBtn(ActionEvent actionEvent) {
        boolean isInputValid = true;

        String username = usernameTf.getText();
        String password = passwordTf.getText();
        String repeatPassword = repeatPasswordTf.getText();
        String email = emailTf.getText();
        String phone = phoneTf.getText();
        String name = nameTf.getText();
        String surname = surnameTf.getText();

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setPassword(password);
        userDTO.setEmail(email);
        userDTO.setPhone(phone);
        userDTO.setName(name);
        userDTO.setSurname(surname);

        // TODO: implement handleInformationMessage
//        if (username.isEmpty()){
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setContentText("Fill in your username please!");
//            alert.show();
//        }
//        if (password.isEmpty()){
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setContentText("Fill in your password please!");
//            alert.show();
//        }
//        if(repeatPassword.isEmpty()){
//            mainViewController.handleInformationMessage("Please repeat your password!");
//        }
//        if(!repeatPassword.equals(password)){
//            mainViewController.handleInformationMessage("Passwords are not the same!");
//        }
//
//        if (password.length() <= 5){
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setContentText("Password is too short!");
//            alert.show();
//        }
//        if (email.isEmpty()){
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setContentText("Fill in your email please!");
//            alert.show();
//        }
//        if (phone.isEmpty()){
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setContentText("Fill in your phone please!");
//            alert.show();
//        }
//        if (name.isEmpty()){
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setContentText("Fill in your name please!");
//            alert.show();
//        }
//        if (surname.isEmpty()){
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setContentText("Fill in your surname please!");
//            alert.show();
//        }


        if(operatorRb.isSelected()){
            userDTO.setUserRole(OPERATOR);
        }else{
            userDTO.setUserRole(USER);
        }
        try{
            userController.register(userDTO);
            mainViewController.handleInformationMessage("Your data will be checked by operator shortly.");
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setContentText("Your data will be checked by operator shortly.");
//            alert.setTitle("Thank you for signing up!");
//            alert.setHeaderText("We've got your sign up request.");
//            alert.show();
            mainViewController.changeView("login");
        } catch (GenericServiceException e){
            // TODO: implement exception handling
            e.printStackTrace();
        }
//        if (isInputValid){
//
//        }
    }

}
