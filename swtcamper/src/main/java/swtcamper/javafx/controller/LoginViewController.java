package swtcamper.javafx.controller;

import java.util.Optional;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.controller.UserController;
import swtcamper.backend.services.exceptions.GenericServiceException;
import swtcamper.backend.services.exceptions.UserDoesNotExistException;
import swtcamper.backend.services.exceptions.WrongPasswordException;

@Component
public class LoginViewController implements EventHandler<KeyEvent> {

  private final BooleanProperty isUsernameOk = new SimpleBooleanProperty(false);
  private final BooleanProperty isPasswordOk = new SimpleBooleanProperty(false);

  @FXML
  public Button loginButton;

  @FXML
  public Hyperlink registerButton;

  @FXML
  public Hyperlink forgotPasswordButton;

  @FXML
  public TextField usernameTf;

  @FXML
  public PasswordField passwordPf;

  @FXML
  public Label errorLabel;

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private UserController userController;

  @Autowired
  private RegisterViewController registerViewController;

  @Autowired
  private ResetPasswordViewController resetPasswordViewController;

  private int loginAttempts;

  @FXML
  public void initialize() {
    loginAttempts = 0;

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

    // Validate username
    if (eventSource.equals(usernameTf)) {
      String inputUsername = usernameTf.getText();
      if (inputUsername.contains(" ") || inputUsername.length() < 5) {
        errorLabel.setText(
          "Ungültiger Nutzername: mindestens 5 Zeichen und keine Leerzeichen"
        );
        isUsernameOk.setValue(false);
      } else {
        errorLabel.setText("");
        isUsernameOk.setValue(true);
      }
      // Validate password
    } else if (eventSource.equals(passwordPf)) {
      String inputPassword = passwordPf.getText();
      if (inputPassword.contains(" ") || inputPassword.length() < 5) {
        errorLabel.setText(
          "Ungültiges Passwort: mindestens 5 Zeichen und keine Leerzeichen"
        );
        isPasswordOk.setValue(false);
      } else {
        errorLabel.setText("");
        isPasswordOk.setValue(true);
      }
    }
  }

  @FXML
  public void handleLogin() throws GenericServiceException {
    // Get user input
    String username = usernameTf.getText();
    String password = passwordPf.getText();

    // Try to login if user input matches data in database
    try {
      mainViewController.login(userController.login(username, password));
    } catch (WrongPasswordException e) {
      // Inform user that entered password is wrong
      errorLabel.setText("Falsches Passwort, bitte versuchen Sie es erneut.");

      // ask user to change password after 3 wrong tries
      if (loginAttempts == 2) {
        Alert alert = new Alert(
          Alert.AlertType.ERROR,
          "Sie haben Ihr Passwort 3x hintereinander falsch eingegeben.\nKlicken Sie OK um es zurückzusetzen."
        );
        alert.setTitle("Authentifizierung fehlgeschlagen!");
        alert.setHeaderText("Falsches Passwort. Zurücksetzen?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && (result.get() == ButtonType.OK)) {
          mainViewController.changeView("forgotPassword");
          resetPasswordViewController.usernameTf.setText(username);
          resetPasswordViewController.validateUsernameTf();
        }
      } else {
        loginAttempts++;
      }
    } catch (UserDoesNotExistException e) {
      errorLabel.setText(
        "Es gibt keinen Account mit diesem Nutzernamen; bitte registrieren Sie sich zuerst wenn sie noch keinen Nutzeraccount haben."
      );
    }
  }

  @FXML
  public void handleRegister() throws GenericServiceException {
    mainViewController.changeView("register");
  }

  @FXML
  public void handleForgotPassword() throws GenericServiceException {
    mainViewController.changeView("forgotPassword");
  }

  public void resetInputFields() {
    loginAttempts = 0;

    usernameTf.clear();
    passwordPf.clear();

    errorLabel.setText("");
  }

  @FXML
  public void handleEnterKey(KeyEvent event) throws GenericServiceException {
    if (event.getCode() == KeyCode.ENTER) handleLogin();
  }
}
