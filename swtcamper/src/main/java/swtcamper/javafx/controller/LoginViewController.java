package swtcamper.javafx.controller;

import java.util.Optional;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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

@Component
public class LoginViewController implements EventHandler<KeyEvent> {

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private UserController userController;

  @Autowired
  private RegisterViewController registerViewController;

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

  private final BooleanProperty isUsernameOk = new SimpleBooleanProperty(false);
  private final BooleanProperty isPasswordOk = new SimpleBooleanProperty(false);

  @FXML
  public void initialize() {
    usernameTf.setOnKeyTyped(this::validateInput);
    passwordPf.setOnKeyTyped(this::validateInput);

    loginButton.disableProperty().bind((isUsernameOk.and(isPasswordOk)).not());
  }

  @Override
  public void handle(KeyEvent event) {
    validateInput(event);
  }

  private void validateInput(KeyEvent event) {
    Object eventSource = event.getSource();
    if (eventSource.equals(usernameTf)) {
      String inputUsername = usernameTf.getText();
      if (inputUsername.contains(" ") || inputUsername.length() < 5) {
        errorLabel.setText(
          "Invalid username: 5 characters minimum and no spaces"
        );
        usernameTf.setBackground(
          new Background(
            new BackgroundFill(Color.LIGHTPINK, CornerRadii.EMPTY, Insets.EMPTY)
          )
        );
        isUsernameOk.setValue(false);
      } else {
        errorLabel.setText("");
        usernameTf.setBackground(
          new Background(
            new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)
          )
        );
        isUsernameOk.setValue(true);
      }
    } else if (eventSource.equals(passwordPf)) {
      String inputPassword = passwordPf.getText();
      if (inputPassword.contains(" ") || inputPassword.length() < 5) {
        errorLabel.setText(
          "Invalid password: 5 characters minimum and no spaces"
        );
        passwordPf.setBackground(
          new Background(
            new BackgroundFill(Color.LIGHTPINK, CornerRadii.EMPTY, Insets.EMPTY)
          )
        );
        isPasswordOk.setValue(false);
      } else {
        errorLabel.setText("");
        passwordPf.setBackground(
          new Background(
            new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)
          )
        );
        isPasswordOk.setValue(true);
      }
    }
  }

  @FXML
  public void handleLogin(ActionEvent actionEvent) {
    // Get user input
    String username = usernameTf.getText();
    String password = passwordPf.getText();

    // Create temporary userDTO to compare it with user database
    UserDTO userDTO = new UserDTO(username, password);

    // Try to login if user input matches data in database
    try {
      userController.login(userDTO);
      mainViewController.login();
    } catch (GenericServiceException e) {
      // Inform user that user doesn't exist in database and forward to registration view if user agrees
      Alert alert = new Alert(
        Alert.AlertType.CONFIRMATION,
        "Want to sign up instead? Click ok."
      );
      alert.setTitle("Authentication failed");
      alert.setHeaderText(e.getMessage());
      Optional<ButtonType> result = alert.showAndWait();
      if (result.isPresent() && (result.get() == ButtonType.OK)) {
        mainViewController.changeView("register");
        registerViewController.usernameTf.setText(username);
        registerViewController.passwordPf.setText(password);
      }
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

  public void resetInputfields() {
    usernameTf.clear();
    passwordPf.clear();
  }
}
