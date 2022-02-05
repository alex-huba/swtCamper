package swtcamper.javafx.controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.interfaces.IUserController;
import swtcamper.api.controller.UserController;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class ResetPasswordViewController {

  @FXML
  private Label errorMessageLabel;

  @FXML
  public TextField usernameTf;

  @FXML
  private TextField emailTf;

  @FXML
  private PasswordField passwordPf;

  @FXML
  private PasswordField repeatPasswordPf;

  @FXML
  private Button resetButton;

  private SimpleBooleanProperty isUsernameOk;
  private SimpleBooleanProperty isEmailOk;
  private SimpleBooleanProperty isPasswordOk;
  private SimpleBooleanProperty isRepeatPasswordOk;

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private IUserController userController;

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

    try {
      userController.resetPassword(username, email, password);

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
    element.setStyle("-fx-background-color: #1987547f;");
  }

  private void validateNeutral(Node element) {
    element.setStyle("-fx-background-color: white; -fx-text-fill: #000000");
  }

  private void validateFalse(Node element) {
    element.setStyle("-fx-background-color: #dc35457f;");
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
    FXCollections
      .observableArrayList(usernameTf, emailTf, passwordPf, repeatPasswordPf)
      .forEach(textField -> {
        textField.clear();
        validateNeutral(textField);
      });
  }
}
