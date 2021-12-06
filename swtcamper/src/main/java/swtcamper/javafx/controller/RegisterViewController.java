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
import swtcamper.backend.entities.userRole;
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
    public TextField passwordTf;

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
    public Label errorMessage;

    @FXML
    public void handleCancelBtn(ActionEvent actionEvent) {
        mainViewController.changeView("login");
    }

    public userRole userRoleRb;

    @FXML
    private void initialize(){
        usernameTf.setOnKeyTyped(this);
        passwordTf.setOnKeyTyped(this);
        emailTf.setOnKeyTyped(this);
        phoneTf.setOnKeyTyped(this);
        nameTf.setOnKeyTyped(this);
        surnameTf.setOnKeyTyped(this);
    }

    @Override
    public void handle(KeyEvent event) {
        String source = "";
        source = event.getSource().toString();
        if (source.contains("usernameTf")) {
            source = "username";
        }
        if (source.contains("passwordTf")) {
            source = "password";
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


        switch (source){
            case "username":
                String inputUsername = usernameTf.getText();
                if(inputUsername.length() < 5 || inputUsername.matches(" ")) {
                    errorMessage.setText("Invalid username: 5 characters minimum");
                    usernameTf.setBackground(new Background(new BackgroundFill(Color.LIGHTPINK, CornerRadii.EMPTY, Insets.EMPTY)));
                } else {
                    errorMessage.setText("");
                    usernameTf.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                }
                break;

            case "password":
                String inputPassword = passwordTf.getText();
                if(inputPassword.length() < 5 || inputPassword.matches(" ")) {
                    errorMessage.setText("Invalid password");
                    passwordTf.setBackground(new Background(new BackgroundFill(Color.LIGHTPINK, CornerRadii.EMPTY, Insets.EMPTY)));
                } else {
                    errorMessage.setText("");
                    passwordTf.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                }
                break;

            case "email":
                String inputEmail = emailTf.getText();
                if(inputEmail.length() < 5 || inputEmail.matches(" ")) {
                    errorMessage.setText("Invalid email");
                    emailTf.setBackground(new Background(new BackgroundFill(Color.LIGHTPINK, CornerRadii.EMPTY, Insets.EMPTY)));
                } else {
                    errorMessage.setText("");
                    emailTf.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                }
                break;

            case "phone":
                String inputPhone = phoneTf.getText();
                if (inputPhone.matches("[a-zA-Z]+")) {
                    errorMessage.setText("Invalid phone number");
                    phoneTf.setBackground(new Background(new BackgroundFill(Color.LIGHTPINK, CornerRadii.EMPTY, Insets.EMPTY)));
                } else {
                    errorMessage.setText("");
                    phoneTf.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                }
                break;

            case "name":
                String inputName = nameTf.getText();
                if (!inputName.matches("[a-zA-Z]+") || inputName.length() < 3) {
                    errorMessage.setText("Invalid name");
                    nameTf.setBackground(new Background(new BackgroundFill(Color.LIGHTPINK, CornerRadii.EMPTY, Insets.EMPTY)));
                } else {
                    errorMessage.setText("");
                    nameTf.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                }
                break;

            case "surnameTf":
                String inputSurname = surnameTf.getText();
                if (!inputSurname.matches("[a-zA-Z]+") || inputSurname.length() < 3) {
                    errorMessage.setText("Invalid surname");
                    surnameTf.setBackground(new Background(new BackgroundFill(Color.LIGHTPINK, CornerRadii.EMPTY, Insets.EMPTY)));
                } else {
                    errorMessage.setText("");
                    surnameTf.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                }
                break;

        }
    }



    @FXML
    public void handleRegisterBtn(ActionEvent actionEvent) {
        boolean isInputValid = true;

        String username = usernameTf.getText();
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
            e.printStackTrace();
        }
        if (isInputValid){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Your data will be checked by operator shortly.");
            alert.setTitle("Thank you for signing up!");
            alert.setHeaderText("We've got your sign up request.");
            alert.show();
        }
    }

}
