package swtcamper.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginViewController {

  @Autowired
  private MainViewController mainViewController;

  @FXML
  public Button loginButton;

  @FXML
  public void loginAction(ActionEvent event) {
    if (loginButton.getText().equals("Login")) {
      loginButton.setText("Logout");
      mainViewController.loginTab.setText("Logout");

      mainViewController.offersTab.setDisable(false);
      mainViewController.placeOfferTab.setDisable(false);
    } else {
      loginButton.setText("Login");
      mainViewController.loginTab.setText("Login");

      mainViewController.offersTab.setDisable(true);
      mainViewController.placeOfferTab.setDisable(true);
    }
  }
}
