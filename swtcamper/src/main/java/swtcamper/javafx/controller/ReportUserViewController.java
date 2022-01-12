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

//    private User userToReport;

    public void initialize(User userToReport) {
        reportThisUserTextField.setText(userToReport.getUsername());
    }

    public void goBackToExcludeView() throws GenericServiceException {
        mainViewController.changeView("exclude");
    }

    public void sendReport() throws GenericServiceException {
        boolean userIsReal = false;
        for (User user : userController.getAllUsers()) {
            if (user.getUsername().equals(reportThisUserTextField.getText())) {
                userReportController.create(new UserReport(userController.getLoggedInUser(),user,reasonForReportTextArea.getText()));
                userIsReal = true;
                break;
            }
        }
        if(!userIsReal) mainViewController.handleInformationMessage("Diesen Nutzer gibt es nicht.");
    }

    public void abortReport() throws GenericServiceException {
        mainViewController.changeView("exclude");
    }

    public void checkUserName() {
//        try {
//            userToReport = userController.getUserByUsername(reportThisUserTextField.getText());
//            sendReportButton.setDisable(false);
//        } catch (GenericServiceException ignore) {
//            sendReportButton.setDisable(true);
//        }
    }
}
