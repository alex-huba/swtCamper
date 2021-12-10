package swtcamper.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
  public void initialize() {
    errorMessageLabel.setText("");

  }

  public void resetPassword(ActionEvent actionEvent) {
    try {
      UserDTO userDTO = new UserDTO();
      userDTO.setUsername(usernameTf.getText());
      userDTO.setEmail(emailTf.getText());
      userDTO.setPassword(passwordPf.getText());
      userController.resetPassword(userDTO);
    } catch (GenericServiceException e) {
      errorMessageLabel.setText(e.getMessage());
    }
  }

  public void cancelReset() {
    mainViewController.changeView("login");
  }
}
