package swtcamper.javafx.controller;

import static swtcamper.backend.entities.UserRole.OPERATOR;
import static swtcamper.backend.entities.UserRole.RENTER;

import java.util.ArrayList;
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
public class RegisterViewController implements EventHandler<KeyEvent> {

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private UserController userController;

  @FXML
  public TextField usernameTf;

  @FXML
  public PasswordField passwordPf;

  @FXML
  public PasswordField repeatPasswordPf;

  @FXML
  public TextField emailTf;

  @FXML
  public TextField phoneTf;

  @FXML
  public TextField nameTf;

  @FXML
  public TextField surnameTf;

  @FXML
  public CheckBox renterCb;

  @FXML
  public CheckBox providerCb;

  @FXML
  public Button cancelBtn;

  @FXML
  public Button registerBtn;

  @FXML
  public Label errorLabel;

  private BooleanProperty isUsernameOk;
  private BooleanProperty isPasswordOk;
  private BooleanProperty isRepeatPasswordOk;
  private BooleanProperty isEmailOk;
  private BooleanProperty isPhoneOk;
  private BooleanProperty isNameOk;
  private BooleanProperty isSurnameOk;

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
  private void initialize() {
    usernameTf.setOnKeyTyped(this);
    passwordPf.setOnKeyTyped(this);
    repeatPasswordPf.setOnKeyTyped(this);
    emailTf.setOnKeyTyped(this);
    phoneTf.setOnKeyTyped(this);
    nameTf.setOnKeyTyped(this);
    surnameTf.setOnKeyTyped(this);

    isUsernameOk = new SimpleBooleanProperty(false);
    isPasswordOk = new SimpleBooleanProperty(false);
    isRepeatPasswordOk = new SimpleBooleanProperty(false);
    isEmailOk = new SimpleBooleanProperty(false);
    isPhoneOk = new SimpleBooleanProperty(false);
    isNameOk = new SimpleBooleanProperty(false);
    isSurnameOk = new SimpleBooleanProperty(false);

    registerBtn
      .disableProperty()
      .bind(
        isUsernameOk
          .and(isPasswordOk)
          .and(isRepeatPasswordOk)
          .and(isEmailOk)
          .and(isPhoneOk)
          .and(isNameOk)
          .and(isSurnameOk)
          .and(renterCb.selectedProperty().or(providerCb.selectedProperty()))
          .not()
      );
  }

  @FXML
  public void handleCancelBtn(ActionEvent actionEvent) {
    mainViewController.changeView("login");
  }

  @Override
  public void handle(KeyEvent event) {
    // Validate input and disable login button if input of one field is invalid
    Object source = event.getSource();
    if (usernameTf.equals(source)) {
      try {
        validateUsernameTf();
      } catch (GenericServiceException e) {}
    } else if (passwordPf.equals(source)) validatePasswordPf(); else if (
      repeatPasswordPf.equals(source)
    ) validateRepeatPasswordPf(); else if (emailTf.equals(source)) {
      try {
        validateEmailTf();
      } catch (GenericServiceException e) {}
    } else if (phoneTf.equals(source)) validatePhoneTf(); else if (
      nameTf.equals(source)
    ) validateNameTf(); else if (surnameTf.equals(source)) validateSurnameTf();
  }

  private void validateUsernameTf() throws GenericServiceException {
    String input = usernameTf.getText();
    if (input.length() < 5 || !input.matches("^[a-zA-Z0-9.-]*")) {
      errorLabel.setText("Invalid username: 5 characters minimum");
      usernameTf.setBackground(errorBackground);
      isUsernameOk.setValue(false);
    } else if (!userController.isUsernameFree(new UserDTO(input, null))) {
      errorLabel.setText("Invalid username: username already taken");
      usernameTf.setBackground(errorBackground);
      isUsernameOk.setValue(false);
    } else {
      errorLabel.setText("");
      usernameTf.setBackground(successBackground);
      isUsernameOk.setValue(true);
    }
  }

  private void validatePasswordPf() {
    String input = passwordPf.getText();
    if (input.length() < 5 || !input.matches("^[a-zA-Z0-9.-]*")) {
      errorLabel.setText("Invalid password: 5 characters minimum");
      passwordPf.setBackground(errorBackground);
      isPasswordOk.setValue(false);
    } else {
      errorLabel.setText("");
      passwordPf.setBackground(successBackground);
      isPasswordOk.setValue(true);
    }
  }

