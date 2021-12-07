package swtcamper.javafx.controller;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import swtcamper.backend.services.exceptions.GenericServiceException;

public class ValidationHelper {

  public static void validateInputParameter(
    String rootDirectory,
    String fileExtension
  ) throws GenericServiceException {
    if (rootDirectory == null || rootDirectory.equals("")) {
      throw new GenericServiceException("Invalid directory name.");
    }
    if (fileExtension == null || fileExtension == "") {
      throw new GenericServiceException("Invalid fileExtension.");
    }
  }

  public static void validateInputLogin(
    KeyEvent event,
    TextField usernameTf,
    PasswordField passwordPf,
    Label errorLabel,
    Button loginButton
  ) {
    boolean isUsernameOk = false;
    boolean isPasswordOk = false;

    String source = "";
    source = event.getSource().toString();
    if (source.contains("usernameTf")) {
      source = "username";
    }
    if (source.contains("passwordPf")) {
      source = "password";
    }

    switch (source) {
      case "username":
        String inputUsername = usernameTf.getText();
        if (inputUsername.contains(" ") || inputUsername.length() < 5) {
          errorLabel.setText(
            "Invalid username: 5 characters minimum and no spaces"
          );
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
        break;
      case "password":
        String inputPassword = passwordPf.getText();
        if (inputPassword.contains(" ") || inputPassword.length() < 5) {
          errorLabel.setText(
            "Invalid password: 5 characters minimum and no spaces"
          );
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
        break;
    }
    if (isUsernameOk && isPasswordOk) {
      loginButton.setDisable(false);
    }
  }

  public static void validateInputRegistration(KeyEvent event) {
    // TODO: implement validation

    //        // TODO: Add comment
    //        String source = "";
    //        source = event.getSource().toString();
    //        if (source.contains("usernameTf")) {
    //            source = "username";
    //        }
    //        if (source.contains("passwordTf")) {
    //            source = "password";
    //        }
    //        if (source.contains("emailTf")) {
    //            source = "email";
    //        }
    //        if (source.contains("phoneTf")) {
    //            source = "phone";
    //        }
    //        if(source.contains("sur")) {
    //            source = "surnameTf";
    //        }
    //        if(source.contains("nameTf")) {
    //            if (source.equals("surnameTf")){
    //                return;
    //            } else {
    //                source = "name";
    //            }
    //        }
    //
    //
    //        // Validate input and disable login button if input of one field is invalid
    //        switch (source){
    //            case "username":
    //                String inputUsername = usernameTf.getText();
    //                if(inputUsername.length() < 5 || inputUsername.matches(" ")) {
    //                    errorLabel.setText("Invalid username: 5 characters minimum");
    //                    usernameTf.setBackground(new Background(new BackgroundFill(Color.LIGHTPINK, CornerRadii.EMPTY, Insets.EMPTY)));
    //                } else {
    //                    errorLabel.setText("");
    //                    usernameTf.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
    //                }
    //                break;
    //
    //            case "password":
    //                String inputPassword = passwordTf.getText();
    //                if(inputPassword.length() < 5 || inputPassword.matches(" ")) {
    //                    errorLabel.setText("Invalid password");
    //                    passwordTf.setBackground(new Background(new BackgroundFill(Color.LIGHTPINK, CornerRadii.EMPTY, Insets.EMPTY)));
    //                } else {
    //                    errorLabel.setText("");
    //                    passwordTf.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
    //                }
    //                break;
    //
    //            case "email":
    //                String inputEmail = emailTf.getText();
    //                if(inputEmail.length() < 5 || inputEmail.matches(" ")) {
    //                    errorLabel.setText("Invalid email");
    //                    emailTf.setBackground(new Background(new BackgroundFill(Color.LIGHTPINK, CornerRadii.EMPTY, Insets.EMPTY)));
    //                } else {
    //                    errorLabel.setText("");
    //                    emailTf.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
    //                }
    //                break;
    //
    //            case "phone":
    //                String inputPhone = phoneTf.getText();
    //                if (inputPhone.matches("[a-zA-Z]+")) {
    //                    errorLabel.setText("Invalid phone number");
    //                    phoneTf.setBackground(new Background(new BackgroundFill(Color.LIGHTPINK, CornerRadii.EMPTY, Insets.EMPTY)));
    //                } else {
    //                    errorLabel.setText("");
    //                    phoneTf.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
    //                }
    //                break;
    //
    //            case "name":
    //                String inputName = nameTf.getText();
    //                if (!inputName.matches("[a-zA-Z]+") || inputName.length() < 3) {
    //                    errorLabel.setText("Invalid name");
    //                    nameTf.setBackground(new Background(new BackgroundFill(Color.LIGHTPINK, CornerRadii.EMPTY, Insets.EMPTY)));
    //                } else {
    //                    errorLabel.setText("");
    //                    nameTf.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
    //                }
    //                break;
    //
    //            case "surnameTf":
    //                String inputSurname = surnameTf.getText();
    //                if (!inputSurname.matches("[a-zA-Z]+") || inputSurname.length() < 3) {
    //                    errorLabel.setText("Invalid surname");
    //                    surnameTf.setBackground(new Background(new BackgroundFill(Color.LIGHTPINK, CornerRadii.EMPTY, Insets.EMPTY)));
    //                } else {
    //                    errorLabel.setText("");
    //                    surnameTf.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
    //                }
    //                break;
    //
    //        }
  }
}
