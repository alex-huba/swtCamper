package swtcamper.javafx.controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.controller.UserController;
import swtcamper.api.controller.UserReportController;
import swtcamper.backend.entities.User;
import swtcamper.backend.entities.UserReport;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class ReportUserViewController {

  @Autowired
  private UserReportController userReportController;

  @Autowired
  private UserController userController;

  @Autowired
  private MainViewController mainViewController;

  @FXML
  public TextField reportThisUserTextField;

  @FXML
  public TextArea reasonForReportTextArea;

  @FXML
  public Button sendReportButton;

  public void initialize(User userToReport) {
    reportThisUserTextField.setText(userToReport.getUsername());
  }

  public void goBackToExcludeView() throws GenericServiceException {
    mainViewController.changeView("exclude");
  }

  public void sendReport() throws GenericServiceException {
    boolean userIsReal = false;
    // check if user is valid
    for (User user : userController.getAllUsers()) {
      if (user.getUsername().equals(reportThisUserTextField.getText())) {
        userIsReal = true;
        // create new report
        userReportController.create(
          new UserReport(
            userController.getLoggedInUser(),
            user,
            reasonForReportTextArea.getText()
          )
        );
        mainViewController.handleInformationMessage(
          "Beschwerde gesendet. Ein Operator wird sie in Kürze bearbeiten."
        );

        // reset fields and go to home-view
        resetInputFields();
        mainViewController.changeView("home");
        break;
      }
    }
    if (!userIsReal) mainViewController.handleInformationMessage(
      "Diesen Nutzer gibt es nicht."
    );
  }

  public void abortReport() throws GenericServiceException {
    resetInputFields();
    mainViewController.changeView("exclude");
  }

  private void resetInputFields() {
    reportThisUserTextField.clear();
    reasonForReportTextArea.clear();
  }
}