  private void validateRepeatPasswordPf() {
    String input = repeatPasswordPf.getText();
    if (!input.equals(passwordPf.getText())) {
      errorLabel.setText("Passwords don't match");
      repeatPasswordPf.setBackground(errorBackground);
      isRepeatPasswordOk.setValue(false);
    } else {
      errorLabel.setText("");
      repeatPasswordPf.setBackground(successBackground);
      isRepeatPasswordOk.setValue(true);
    }
  }

  private void validateEmailTf() throws GenericServiceException {
    String input = emailTf.getText();

    UserDTO userWithEmail = new UserDTO();
    userWithEmail.setEmail(input);

    if (
      input.length() < 5 ||
      !input.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")
    ) {
      errorLabel.setText("Invalid email. Enter a correct email");
      emailTf.setBackground(errorBackground);
      isEmailOk.setValue(false);
    } else if (!userController.isEmailFree(userWithEmail)) {
      errorLabel.setText("Invalid email: email already taken");
      emailTf.setBackground(errorBackground);
      isEmailOk.setValue(false);
    } else {
      errorLabel.setText("");
      emailTf.setBackground(successBackground);
      isEmailOk.setValue(true);
    }
  }

  private void validatePhoneTf() {
    String input = phoneTf.getText();
    if (input.length() < 5 || !input.matches("^[0-9-]*")) {
      errorLabel.setText("Invalid phone number. No letters allowed.");
      phoneTf.setBackground(errorBackground);
      isPhoneOk.setValue(false);
    } else {
      errorLabel.setText("");
      phoneTf.setBackground(successBackground);
      isPhoneOk.setValue(true);
    }
  }

  private void validateNameTf() {
    String input = nameTf.getText();
    if (input.length() < 3 || !input.matches("^[a-zA-Z]*")) {
      errorLabel.setText("Invalid name: 2 letters minimum");
      nameTf.setBackground(errorBackground);
      isNameOk.setValue(false);
    } else {
      errorLabel.setText("");
      nameTf.setBackground(successBackground);
      isNameOk.setValue(true);
    }
  }

  private void validateSurnameTf() {
    String input = surnameTf.getText();
    if (input.length() < 3 || !input.matches("^[a-zA-Z0-9.-]*")) {
      errorLabel.setText("Invalid surname: 2 letters minimum");
      surnameTf.setBackground(errorBackground);
      isSurnameOk.setValue(false);
    } else {
      errorLabel.setText("");
      surnameTf.setBackground(successBackground);
      isSurnameOk.setValue(true);
    }
  }

  @FXML
  public void handleRegisterBtn(ActionEvent actionEvent) {
    String username = usernameTf.getText();
    String password = passwordPf.getText();
    String repeatPassword = repeatPasswordPf.getText();
    String email = emailTf.getText();
    String phone = phoneTf.getText();
    String name = nameTf.getText();
    String surname = surnameTf.getText();

    UserDTO userDTO = new UserDTO();
    userDTO.setUsername(username);
    userDTO.setPassword(password);
    userDTO.setEmail(email);
    userDTO.setPhone(phone);
    userDTO.setName(name);
    userDTO.setSurname(surname);

    // TODO: implement user roles (Provider and Renter instead of User)
    if (providerCb.isSelected()) {
      userDTO.setUserRole(OPERATOR);
    } else if (renterCb.isSelected()) {
      userDTO.setUserRole(RENTER);
    }
    try {
      userController.register(userDTO);
      mainViewController.handleInformationMessage(
        "Your data will be checked by operator shortly."
      );
      // Alert alert = new Alert(Alert.AlertType.INFORMATION);
      // alert.setContentText("Your data will be checked by operator shortly.");
      // alert.setTitle("Thank you for signing up!");
      // alert.setHeaderText("We've got your sign up request.");
      // alert.show();
      mainViewController.changeView("login");
    } catch (GenericServiceException e) {
      mainViewController.handleExceptionMessage(e.getMessage());
    }
    // if (isInputValid){
    // Alert
    // }
  }
}
