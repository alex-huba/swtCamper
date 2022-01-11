package swtcamper.javafx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.LoggingMessageDTO;
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
  public ListView<LoggingMessageDTO> logListView;

  @FXML
  public ListView<User> usersListView;

  private User selectedUser = null;

  @FXML
  public void initialize() {
    // disable all control buttons by default and enable needed ones later
    showLogBtn
      .disableProperty()
      .bind(usersListView.getSelectionModel().selectedItemProperty().isNull());
    blockBtn.setDisable(true);
    degradeBtn.setDisable(true);
    promoteBtn.setDisable(true);

    // listen for selected list elements (= users) and get their roles
    usersListView
      .getSelectionModel()
      .selectedItemProperty()
      .addListener((observable, oldValue, newValue) -> {
        if (newValue == null) {
          // if no element is selected (ctrl + click on already selected user)
          blockBtn.setDisable(true);
          degradeBtn.setDisable(true);
          promoteBtn.setDisable(true);
        } else {
          selectedUser = newValue;

          if (
            // an operator can only (un)block or change the UserRole of other users
            !newValue.getId().equals(userController.getLoggedInUser().getId())
          ) {
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
                // reload UI
                operatorInit(false);
              } catch (GenericServiceException ignore) {}
            });

            // evaluate if selected user can be promoted/degraded any further
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

  /**
   * Initialization method for operators
   *
   * @param ascending if true, order all log messages from oldest to newest, if false from newest to oldest
   * @throws GenericServiceException
   */
  public void operatorInit(boolean ascending) throws GenericServiceException {
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

    // fill in all log messages DESC
    ObservableList<LoggingMessageDTO> logList = FXCollections.observableArrayList(
      loggingController.getAllLogMessages()
    );
    if(!ascending) FXCollections.reverse(logList);
    logListView.setItems(logList);

    // fill in all users
    usersListView.setItems(
      FXCollections.observableArrayList(userController.getAllUsers())
    );
  }

  /**
   * Initialization method for non-Operators
   */
  public void normalUserInit() {
    buttonToolbar.getItems().clear();
    buttonToolbar.getItems().add(logoutBtn);
    operatorDashboard.setVisible(false);
  }

  public void logout() throws GenericServiceException {
    mainViewController.logout();
  }

  /**
   * Filters all log messages that include the selected user
   */
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
    operatorInit(false);
  }

  public void promoteUser() throws GenericServiceException {
    userController.promoteUserById(selectedUser.getId());
    operatorInit(false);
  }
}
