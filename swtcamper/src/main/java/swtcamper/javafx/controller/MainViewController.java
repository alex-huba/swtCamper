package swtcamper.javafx.controller;

import java.util.LinkedList;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.UserDTO;
import swtcamper.api.contract.UserReportDTO;
import swtcamper.api.contract.interfaces.IBookingController;
import swtcamper.api.contract.interfaces.IUserController;
import swtcamper.api.contract.interfaces.IUserReportController;
import swtcamper.backend.entities.Booking;
import swtcamper.backend.entities.User;
import swtcamper.backend.entities.UserRole;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class MainViewController {

  /**
   * Quick Settings
   */
  public final boolean startNavigationHidden = true;
  public final String startPageAfterLogin = "home";

  @FXML
  private Label globalHeaderLabel;

  @FXML
  private Button globalBackBtn;

  @FXML
  private AnchorPane mainStage;

  @FXML
  private Node homeViewBox;

  @FXML
  private Node placeOfferViewBox;

  @FXML
  private Pane offerViewBox;

  @FXML
  private Pane activeOffersViewBox;

  @FXML
  private Pane dealHistoryViewBox;

  @FXML
  private Pane excludeRenterViewBox;

  @FXML
  private Pane approveDealViewBox;

  @FXML
  private Pane myBookingsViewBox;

  @FXML
  private Pane loginViewBox;

  @FXML
  private Pane accountViewBox;

  @FXML
  private Pane registerViewBox;

  @FXML
  private Pane forgotPasswordViewBox;

  @FXML
  private Pane reportUserViewBox;

  @FXML
  private Pane faqViewBox;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private IUserController userController;

  @Autowired
  private IBookingController bookingController;

  @Autowired
  private IUserReportController userReportController;

  @Autowired
  private MyOffersViewController myOffersViewController;

  @Autowired
  private NavigationViewController navigationViewController;

  @Autowired
  private ModifyOfferViewController modifyOfferViewController;

  @Autowired
  private RentingViewController rentingViewController;

  @Autowired
  private ExcludeRenterViewController excludeRenterViewController;

  @Autowired
  private MyBookingsViewController myBookingsViewController;

  @Autowired
  private RegisterViewController registerViewController;

  @Autowired
  private AccountViewController accountViewController;

  @Autowired
  private LoginViewController loginViewController;

  @Autowired
  private ResetPasswordViewController resetPasswordViewController;

  @Autowired
  private ApproveNewProvidersViewController approveNewProvidersViewController;

  @Autowired
  private DealHistoryViewController dealHistoryViewController;

  private User latestLoggedInStatus = null;
  private String latestView = null;
  private final SimpleBooleanProperty updateHappening = new SimpleBooleanProperty(
    false
  );
  private final LinkedList<String> historyList = new LinkedList<>();
  // needed in order to make the back button work (see goBack())
  private boolean createHistory;

  @FXML
  private void initialize() throws GenericServiceException {
    createHistory = true;
    changeView("home");
  }

  public void reloadData() {
    try {
      myOffersViewController.reloadData();
    } catch (GenericServiceException e) {
      handleException(e);
    }
  }

  public void clearView() {
    mainStage.getChildren().removeIf(node -> node instanceof Pane);
  }

  @FXML
  public void goBack() throws GenericServiceException {
    // button is invisible anyway at this point but just be safe...
    if (
      historyList.size() <= 1 ||
      userController.getLoggedInUser() != null &&
      userController.getLoggedInUser().isLocked()
    ) return;
    if (!historyList.isEmpty() && historyList.getLast().equals("home")) {
      rentingViewController.resetFilter();
    }
    historyList.removeLast();
    // set boolean false in order to not overwrite the last index directly again
    createHistory = false;
    changeView(historyList.getLast());
  }

  @Scheduled(fixedDelay = 1000)
  private void listenForDataBaseChanges() throws GenericServiceException {
    User loggedInUser = userController.getLoggedInUser();
    if (loggedInUser == null) return;

    // check for new user reports
    if (
      loggedInUser.getUserRole().equals(UserRole.OPERATOR) &&
      userReportController
        .getAllUserReports()
        .stream()
        .anyMatch(UserReportDTO::isActive)
    ) {
      navigationViewController.showAccountNotification();
    } else {
      navigationViewController.hideAccountNotification();
    }

    // check for new booking requests
    if (
      !bookingController.getBookingsForUser(loggedInUser).isEmpty() &&
      !bookingController
        .getBookingsForUser(loggedInUser)
        .stream()
        .allMatch(Booking::isActive)
    ) {
      navigationViewController.showBookingNotification();
    } else {
      navigationViewController.hideBookingNotification();
    }

    // check for new providers that need to be enabled
    if (
      loggedInUser.getUserRole().equals(UserRole.OPERATOR) &&
      userController.getAllUsers().stream().anyMatch(user -> !user.isEnabled())
    ) {
      navigationViewController.showApproveNotification();
    } else {
      navigationViewController.hideApproveNotification();
    }

    if (latestLoggedInStatus != null && latestView != null) {
      // get the latest update for the logged-in user to check if there were made any changes
      User checkUser = userController.getUserById(loggedInUser.getId());
      if (!updateHappening.get()) {
        userController.setLoggedInUser(checkUser);

        // user-role has changed
        if (
          !checkUser.getUserRole().equals(latestLoggedInStatus.getUserRole())
        ) {
          updateHappening.set(true);
          Platform.runLater(() -> {
            try {
              handleInformationMessage(
                "Deine Rolle hat sich ge??ndert zu " +
                checkUser.getUserRole() +
                "!\nDie Oberfl??che wird sich aktualisieren"
              );
              login(modelMapper.userToUserDTO(checkUser), "home");
              updateHappening.set(false);
            } catch (GenericServiceException ignore) {}
          });
          // user got enabled
        } else if (checkUser.isEnabled() && !latestLoggedInStatus.isEnabled()) {
          updateHappening.set(true);
          Platform.runLater(() -> {
            try {
              handleInformationMessage(
                "Du wurdest akzeptiert und kannst jetzt Anzeigen erstellen!\nDie Oberfl??che wird sich aktualisieren"
              );
              login(modelMapper.userToUserDTO(checkUser), latestView);
              updateHappening.set(false);
            } catch (GenericServiceException ignore) {}
          });
          // user got locked globally
        } else if (checkUser.isLocked() && !latestLoggedInStatus.isLocked()) {
          updateHappening.set(true);
          Platform.runLater(() -> {
            try {
              handleInformationMessage(
                "Du wurdest gesperrt und kannst nicht mehr mit anderen Nutzern interagieren!\nDie Oberfl??che wird sich aktualisieren"
              );
              login(modelMapper.userToUserDTO(checkUser), "home");
              updateHappening.set(false);
            } catch (GenericServiceException ignore) {}
          });
          // user got unlocked from global lock
        } else if (!checkUser.isLocked() && latestLoggedInStatus.isLocked()) {
          updateHappening.set(true);
          Platform.runLater(() -> {
            try {
              handleInformationMessage(
                "Du wurdest entsperrt und kannst wieder mit anderen Nutzern interagieren!\nDie Oberfl??che wird sich aktualisieren"
              );
              login(modelMapper.userToUserDTO(checkUser), latestView);
              updateHappening.set(false);
            } catch (GenericServiceException ignore) {}
          });
        }
      }
    }
  }

  public void changeView(String switchTo) throws GenericServiceException {
    latestView = switchTo;

    // add new page to historyList if createHistory says so, if not turn it true again
    if (createHistory) {
      // only add new page if it is not the last one as well
      if (historyList.isEmpty() || !historyList.getLast().equals(switchTo)) {
        if (!historyList.isEmpty() && historyList.getLast().equals("home")) {
          rentingViewController.resetFilter();
        }
        historyList.add(switchTo);
      }
    } else {
      createHistory = true;
    }
    // hide back button if user cannot go back any further
    globalBackBtn.setVisible(historyList.size() > 1);

    clearView();
    switch (switchTo) {
      case "home":
        mainStage.getChildren().add(homeViewBox);
        globalHeaderLabel.setText("SWTCamper - Finde Deinen perfekten Camper");
        rentingViewController.reloadData();
        navigationViewController.setButtonActive(
          navigationViewController.homeButton
        );
        break;
      case "placeOffer":
        mainStage.getChildren().add(placeOfferViewBox);
        globalHeaderLabel.setText("SWTCamper - Neue Anzeige erstellen");
        modifyOfferViewController.initialize();
        navigationViewController.setButtonActive(
          navigationViewController.newOfferButton
        );
        break;
      case "viewOffer":
        mainStage.getChildren().add(offerViewBox);
        globalHeaderLabel.setText("SWTCamper - Anzeigenansicht");
        break;
      case "activeOffers":
        mainStage.getChildren().add(activeOffersViewBox);
        globalHeaderLabel.setText("SWTCamper - Meine Anzeigen");
        navigationViewController.setButtonActive(
          navigationViewController.activeOffersButton
        );
        myOffersViewController.reloadData();
        break;
      case "history":
        mainStage.getChildren().add(dealHistoryViewBox);
        globalHeaderLabel.setText("SWTCamper - Meine bisherigen Buchungen");
        navigationViewController.setButtonActive(
          navigationViewController.dealHistoryButton
        );
        dealHistoryViewController.reloadData();
        break;
      case "exclude":
        mainStage.getChildren().add(excludeRenterViewBox);
        globalHeaderLabel.setText("SWTCamper - Nutzer ausschlie??en");
        navigationViewController.setButtonActive(
          navigationViewController.excludeButton
        );
        excludeRenterViewController.initialize();
        break;
      case "approve":
        mainStage.getChildren().add(approveDealViewBox);
        globalHeaderLabel.setText("SWTCamper - Neue Nutzer annehmen");
        navigationViewController.setButtonActive(
          navigationViewController.approveButton
        );
        approveNewProvidersViewController.reloadData();
        break;
      case "myBookings":
        mainStage.getChildren().add(myBookingsViewBox);
        globalHeaderLabel.setText("SWTCamper - Meine Buchungen");
        navigationViewController.setButtonActive(
          navigationViewController.myBookingsButton
        );
        myBookingsViewController.reloadData();
        break;
      case "login":
        mainStage.getChildren().add(loginViewBox);
        globalHeaderLabel.setText("SWTCamper - Einloggen");
        navigationViewController.setButtonActive(
          navigationViewController.loginButton
        );
        loginViewController.resetInputFields();
        break;
      case "account":
        mainStage.getChildren().add(accountViewBox);
        globalHeaderLabel.setText("SWTCamper - Dashboard");
        navigationViewController.setButtonActive(
          navigationViewController.accountButton
        );
        if (
          userController
            .getLoggedInUser()
            .getUserRole()
            .equals(UserRole.OPERATOR)
        ) {
          accountViewController.operatorInit(false);
        } else {
          accountViewController.normalUserInit();
        }
        break;
      case "register":
        mainStage.getChildren().add(registerViewBox);
        globalHeaderLabel.setText("SWTCamper - Registrieren");
        navigationViewController.setButtonActive(
          navigationViewController.accountButton
        );
        registerViewController.resetInputFields();
        break;
      case "forgotPassword":
        mainStage.getChildren().add(forgotPasswordViewBox);
        globalHeaderLabel.setText("SWTCamper - Passwort zur??cksetzen");
        navigationViewController.setButtonActive(
          navigationViewController.accountButton
        );
        resetPasswordViewController.resetInputFields();
        break;
      case "reportUser":
        mainStage.getChildren().add(reportUserViewBox);
        break;
      case "logout":
        logout();
        break;
      case "faq":
        mainStage.getChildren().add(faqViewBox);
        globalHeaderLabel.setText("SWTCamper - Frequently Asked Question ");
        navigationViewController.setButtonActive(
          navigationViewController.faqBtn
        );
        break;
    }

    // give hint for blocked users
    if (
      userController.getLoggedInUser() != null &&
      userController.getLoggedInUser().isLocked()
    ) {
      globalHeaderLabel.setText(
        "Du bist geblockt, deine Nutzunsm??glichkeiten sind begrenzt!"
      );
      globalHeaderLabel.getStyleClass().add("text-danger");
    } else {
      globalHeaderLabel.getStyleClass().removeIf(s -> s.equals("text-danger"));
    }
  }

  public void handleExceptionMessage(String message) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Fehler");
    alert.setHeaderText(
      "Bei der Verarbeitung Ihrer Anfrage ist ein Fehler aufgetreten"
    );
    alert.setContentText(message);
    alert.showAndWait();
  }

  public void handleInformationMessage(String message) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Information");
    alert.setHeaderText("Beachten Sie das Folgende:");
    alert.setContentText(message);
    alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
    alert.showAndWait();
  }

  public void handleException(Exception e) {
    handleExceptionMessage(e.getMessage());
  }

  public void login(UserDTO userDTO) throws GenericServiceException {
    login(userDTO, startPageAfterLogin);
  }

  public void login(UserDTO userDTO, String startPage)
    throws GenericServiceException {
    navigationViewController.login(userDTO, startPage);
    latestLoggedInStatus =
      userController.getUserById(userController.getLoggedInUser().getId());
  }

  public void logout() throws GenericServiceException {
    latestLoggedInStatus = null;
    latestView = null;

    userController.logout();
    navigationViewController.logout();
  }
}
