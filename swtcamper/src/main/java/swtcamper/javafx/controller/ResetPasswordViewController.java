package swtcamper.javafx.controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
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

  private Background errorBackground = new Background(
    new BackgroundFill(Color.LIGHTPINK, CornerRadii.EMPTY, Insets.EMPTY)
  );

  private Background neutralBackground = new Background(
    new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)
  );

  private Background successBackground = new Background(
    new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)
  );

  @FXML
  public void initialize() {
    errorMessageLabel.setText("");

    isUsernameOk = new SimpleBooleanProperty(false);
    isEmailOk = new SimpleBooleanProperty(false);
    isPasswordOk = new SimpleBooleanProperty(false);
    isRepeatPasswordOk = new SimpleBooleanProperty(false);

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

  public void resetPassword(ActionEvent actionEvent) {
    try {
      UserDTO userDTO = new UserDTO();
      userDTO.setUsername(usernameTf.getText());
      userDTO.setEmail(emailTf.getText());
      userDTO.setPassword(passwordPf.getText());
      userController.resetPassword(userDTO);

      Alert successAlert = new Alert(
        Alert.AlertType.INFORMATION,
        "Changed password successfully"
      );
      mainViewController.changeView("login");
    } catch (GenericServiceException e) {
      errorMessageLabel.setText(e.getMessage());
    }
  }

  public void cancelReset() {
    mainViewController.changeView("login");
  }

  @FXML
  public void validateUsernameTf() {
    String input = usernameTf.getText();
    if (input.length() == 0) {
      errorMessageLabel.setText("Invalid username");
      usernameTf.setBackground(errorBackground);
      isUsernameOk.setValue(false);
    } else {
      errorMessageLabel.setText("");
      usernameTf.setBackground(neutralBackground);
      isUsernameOk.setValue(true);
    }
  }

  @FXML
  private void validateEmailTf() {
    String input = usernameTf.getText();
    if (input.length() == 0) {
      errorMessageLabel.setText("Invalid email");
      usernameTf.setBackground(errorBackground);
      isEmailOk.setValue(false);
    } else {
      errorMessageLabel.setText("");
      usernameTf.setBackground(neutralBackground);
      isEmailOk.setValue(true);
    }
  }

  @FXML
  public void validatePasswordPf() {
    String input = passwordPf.getText();
    if (input.length() < 5 || !input.matches("^[a-zA-Z0-9.-]*")) {
      errorMessageLabel.setText("Invalid password: 5 characters minimum");
      passwordPf.setBackground(errorBackground);
      isPasswordOk.setValue(false);
    } else {
      errorMessageLabel.setText("");
      passwordPf.setBackground(successBackground);
      isPasswordOk.setValue(true);
    }
  }

  @FXML
  private void validateRepeatPasswordPf() {
    String input = repeatPasswordPf.getText();
    if (!input.equals(passwordPf.getText())) {
      errorMessageLabel.setText("Passwords don't match");
      repeatPasswordPf.setBackground(errorBackground);
      isRepeatPasswordOk.setValue(false);
    } else {
      errorMessageLabel.setText("");
      repeatPasswordPf.setBackground(successBackground);
      isRepeatPasswordOk.setValue(true);
    }
  }
}
