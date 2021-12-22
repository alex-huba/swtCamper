package swtcamper.javafx.controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
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
public class ResetPasswordViewController {

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private UserController userController;

  @FXML
  public Label errorMessageLabel;

  @FXML
  public TextField usernameTf;

  @FXML
  public TextField emailTf;

  @FXML
  public PasswordField passwordPf;

  @FXML
  public PasswordField repeatPasswordPf;

  @FXML
  public Button resetButton;

  SimpleBooleanProperty isUsernameOk;
  SimpleBooleanProperty isEmailOk;
  SimpleBooleanProperty isPasswordOk;
  SimpleBooleanProperty isRepeatPasswordOk;

  @FXML
  public void initialize() {
    errorMessageLabel.setText("");

    isUsernameOk = new SimpleBooleanProperty(false);
    isEmailOk = new SimpleBooleanProperty(false);
    isPasswordOk = new SimpleBooleanProperty(false);
    isRepeatPasswordOk = new SimpleBooleanProperty(false);

    // Disable reset button until every field contains valid input
    resetButton
      .disableProperty()
      .bind(
        isUsernameOk
          .and(isEmailOk)
          .and(isPasswordOk)
          .and(isRepeatPasswordOk)
          .not()
      );
  }

  public void resetPassword() {
    String username = usernameTf.getText();
    String email = emailTf.getText();
    String password = passwordPf.getText();

    UserDTO userDTO = new UserDTO();
    userDTO.setUsername(username);
    userDTO.setPassword(password);
    userDTO.setEmail(email);

    try {
      userController.resetPassword(userDTO);

      Alert successAlert = new Alert(
        Alert.AlertType.INFORMATION,
        "Passwort wurde erfolgreich geändert"
      );
      mainViewController.changeView("login");
    } catch (GenericServiceException e) {
      errorMessageLabel.setText(e.getMessage());
    }
  }

  public void cancelReset() throws GenericServiceException {
    mainViewController.changeView("login");
  }

  private void validateTrue(Node element) {
    element.setStyle("-fx-background-color: #198754; -fx-text-fill: #FFFFFF");
  }

  private void validateFalse(Node element) {
    element.setStyle("-fx-background-color: #dc3545; -fx-text-fill: #FFFFFF");
  }

  @FXML
  public void validateUsernameTf() {
    String input = usernameTf.getText();
    if (input.length() == 0) {
      errorMessageLabel.setText("Ungültiger Nutzername");
      validateFalse(usernameTf);
      isUsernameOk.setValue(false);
    } else {
      errorMessageLabel.setText("");
      validateTrue(usernameTf);
      isUsernameOk.setValue(true);
    }
  }

  @FXML
  private void validateEmailTf() {
    String input = emailTf.getText();
    if (input.length() == 0) {
      errorMessageLabel.setText("Ungültige Email");
      validateFalse(emailTf);
      isEmailOk.setValue(false);
    } else {
      errorMessageLabel.setText("");
      validateTrue(emailTf);
      isEmailOk.setValue(true);
    }
  }

  @FXML
  public void validatePasswordPf() {
    String input = passwordPf.getText();
    if (input.length() < 5 || !input.matches("^[a-zA-Z0-9.-]*")) {
      errorMessageLabel.setText(
        "Ungültiges Passwort: 5 Zeichen mindestens und keine Leerzeichen"
      );
      validateFalse(passwordPf);
      isPasswordOk.setValue(false);
    } else {
      errorMessageLabel.setText("");
      validateTrue(passwordPf);
      isPasswordOk.setValue(true);
    }
  }

  @FXML
  private void validateRepeatPasswordPf() {
    String input = repeatPasswordPf.getText();
    if (!input.equals(passwordPf.getText())) {
      errorMessageLabel.setText("Passwörter stimmen nicht überein");
      validateFalse(repeatPasswordPf);
      isRepeatPasswordOk.setValue(false);
    } else {
      errorMessageLabel.setText("");
      validateTrue(repeatPasswordPf);
      isRepeatPasswordOk.setValue(true);
    }
  }

  public void resetInputFields() {
    usernameTf.clear();
    emailTf.clear();
    passwordPf.clear();
    repeatPasswordPf.clear();
  }
}
