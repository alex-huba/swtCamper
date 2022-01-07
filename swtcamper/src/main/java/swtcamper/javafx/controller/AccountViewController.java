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
    blockBtn.setDisable(true);
    degradeBtn.setDisable(true);
    promoteBtn.setDisable(true);

    usersListView
      .getSelectionModel()
      .selectedItemProperty()
      .addListener((observable, oldValue, newValue) -> {
        if (newValue == null) {
          blockBtn.setDisable(true);
          degradeBtn.setDisable(true);
          promoteBtn.setDisable(true);
        } else {
          selectedUser = newValue;

          if (
            !newValue.getId().equals(userController.getLoggedInUser().getId())
          ) {
            //            if (!newValue.isLocked()) enableBtn.setDisable(false);
            //            enableBtn.setText(
            //              newValue.isEnabled() ? "Ignorieren" : "Akzeptieren"
            //            );
            //            enableBtn.setOnAction(event -> {
            //              if (newValue.isEnabled()) {
            //                userController.ignoreUserById(selectedUser.getId());
            //              } else {
            //                userController.enableUserById(selectedUser.getId());
            //              }
            //              try {
            //                operatorInit();
            //              } catch (GenericServiceException ignore) {}
            //            });

            blockBtn.setDisable(false);
            blockBtn.setText(
              newValue.isLocked() ? "Entblocken" : "Global Blockieren"
            );
            blockBtn.setOnAction(event -> {
              if (newValue.isLocked()) {
                userController.unblockUserById(selectedUser.getId());
              } else {
                userController.blockUserById(selectedUser.getId());
              }
              try {
                operatorInit();
              } catch (GenericServiceException ignore) {}
            });

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
            blockBtn.setDisable(true);
            degradeBtn.setDisable(true);
            promoteBtn.setDisable(true);
          }
        }
      });
  }

  public void operatorInit() throws GenericServiceException {
    buttonToolbar.getItems().clear();

    Separator verticalSeparator = new Separator();
    verticalSeparator.setOrientation(Orientation.VERTICAL);

    buttonToolbar
      .getItems()
      .addAll(
        showLogBtn,
        blockBtn,
        degradeBtn,
        promoteBtn,
        verticalSeparator,
        logoutBtn
      );

    operatorDashboard.setVisible(true);

    logListView.setItems(
      FXCollections.observableArrayList(loggingController.getAllLogMessages())
    );
    usersListView.setItems(
      FXCollections.observableArrayList(userController.getAllUsers())
    );
  }

  public void normalUserInit() {
    buttonToolbar.getItems().clear();
    buttonToolbar.getItems().add(logoutBtn);
    operatorDashboard.setVisible(false);
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

  public void degradeUser() throws GenericServiceException {
    userController.degradeUserById(selectedUser.getId());
    operatorInit();
  }

  public void promoteUser() throws GenericServiceException {
    userController.promoteUserById(selectedUser.getId());
    operatorInit();
  }
}
