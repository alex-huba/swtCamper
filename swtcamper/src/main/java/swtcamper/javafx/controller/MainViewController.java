package swtcamper.javafx.controller;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.UserDTO;
import swtcamper.api.contract.UserRoleDTO;
import swtcamper.api.controller.UserController;
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

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private UserController userController;

  @Autowired
  public MyOffersViewController myOffersViewController;

  @Autowired
  public NavigationViewController navigationViewController;

  @Autowired
  public ModifyOfferViewController modifyOfferViewController;

  @Autowired
  public RentingViewController rentingViewController;

  @Autowired
  public RegisterViewController registerViewController;

  @Autowired
  public AccountViewController accountViewController;

  @Autowired
  public LoginViewController loginViewController;

  @Autowired
  public ResetPasswordViewController resetPasswordViewController;

  @Autowired
  public OfferViewController offerViewController;

  @FXML
  public AnchorPane mainStage;

  @FXML
  public Node homeViewBox;

  @FXML
  public Node placeOfferViewBox;

  @FXML
  public Pane offerViewBox;

  @FXML
  public Pane activeOffersViewBox;

  @FXML
  public Pane dealHistoryViewBox;

  @FXML
  public Pane excludeRenterViewBox;

  @FXML
  public Pane approveDealViewBox;

  @FXML
  public Pane myBookingsViewBox;

  @FXML
  public Pane loginViewBox;

  @FXML
  public Pane accountViewBox;

  @FXML
  public Pane registerViewBox;

  @FXML
  public Pane forgotPasswordViewBox;

  private User latestLoggedInStatus = null;
  private String latestView = null;
  boolean updateHappening = false;

  @FXML
  public Pane moreAboutOfferViewBox;

  @FXML
  private void initialize() throws GenericServiceException {
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
    List<Pane> toRemove = new ArrayList<>();
    for (Object child : mainStage.getChildren()) {
      if (child instanceof Pane) {
        toRemove.add((Pane) child);
      }
    }
    mainStage.getChildren().removeAll(toRemove);
  }

  @Scheduled(fixedDelay = 1000)
  private void listenForDataBaseChanges() throws GenericServiceException {
    if(userController.getLoggedInUser() != null) {
      // check for new providers that need to be enabled
      if (
              userController
                      .getLoggedInUser()
                      .getUserRole()
                      .equals(UserRole.OPERATOR) &&
                      userController
                              .getAllUsers()
                              .stream()
                              .anyMatch(user -> !user.isEnabled())
      ) {
        navigationViewController.showApproveNotification();
      } else {
        navigationViewController.hideApproveNotification();
      }

      if (
              latestLoggedInStatus != null &&
                      latestView != null
      ) {
        // get the latest update for the logged-in user to check if there were made any changes
        User checkUser = userController.getUserById(
                userController.getLoggedInUser().getId()
        );
        if (!updateHappening) {
          // user-role has changed
          if (
                  !checkUser.getUserRole().equals(latestLoggedInStatus.getUserRole())
          ) {
            updateHappening = true;
            Platform.runLater(() -> {
              try {
                handleInformationMessage(
                        "Deine Rolle hat sich geändert zu " +
                                checkUser.getUserRole() +
                                "!\nDie Oberfläche wird sich aktualisieren"
                );
                login(modelMapper.userToUserDTO(checkUser), "home");
                updateHappening = false;
              } catch (GenericServiceException ignore) {
              }
            });
            // user got enabled
          } else if (checkUser.isEnabled() && !latestLoggedInStatus.isEnabled()) {
            updateHappening = true;
            Platform.runLater(() -> {
              try {
                handleInformationMessage(
                        "Du wurdest akzeptiert und kannst jetzt Anzeigen erstellen!\nDie Oberfläche wird sich aktualisieren"
                );
                login(modelMapper.userToUserDTO(checkUser), latestView);
                updateHappening = false;
              } catch (GenericServiceException ignore) {
              }
            });
            // user got disabled
          } else if (!checkUser.isEnabled() && latestLoggedInStatus.isEnabled()) {
            updateHappening = true;
            Platform.runLater(() -> {
              try {
                handleInformationMessage(
                        "Du wurdest ent-akzeptiert und kannst keine Anzeigen mehr erstellen!\nDie Oberfläche wird sich aktualisieren"
                );
                login(modelMapper.userToUserDTO(checkUser), "home");
                updateHappening = false;
              } catch (GenericServiceException ignore) {
              }
            });
            // user got locked
          } else if (checkUser.isLocked() && !latestLoggedInStatus.isLocked()) {
            updateHappening = true;
            Platform.runLater(() -> {
              try {
                handleInformationMessage(
                        "Du wurdest gesperrt und kannst nicht mehr mit anderen Nutzern interagieren!\nDie Oberfläche wird sich aktualisieren"
                );
                login(modelMapper.userToUserDTO(checkUser), "home");
                updateHappening = false;
              } catch (GenericServiceException ignore) {
              }
            });
            // user got unlocked
          } else if (!checkUser.isLocked() && latestLoggedInStatus.isLocked()) {
            updateHappening = true;
            Platform.runLater(() -> {
              try {
                handleInformationMessage(
                        "Du wurdest entsperrt und kannst wieder mit anderen Nutzern interagieren!\nDie Oberfläche wird sich aktualisieren"
                );
                login(modelMapper.userToUserDTO(checkUser), latestView);
                updateHappening = false;
              } catch (GenericServiceException ignore) {
              }
            });
          }
        }
      }
    }
  }

  public void changeView(String switchTo) throws GenericServiceException {
    changeView(switchTo, true);
  }

  public void changeView(String switchTo, boolean reloadData)
    throws GenericServiceException {
    latestView = switchTo;
    clearView();

    switch (switchTo) {
      case "home":
        mainStage.getChildren().add(homeViewBox);
        if (reloadData) rentingViewController.reloadData();
        navigationViewController.setButtonActive(
          navigationViewController.homeButton
        );
        break;
      case "placeOffer":
        mainStage.getChildren().add(placeOfferViewBox);
        modifyOfferViewController.initialize();
        navigationViewController.setButtonActive(
          navigationViewController.newOfferButton
        );
        break;
      case "viewOffer":
        mainStage.getChildren().add(offerViewBox);
        break;
      case "activeOffers":
        mainStage.getChildren().add(activeOffersViewBox);
        navigationViewController.setButtonActive(
          navigationViewController.activeOffersButton
        );
        myOffersViewController.reloadData();
        break;
      case "history":
        mainStage.getChildren().add(dealHistoryViewBox);
        navigationViewController.setButtonActive(
          navigationViewController.dealHistoryButton
        );
        break;
      case "exclude":
        mainStage.getChildren().add(excludeRenterViewBox);
        navigationViewController.setButtonActive(
          navigationViewController.excludeButton
        );
        break;
      case "approve":
        mainStage.getChildren().add(approveDealViewBox);
        navigationViewController.setButtonActive(
          navigationViewController.approveButton
        );
        break;
      case "myBookings":
        mainStage.getChildren().add(myBookingsViewBox);
        navigationViewController.setButtonActive(
          navigationViewController.myBookingsButton
        );
        break;
      case "login":
        mainStage.getChildren().add(loginViewBox);
        navigationViewController.setButtonActive(
          navigationViewController.loginButton
        );
        loginViewController.resetInputFields();
        break;
      case "account":
        mainStage.getChildren().add(accountViewBox);
        navigationViewController.setButtonActive(
          navigationViewController.accountButton
        );
        if (
          userController
            .getLoggedInUser()
            .getUserRole()
            .equals(UserRole.OPERATOR)
        ) {
          accountViewController.operatorInit();
        } else {
          accountViewController.normalUserInit();
        }
        break;
      case "register":
        mainStage.getChildren().add(registerViewBox);
        navigationViewController.setButtonActive(
          navigationViewController.accountButton
        );
        registerViewController.resetInputFields();
        break;
      case "forgotPassword":
        mainStage.getChildren().add(forgotPasswordViewBox);
        navigationViewController.setButtonActive(
          navigationViewController.accountButton
        );
        resetPasswordViewController.resetInputFields();
        break;
      case "moreInfoOffer":
        mainStage.getChildren().add(moreAboutOfferViewBox);
        break;
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
    userController.logout();
    navigationViewController.logout();
  }
}
