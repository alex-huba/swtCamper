package swtcamper.javafx.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.controller.LoggingController;
import swtcamper.api.controller.UserController;
import swtcamper.backend.entities.LoggingMessage;
import swtcamper.backend.entities.User;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class AccountViewController {

  @Autowired
  private UserController userController;

  @Autowired
  private LoggingController loggingController;

  @Autowired
  private MainViewController mainViewController;

  @FXML
  public BorderPane accountRootPane;

  @FXML
  public ToolBar buttonToolbar;

  @FXML
  public Button showLogBtn;

  @FXML
  public Button enableBtn;

  @FXML
  public Button blockBtn;

  @FXML
  public Button degradeBtn;

  @FXML
  public Button promoteBtn;

  @FXML
  public Button logoutBtn;

  @FXML
  public SplitPane operatorDashboard;

  @FXML
  public ListView<LoggingMessage> logListView;

  @FXML
  public ListView<User> usersListView;

  private User selectedUser = null;

  @FXML
  public void initialize() {
    showLogBtn
      .disableProperty()
      .bind(usersListView.getSelectionModel().selectedItemProperty().isNull());
    enableBtn.setDisable(true);
    blockBtn.setDisable(true);
    degradeBtn.setDisable(true);
    promoteBtn.setDisable(true);

    usersListView
      .getSelectionModel()
      .selectedItemProperty()
      .addListener((observable, oldValue, newValue) -> {
        if (newValue == null) {
          enableBtn.setDisable(true);
          blockBtn.setDisable(true);
          degradeBtn.setDisable(true);
          promoteBtn.setDisable(true);
        } else {
          selectedUser = newValue;

          if (
            !newValue.getId().equals(userController.getLoggedInUser().getId())
          ) {
            enableBtn.setDisable(false);
            enableBtn.setText(
              newValue.isEnabled() ? "Ignorieren" : "Akzeptieren"
            );
            blockBtn.setDisable(false);
            blockBtn.setText(newValue.isLocked() ? "Entblocken" : "Blocken");

            switch (newValue.getUserRole()) {
              case RENTER:
                degradeBtn.setDisable(true);
                promoteBtn.setDisable(false);
                break;
              case PROVIDER:
                degradeBtn.setDisable(false);
                promoteBtn.setDisable(false);
                break;
              case OPERATOR:
                degradeBtn.setDisable(false);
                promoteBtn.setDisable(true);
                break;
            }
          } else {
            enableBtn.setDisable(true);
            blockBtn.setDisable(true);
            degradeBtn.setDisable(true);
            promoteBtn.setDisable(true);
          }
        }
      });
  }

  public void operatorInit() throws GenericServiceException {
    if (!accountRootPane.getChildren().contains(operatorDashboard)) {
      buttonToolbar.getItems().removeIf(node -> true);

      Separator verticalSeperator = new Separator();
      verticalSeperator.setOrientation(Orientation.VERTICAL);

      buttonToolbar
        .getItems()
        .addAll(
          showLogBtn,
          enableBtn,
          blockBtn,
          degradeBtn,
          promoteBtn,
          verticalSeperator,
          logoutBtn
        );
      // TODO: passiert nicht wenn sich als erstes ein Nicht-Operator einloggt
      accountRootPane.getChildren().add(operatorDashboard);
    }

    logListView.setItems(
      FXCollections.observableArrayList(loggingController.getAllLogMessages())
    );
    usersListView.setItems(
      FXCollections.observableArrayList(userController.getAllUsers())
    );
  }

  public void normalUserInit() {
    if (accountRootPane.getChildren().contains(operatorDashboard)) {
      buttonToolbar.getItems().removeIf(node -> true);
      buttonToolbar.getItems().add(logoutBtn);
      accountRootPane.getChildren().remove(operatorDashboard);
    }
  }

  public void logout() throws GenericServiceException {
    mainViewController.logout();
  }

  public void showLogForUser() {
    if (showLogBtn.getText().equals("Zeige alle Logs")) {
      usersListView.getSelectionModel().select(null);
      logListView.setItems(
        FXCollections.observableArrayList(loggingController.getAllLogMessages())
      );
      showLogBtn.setText("Zeige Logs zu diesem Nutzer");
    } else {
      logListView.setItems(
        FXCollections.observableArrayList(
          loggingController.getLogForUser(selectedUser)
        )
      );
      showLogBtn.setText("Zeige alle Logs");
    }
  }

  public void enableUser() {
    // TODO
  }

  public void blockUser() {
    // TODO
  }

  public void degradeUser() {
    // TODO
  }

  public void promoteUser() {
    // TODO
  }
}
