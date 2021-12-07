package swtcamper.javafx.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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

@Component
public class LoginViewController implements EventHandler<KeyEvent> {

    @Autowired
    private MainViewController mainViewController;

    @Autowired
    private UserController userController;

    @FXML
    public Button loginButton;

    @FXML
    public Button registerButton;

    @FXML
    public Button forgotPasswordButton;

    @FXML
    public TextField usernameTf;

    @FXML
    public PasswordField passwordPf;

    @FXML
    public Label errorLabel;

    boolean isUsernameOk = false;
    boolean isPasswordOk = false;

    @FXML
    public void initialize() {
        usernameTf.setOnKeyTyped(this);
        passwordPf.setOnKeyTyped(this);
        loginButton.setDisable(true);
    }

    @Override
    public void handle(KeyEvent event) {
        validateInput(event);
    }

    private void validateInput(KeyEvent event) {
        String source = "";
        source = event.getSource().toString();
        if (source.contains("usernameTf")) {
            source = "username";
        }
        if (source.contains("passwordPf")) {
            source = "password";
        }

        switch (source) {
            case "username":
                String inputUsername = usernameTf.getText();
                if (inputUsername.contains(" ") || inputUsername.length() < 5) {
                    errorLabel.setText("Invalid username: 5 characters minimum and no spaces");
                    usernameTf.setBackground(new Background(new BackgroundFill(Color.LIGHTPINK, CornerRadii.EMPTY, Insets.EMPTY)));
                    isUsernameOk = false;
                } else {
                    errorLabel.setText("");
                    usernameTf.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                    isUsernameOk = true;
                }
                break;

            case "password":
                String inputPassword = passwordPf.getText();
                if (inputPassword.contains(" ") || inputPassword.length() < 5) {
                    errorLabel.setText("Invalid password: 5 characters minimum and no spaces");
                    passwordPf.setBackground(new Background(new BackgroundFill(Color.LIGHTPINK, CornerRadii.EMPTY, Insets.EMPTY)));
                    isPasswordOk = false;
                } else {
                    errorLabel.setText("");
                    passwordPf.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                    isPasswordOk = true;
                }
                break;
        }
        if (isUsernameOk && isPasswordOk) {
            loginButton.setDisable(false);
        }
    }


    @FXML
    public void handleLogin(ActionEvent actionEvent) {

        // Get user input
        String username =  usernameTf.getText();
        String password = passwordPf.getText();

        // Validate user input

        // Create temporary userDTO to compare it with user database
        UserDTO userDTO = new UserDTO(username, password);

        // Try to login if user data matches data in database
        boolean userExists = false;

        try {
            userExists = userController.login(userDTO);
        } catch (GenericServiceException e){
            mainViewController.handleException(e);
            return;
        }

        if (userExists){
            mainViewController.changeView("myOffers");
        }
    }



    @FXML
    public void handleRegister(ActionEvent actionEvent) {
        mainViewController.changeView("register");
    }

    @FXML
    public void handleForgotPassword(ActionEvent actionEvent) {
        mainViewController.changeView("forgotPassword");
    }


}
