package swtcamper.javafx.controller;

import static swtcamper.backend.entities.userRole.OPERATOR;
import static swtcamper.backend.entities.userRole.USER;

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

import java.util.ArrayList;
import java.util.regex.Pattern;

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
  public CheckBox operatorCb;

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

  boolean isUsernameOk;
  boolean isPasswordOk;
  boolean isRepeatPasswordOk;
  boolean isEmailOk;
  boolean isPhoneOk;
  boolean isNameOk;
  boolean isSurnameOk;

  @FXML
  private void initialize() {
    usernameTf.setOnKeyTyped(this);
    passwordPf.setOnKeyTyped(this);
    repeatPasswordPf.setOnKeyTyped(this);
    emailTf.setOnKeyTyped(this);
    phoneTf.setOnKeyTyped(this);
    nameTf.setOnKeyTyped(this);
    surnameTf.setOnKeyTyped(this);
    registerBtn.setDisable(true);
    isUsernameOk = true;
    isPasswordOk = true;
    isRepeatPasswordOk = true;
    isEmailOk = true;
    isPhoneOk = true;
    isNameOk = true;
    isSurnameOk = false;
  }

  @FXML
  public void handleCancelBtn(ActionEvent actionEvent) {
    mainViewController.changeView("login");
  }

  @Override
  public void handle(KeyEvent event) {
    String source = getSourceOfEvent(event);

    // Validate input and disable login button if input of one field is invalid
    switch (source) {
      case "username":
        validateUsernameTf();
        controlRegisterBtn(isUsernameOk, isPasswordOk, isRepeatPasswordOk, isEmailOk, isPhoneOk, isNameOk, isSurnameOk);
        break;

      case "password":
        validatePasswordPf();
        controlRegisterBtn(isUsernameOk, isPasswordOk, isRepeatPasswordOk, isEmailOk, isPhoneOk, isNameOk, isSurnameOk);
        break;

      case "passwordRepeat":
        validateRepeatPasswordPf();
        controlRegisterBtn(isUsernameOk, isPasswordOk, isRepeatPasswordOk, isEmailOk, isPhoneOk, isNameOk, isSurnameOk);
        break;

      case "email":
        validateEmailTf();
        controlRegisterBtn(isUsernameOk, isPasswordOk, isRepeatPasswordOk, isEmailOk, isPhoneOk, isNameOk, isSurnameOk);
        break;

      case "phone":
        validatePhoneTf();
        controlRegisterBtn(isUsernameOk, isPasswordOk, isRepeatPasswordOk, isEmailOk, isPhoneOk, isNameOk, isSurnameOk);
        break;

      case "name":
        validateNameTf();
        controlRegisterBtn(isUsernameOk, isPasswordOk, isRepeatPasswordOk, isEmailOk, isPhoneOk, isNameOk, isSurnameOk);
        break;

      case "surname":
        validateSurnameTf();
        controlRegisterBtn(isUsernameOk, isPasswordOk, isRepeatPasswordOk, isEmailOk, isPhoneOk, isNameOk, isSurnameOk);
        break;
    }
  }

  private String getSourceOfEvent(KeyEvent event) {
    String source = "";
    source = event.getSource().toString();

    if(source.contains("usernameTf")) {
      source = "username";
    }
    if (source.contains("passwordPf")) {
      source = "password";
    }
    if (source.contains("repeatPasswordPf")) {
      source = "passwordRepeat";
    }
    if (source.contains("emailTf")) {
      source = "email";
    }
    if (source.contains("phoneTf")) {
      source = "phone";
    }
    if (source.contains("TextField[id=nameTf, styleClass=text-input text-field inputField]")) {
      source = "name";
    }
    if (source.contains("TextField[id=surnameTf, styleClass=text-input text-field inputField")) {
      source = "surname";
    }

    return source;
  }

  private void validateUsernameTf() {
    String input = usernameTf.getText();
    if (input.length() < 5 || !input.matches("^[a-zA-Z0-9.-]*")) {
      errorLabel.setText("Invalid username: 5 characters minimum");
      usernameTf.setBackground(
              new Background(
                      new BackgroundFill(
                              Color.LIGHTPINK,
                              CornerRadii.EMPTY,
                              Insets.EMPTY
                      )
              )
      );
      isUsernameOk = false;
    } else {
      errorLabel.setText("");
      usernameTf.setBackground(
              new Background(
                      new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)
              )
      );
      isUsernameOk = true;
    }
  }

  private void validatePasswordPf() {
    String input = passwordPf.getText();
    if (input.length() < 5 || !input.matches("^[a-zA-Z0-9.-]*")) {
      errorLabel.setText("Invalid password: 5 characters minimum");
      passwordPf.setBackground(
              new Background(
                      new BackgroundFill(
                              Color.LIGHTPINK,
                              CornerRadii.EMPTY,
                              Insets.EMPTY
                      )
              )
      );
      isPasswordOk = false;
    } else {
      errorLabel.setText("");
      passwordPf.setBackground(
              new Background(
                      new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)
              )
      );
      isPasswordOk = true;
    }
  }

  private void validateRepeatPasswordPf() {
    String input = repeatPasswordPf.getText();
    if (!input.equals(passwordPf.getText())) {
      errorLabel.setText("Passwords don't match");
      repeatPasswordPf.setBackground(
              new Background(
                      new BackgroundFill(
                              Color.LIGHTPINK,
                              CornerRadii.EMPTY,
                              Insets.EMPTY
                      )
              )
      );
      isRepeatPasswordOk = false;
    } else {
      errorLabel.setText("");
      repeatPasswordPf.setBackground(
              new Background(
                      new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)
              )
      );
      isRepeatPasswordOk = true;
    }
  }

  private void validateEmailTf() {
    String input = emailTf.getText();
    if (input.length() < 5 || !input.matches(String.valueOf(Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)))) {
      errorLabel.setText("Invalid email. Enter a correct email");
      emailTf.setBackground(
              new Background(
                      new BackgroundFill(
                              Color.LIGHTPINK,
                              CornerRadii.EMPTY,
                              Insets.EMPTY
                      )
              )
      );
      isEmailOk = false;
    } else {
      errorLabel.setText("");
      emailTf.setBackground(
              new Background(
                      new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)
              )
      );
      isEmailOk = true;
    }
  }

  private void validatePhoneTf() {
    String input = phoneTf.getText();
    if (input.length() < 5 || !input.matches("^[0-9-]*")) {
      errorLabel.setText("Invalid phone number. No letters allowed.");
      phoneTf.setBackground(
              new Background(
                      new BackgroundFill(
                              Color.LIGHTPINK,
                              CornerRadii.EMPTY,
                              Insets.EMPTY
                      )
              )
      );
      isPhoneOk = false;
    } else {
      errorLabel.setText("");
      phoneTf.setBackground(
              new Background(
                      new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)
              )
      );
      isPhoneOk = true;
    }
  }

  private void validateNameTf() {
    String input = nameTf.getText();
    if (input.length() < 3 || !input.matches("^[a-zA-Z]*")) {
      errorLabel.setText("Invalid name: 2 letters minimum");
      nameTf.setBackground(
              new Background(
                      new BackgroundFill(
                              Color.LIGHTPINK,
                              CornerRadii.EMPTY,
                              Insets.EMPTY
                      )
              )
      );
      isNameOk = false;
    } else {
      errorLabel.setText("");
      nameTf.setBackground(
              new Background(
                      new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)
              )
      );
      isNameOk = true;
    }
  }

  private void validateSurnameTf() {
    String input = surnameTf.getText();
    if (input.length() < 3 || !input.matches("^[a-zA-Z0-9.-]*")) {
      errorLabel.setText("Invalid surname: 2 letters minimum");
      surnameTf.setBackground(
              new Background(
                      new BackgroundFill(
                              Color.LIGHTPINK,
                              CornerRadii.EMPTY,
                              Insets.EMPTY
                      )
              )
      );
      isSurnameOk = false;
    } else {
      errorLabel.setText("");
      surnameTf.setBackground(
              new Background(
                      new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)
              )
      );
      isSurnameOk = true;
    }
  }

  private void controlRegisterBtn(boolean isUsernameOk,
                                  boolean isPasswordOk,
                                  boolean isRepeatPasswordOk,
                                  boolean isEmailOk,
                                  boolean isPhoneOk,
                                  boolean isNameOk,
                                  boolean isSurnameOk) {
    ArrayList<Boolean> isEverythingOk = new ArrayList<>();
    isEverythingOk.add(isUsernameOk);
    isEverythingOk.add(isPasswordOk);
    isEverythingOk.add(isRepeatPasswordOk);
    isEverythingOk.add(isEmailOk);
    isEverythingOk.add(isPhoneOk);
    isEverythingOk.add(isNameOk);
    isEverythingOk.add(isSurnameOk);

    if(!isEverythingOk.contains(false)) {
      registerBtn.setDisable(false);
    } else {
      registerBtn.setDisable(true);
    }
    isEverythingOk.clear();
  }

  @FXML
  public void handleRegisterBtn(ActionEvent actionEvent) {
    boolean isInputValid = true;

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
    if (operatorCb.isSelected()) {
      userDTO.setUserRole(OPERATOR);
    } else {
      userDTO.setUserRole(USER);
    }
    try {
      userController.register(userDTO);
      mainViewController.handleInformationMessage(
        "Your data will be checked by operator shortly."
      );
      //            Alert alert = new Alert(Alert.AlertType.INFORMATION);
      //            alert.setContentText("Your data will be checked by operator shortly.");
      //            alert.setTitle("Thank you for signing up!");
      //            alert.setHeaderText("We've got your sign up request.");
      //            alert.show();
      mainViewController.changeView("login");
    } catch (GenericServiceException e) {
      mainViewController.handleExceptionMessage(e.getMessage());
    }
    //        if (isInputValid){
    //        Alert
    //        }
  }
}
