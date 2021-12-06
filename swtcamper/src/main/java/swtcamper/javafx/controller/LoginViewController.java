package swtcamper.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginViewController {

    @Autowired
    private MainViewController mainViewController;

    @FXML
    public Button loginButton;

    @FXML
    public Button registerButton;

    @FXML
    public TextField usernameTf;

    @FXML
    public PasswordField passwordPf;

    @FXML
    public void handleLogin(ActionEvent actionEvent) {
        String username =  usernameTf.getText();
        String password = passwordPf.getText();
        if (validateInput(username, password)) {
            mainViewController.changeView("myOffers");
        }
    }

    private boolean validateInput(String username, String password) {
        return true;
    }

    public void handleRegister(ActionEvent actionEvent) {
        mainViewController.changeView("register");
    }

    public void handleForgotPassword(ActionEvent actionEvent) {
        mainViewController.changeView("forgotPassword");
    }
}
