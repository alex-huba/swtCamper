package swtcamper.javafx.controller;

import static swtcamper.backend.entities.UserRole.*;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.controller.UserController;
import swtcamper.backend.entities.UserRole;
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

  @FXML
  public VBox rootElement;

  private BooleanProperty isUsernameOk;
  private BooleanProperty isPasswordOk;
  private BooleanProperty isRepeatPasswordOk;
  private BooleanProperty isEmailOk;
  private BooleanProperty isPhoneOk;
  private BooleanProperty isNameOk;
  private BooleanProperty isSurnameOk;

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

    renterCb
      .selectedProperty()
      .addListener((observable, oldValue, newValue) -> {
        if (newValue.equals(false)) providerCb.setSelected(false);
      });
    providerCb
      .selectedProperty()
      .addListener((observable, oldValue, newValue) -> {
        if (newValue.equals(true)) renterCb.setSelected(true);
      });

    // Disable register button until every field contains valid input
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
    //    rootElement.setVgrow()
  }

  @FXML
  public void handleCancelBtn() throws GenericServiceException {
    mainViewController.changeView("login");
  }

  @Override
  public void handle(KeyEvent event) {
    // Validate input and disable login button until every field contains valid input
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

  private void validateTrue(Node element) {
    element.setStyle("-fx-background-color: #198754; -fx-text-fill: #FFFFFF");
  }

  private void validateNeutral(Node element) {
    element.setStyle("-fx-background-color: white; -fx-text-fill: #000000");
  }

  private void validateFalse(Node element) {
    element.setStyle("-fx-background-color: #dc3545; -fx-text-fill: #FFFFFF");
  }

  public void validateUsernameTf() throws GenericServiceException {
    String input = usernameTf.getText();
    if (input.length() < 5 || !input.matches("^[a-zA-Z0-9.-]*")) {
      errorLabel.setText(
        "Ungültiger Nutzername: 5 Zeichen mindestens und keine Leerzeichen"
      );
      validateFalse(usernameTf);
      isUsernameOk.setValue(false);
    } else if (!userController.isUsernameFree(input)) {
      errorLabel.setText(
        "Ungültiger Nutzername: Nutzername ist bereits vergeben"
      );
      validateFalse(usernameTf);
      isUsernameOk.setValue(false);
    } else {
      errorLabel.clear();
      validateTrue(usernameTf);
      isUsernameOk.setValue(true);
    }
  }

  public void validatePasswordPf() {
    String input = passwordPf.getText();
    if (input.length() < 5 || !input.matches("^[a-zA-Z0-9.-]*")) {
      errorLabel.setText(
        "Ungültiges Passwort: 5 Zeichen mindestens und keine Leerzeichen"
      );
      validateFalse(passwordPf);
      isPasswordOk.setValue(false);
    } else {
      errorLabel.clear();
      validateTrue(passwordPf);
      isPasswordOk.setValue(true);
    }
  }

  private void validateRepeatPasswordPf() {
    String input = repeatPasswordPf.getText();
    if (!input.equals(passwordPf.getText())) {
      errorLabel.setText("Passwörter stimmen nicht überein");
      validateFalse(repeatPasswordPf);
      isRepeatPasswordOk.setValue(false);
    } else {
      errorLabel.clear();
      validateTrue(repeatPasswordPf);
      isRepeatPasswordOk.setValue(true);
    }
  }

  private void validateEmailTf() throws GenericServiceException {
    String input = emailTf.getText();
    if (
      input.length() < 5 ||
      !input.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")
    ) {
      errorLabel.setText("Ungültiges Email");
      validateFalse(emailTf);
      isEmailOk.setValue(false);
    } else if (!userController.isEmailFree(input)) {
      errorLabel.setText("Ungültiges Email: Email ist bereits vergeben");
      validateFalse(emailTf);
      isEmailOk.setValue(false);
    } else {
      errorLabel.clear();
      validateTrue(emailTf);
      isEmailOk.setValue(true);
    }
  }

  private void validatePhoneTf() {
    String input = phoneTf.getText();
    if (input.length() < 9 || !input.matches("^[0-9-]*")) {
      errorLabel.setText("Ungültige Telefonnummer. Bitte keine Buchstaben.");
      validateFalse(phoneTf);
      isPhoneOk.setValue(false);
    } else {
      errorLabel.clear();
      validateTrue(phoneTf);
      isPhoneOk.setValue(true);
    }
  }

  private void validateNameTf() {
    String input = nameTf.getText();
    if (input.length() < 3 || !input.matches("^[a-zA-Z]*")) {
      errorLabel.setText("Ungültiger Name: 2 Buchstaben mindestens");
      validateFalse(nameTf);
      isNameOk.setValue(false);
    } else {
      errorLabel.clear();
      validateTrue(nameTf);
      isNameOk.setValue(true);
    }
  }

  private void validateSurnameTf() {
    String input = surnameTf.getText();
    if (input.length() < 3 || !input.matches("^[a-zA-Z0-9.-]*")) {
      errorLabel.setText("Ungültiger Nachname: 2 Buchstaben mindestens");
      validateFalse(surnameTf);
      isSurnameOk.setValue(false);
    } else {
      errorLabel.clear();
      validateTrue(surnameTf);
      isSurnameOk.setValue(true);
    }
  }

  public void resetInputFields() {
    usernameTf.clear();
    passwordPf.clear();
    repeatPasswordPf.clear();
    emailTf.clear();
    phoneTf.clear();
    nameTf.clear();
    surnameTf.clear();
    providerCb.setSelected(false);
    renterCb.setSelected(false);

    FXCollections
      .observableArrayList(
        usernameTf,
        passwordPf,
        repeatPasswordPf,
        emailTf,
        phoneTf,
        nameTf,
        surnameTf
      )
      .forEach(this::validateNeutral);

    errorLabel.clear();
  }

  @FXML
  public void handleRegisterBtn() {
    String username = usernameTf.getText();
    String password = passwordPf.getText();
    String email = emailTf.getText();
    String phone = phoneTf.getText();
    String name = nameTf.getText();
    String surname = surnameTf.getText();
    UserRole userRole;
    boolean enabled;

    if (userController.countUser() == 0) {
      userRole = OPERATOR;
      enabled = true;
    } else if (providerCb.isSelected()) {
      userRole = PROVIDER;
      enabled = false;
    } else {
      userRole = RENTER;
      enabled = true;
    }

    try {
      userController.register(
        username,
        password,
        email,
        phone,
        name,
        surname,
        userRole,
        enabled
      );
      // User registered as provider
      if (providerCb.isSelected()) {
        mainViewController.handleInformationMessage(
          String.format(
            "Neuer Benutzer '%s' erstellt. \nMelden Sie sich an, um fortzufahren. \nIhre Daten werden in Kürze von einem Operator geprüft.",
            username
          )
        );
        // User registered as renter
      } else {
        mainViewController.handleInformationMessage(
          String.format(
            "Neuer Benutzer '%s' erstellt. Melden Sie sich an, um fortzufahren.",
            username
          )
        );
      }
      mainViewController.changeView("login");
    } catch (GenericServiceException e) {
      mainViewController.handleExceptionMessage(e.getMessage());
    }
  }
}
