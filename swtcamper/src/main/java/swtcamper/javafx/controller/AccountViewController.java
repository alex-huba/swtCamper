package swtcamper.javafx.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.LoggingMessageDTO;
import swtcamper.api.contract.UserReportDTO;
import swtcamper.api.contract.interfaces.ILoggingController;
import swtcamper.api.contract.interfaces.IUserController;
import swtcamper.api.contract.interfaces.IUserReportController;
import swtcamper.backend.entities.User;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class AccountViewController {

  private final SimpleBooleanProperty showingLogForSpecificUser = new SimpleBooleanProperty(
    false
  );

  @FXML
  private BorderPane accountRootPane;

  @FXML
  private ToolBar buttonToolbar;

  @FXML
  private Button showLogBtn;

  @FXML
  private Button blockBtn;

  @FXML
  private Button degradeBtn;

  @FXML
  private Button promoteBtn;

  @FXML
  private Button logoutBtn;

  @FXML
  private SplitPane operatorDashboard;

  @FXML
  private ListView<LoggingMessageDTO> logListView;

  @FXML
  private TableView<User> usersTableView;

  @FXML
  private TextField userFilterTextField;

  @FXML
  private Button resetUserFilterBtn;

  @FXML
  private Button resetLogBtn;

  @FXML
  private VBox reportVBox;

  @Autowired
  private IUserController userController;

  @Autowired
  private ILoggingController loggingController;

  @Autowired
  private IUserReportController userReportController;

  @Autowired
  private MainViewController mainViewController;

  private User selectedUser = null;

  @FXML
  public void initialize() {
    // disable all control buttons by default and enable needed ones later
    showLogBtn
      .disableProperty()
      .bind(usersTableView.getSelectionModel().selectedItemProperty().isNull());
    blockBtn.setDisable(true);
    degradeBtn.setDisable(true);
    promoteBtn.setDisable(true);

    // listen for selected list elements (= users) and get their roles
    usersTableView
      .getSelectionModel()
      .selectedItemProperty()
      .addListener((observable, oldValue, newValue) -> {
        if (newValue == null) {
          // if no element is selected (ctrl + click on already selected user)
          blockBtn.setDisable(true);
          degradeBtn.setDisable(true);
          promoteBtn.setDisable(true);

          showLogBtn.setText("Zeige Logs zu diesem Nutzer");
        } else {
          selectedUser = newValue;
          showLogBtn.setText("Zeige Logs zu: " + selectedUser.getUsername());

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

            // prevent new providers from being promoted/degraded
            if (newValue.isEnabled()) {
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
   * Loads a given list of loggingMessages into their ListView
   *
   * @param logList   list of LogMessages to load
   * @param ascending if true, order all log messages from oldest to newest, if false from newest to oldest
   */
  private void loadLogsIntoListView(
    List<LoggingMessageDTO> logList,
    boolean ascending
  ) {
    ObservableList<LoggingMessageDTO> modifiedLogList = FXCollections.observableArrayList(
      logList
    );
    if (!ascending) FXCollections.reverse(modifiedLogList);
    logListView.setItems(modifiedLogList);
  }

  /**
   * Initialization method for operators
   *
   * @param ascending if true, order all log messages from oldest to newest, if false from newest to oldest
   * @throws GenericServiceException
   */
  public void operatorInit(boolean ascending) throws GenericServiceException {
    buttonToolbar.getItems().clear();

    buttonToolbar
      .getItems()
      .addAll(
        resetLogBtn,
        showLogBtn,
        new Separator(Orientation.VERTICAL),
        blockBtn,
        degradeBtn,
        promoteBtn,
        new Separator(Orientation.VERTICAL),
        logoutBtn
      );

    operatorDashboard.setVisible(true);

    // fill in all log messages DESC
    loadLogsIntoListView(
      selectedUser == null
        ? loggingController.getAllLogMessages()
        : loggingController.getLogForUser(selectedUser),
      ascending
    );
    logListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    resetUserFilterBtn
      .visibleProperty()
      .bind(userFilterTextField.textProperty().isEmpty().not());

    resetLogBtn.disableProperty().bind(showingLogForSpecificUser.not());

    // generate table and fill in all users
    usersTableView.getColumns().clear();
    usersTableView.getItems().clear();

    FXCollections
      .observableArrayList(
        new Pair<>("Nickname", "username"),
        new Pair<>("Name", "name"),
        new Pair<>("Nachname", "surname"),
        new Pair<>("Email", "email"),
        new Pair<>("Befugnis", "userRole"),
        new Pair<>("akzeptiert?", "enabled"),
        new Pair<>("blockiert?", "locked")
      )
      .forEach(category -> {
        TableColumn<User, Object> tableColumn = new TableColumn<>(
          category.getKey()
        );
        tableColumn.setCellValueFactory(
          new PropertyValueFactory<>(category.getValue())
        );

        usersTableView.getColumns().add(tableColumn);
      });

    filterUsers();

    // user reports
    reportVBox.getChildren().clear();
    List<UserReportDTO> activeUserReports = userReportController
      .getAllUserReports()
      .stream()
      .filter(UserReportDTO::isActive)
      .collect(Collectors.toList());
    if (activeUserReports.isEmpty()) {
      Label thereAreNoActiveReportsLabel = new Label(
        "Im Moment liegen keine Beschwerden ??ber Nutzer vor"
      );
      thereAreNoActiveReportsLabel.setDisable(true);
      reportVBox.getChildren().add(thereAreNoActiveReportsLabel);
    } else {
      for (UserReportDTO userReportDTO : activeUserReports) {
        Label infoLabel = new Label(
          String.format(
            "Beschwerde von %s ??ber %s.",
            userReportDTO.getReporter().getUsername(),
            userReportDTO.getReportee().getUsername()
          )
        );
        infoLabel.setStyle("-fx-font-size: 20");
        Label reasonLabel = new Label(userReportDTO.getReportReason());

        Button acceptReportButton = new Button(
          String.format(
            "%s blockieren",
            userReportDTO.getReportee().getUsername()
          )
        );
        acceptReportButton.getStyleClass().add("bg-warning");
        acceptReportButton.setDisable(
          userReportDTO
            .getReportee()
            .getId()
            .equals(userController.getLoggedInUser().getId())
        );
        acceptReportButton.setOnAction(event -> {
          try {
            userReportController.accept(userReportDTO.getId());
            operatorInit(false);
          } catch (GenericServiceException ignore) {}
        });

        Button rejectReportButton = new Button("Beschwerde ignorieren");
        rejectReportButton.getStyleClass().add("bg-primary");
        rejectReportButton.setDisable(
          userReportDTO
            .getReportee()
            .getId()
            .equals(userController.getLoggedInUser().getId())
        );
        rejectReportButton.setOnAction(event -> {
          try {
            userReportController.reject(userReportDTO.getId());
            operatorInit(false);
          } catch (GenericServiceException ignore) {}
        });

        HBox buttonHBox = new HBox(acceptReportButton, rejectReportButton);
        buttonHBox.setSpacing(5);

        VBox userReportVBox = new VBox(
          infoLabel,
          reasonLabel,
          new Separator(),
          buttonHBox
        );
        userReportVBox.setStyle("-fx-background-color: white");
        userReportVBox.getStyleClass().addAll("border-dark", "radius-10", "p4");

        // add to view
        reportVBox.getChildren().add(userReportVBox);
      }
    }
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
    loadLogsIntoListView(loggingController.getLogForUser(selectedUser), false);
    showingLogForSpecificUser.set(true);
  }

  @FXML
  public void resetLogList() {
    loadLogsIntoListView(loggingController.getAllLogMessages(), false);
    showingLogForSpecificUser.set(false);
  }

  public void degradeUser() throws GenericServiceException {
    userController.degradeUserById(selectedUser.getId());
    operatorInit(false);
  }

  public void promoteUser() throws GenericServiceException {
    userController.promoteUserById(selectedUser.getId());
    operatorInit(false);
  }

  @FXML
  public void filterUsers() throws GenericServiceException {
    String searchText = userFilterTextField.getText().toLowerCase();
    if (searchText.isEmpty()) {
      usersTableView.setItems(
        FXCollections.observableArrayList(userController.getAllUsers())
      );
      return;
    }

    usersTableView.setItems(
      FXCollections.observableArrayList(
        userController
          .getAllUsers()
          .stream()
          .filter(user ->
            user.getUsername().toLowerCase().contains(searchText) ||
            user.getSurname().toLowerCase().contains(searchText) ||
            user.getName().toLowerCase().contains(searchText) ||
            user.getEmail().toLowerCase().contains(searchText)
          )
          .collect(Collectors.toList())
      )
    );
  }

  @FXML
  public void resetUserFilter() throws GenericServiceException {
    userFilterTextField.clear();
    filterUsers();
  }

  @FXML
  public void downloadLog(ActionEvent event) {
    Node source = (Node) event.getSource();
    Window window = source.getScene().getWindow();

    List<LoggingMessageDTO> logToDownload = logListView.getItems();

    FileChooser fileChooser = new FileChooser();

    fileChooser.setTitle("Verzeichnis w??hlen");
    fileChooser
      .getExtensionFilters()
      .addAll(
        new FileChooser.ExtensionFilter("LOG file (*.log)", "*.log"),
        new FileChooser.ExtensionFilter("TXT file (*.txt)", "*.txt")
      );
    fileChooser.setInitialFileName(
      String.format(
        "log_swtcamper_%s",
        LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
      )
    );

    File file = fileChooser.showSaveDialog(window);
    if (file != null) {
      if (file.exists()) file.delete();

      try (
        BufferedWriter writer = Files.newBufferedWriter(
          Path.of(file.getPath()),
          StandardCharsets.UTF_8,
          StandardOpenOption.CREATE
        )
      ) {
        for (LoggingMessageDTO logMsg : logToDownload) {
          writer.write(logMsg.toString());
          writer.newLine();
        }
      } catch (IOException e) {
        mainViewController.handleExceptionMessage(e.getMessage());
      }
    }
  }
}
