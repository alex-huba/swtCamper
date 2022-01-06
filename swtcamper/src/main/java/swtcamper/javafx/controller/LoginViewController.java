package swtcamper.javafx.controller;

import java.util.Optional;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.controller.UserController;
import swtcamper.backend.services.exceptions.GenericServiceException;
import swtcamper.backend.services.exceptions.UserDoesNotExistException;
import swtcamper.backend.services.exceptions.WrongPasswordException;

@Component
public class LoginViewController implements EventHandler<KeyEvent> {

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private UserController userController;

  @Autowired
  private RegisterViewController registerViewController;

  @Autowired
  private ResetPasswordViewController resetPasswordViewController;

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
      Alert alert = new Alert(
        Alert.AlertType.ERROR,
        "Klicken Sie OK um das Passwort zurückzusetzen"
      );
      alert.setTitle("Authentifizierung fehlgeschlagen!");
      alert.setHeaderText("Falsches Passwort. Bitte erneut eingeben.");
      Optional<ButtonType> result = alert.showAndWait();
      if (result.isPresent() && (result.get() == ButtonType.OK)) {
        mainViewController.changeView("forgotPassword");
        resetPasswordViewController.usernameTf.setText(username);
        resetPasswordViewController.validateUsernameTf();
      }
    } catch (UserDoesNotExistException e) {
      // Inform user that user account doesn't exist
      Alert alert = new Alert(
        Alert.AlertType.ERROR,
        "Klicke OK um einen neuen Account zu erstellen"
      );
      alert.setTitle("Authentifizierung fehlgeschlagen!");
      alert.setHeaderText("Es gibt keinen Account mit diesem Nutzernamen");
      Optional<ButtonType> result = alert.showAndWait();
      if (result.isPresent() && (result.get() == ButtonType.OK)) {
        mainViewController.changeView("register");
        registerViewController.usernameTf.setText(username);
        registerViewController.passwordPf.setText(password);
        registerViewController.validateUsernameTf();
        registerViewController.validatePasswordPf();
      }
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
    usernameTf.clear();
    passwordPf.clear();
  }

  @FXML
  public void handleEnterKey(KeyEvent event) throws GenericServiceException {
    if (event.getCode() == KeyCode.ENTER) handleLogin();
  }
}
