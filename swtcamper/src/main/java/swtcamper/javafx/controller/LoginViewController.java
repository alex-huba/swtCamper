package swtcamper.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.UserDTO;
import swtcamper.api.controller.UserController;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class LoginViewController {

    @Autowired
    private MainViewController mainViewController;

    @Autowired
    private UserController userController;

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

    private boolean validateInput(String username, String password) {
        // TODO: implement input validation
        return true;
    }

    public void handleRegister(ActionEvent actionEvent) {
        mainViewController.changeView("register");
    }
}
