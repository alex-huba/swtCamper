package swtcamper.javafx.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.UserDTO;
import swtcamper.api.controller.UserController;
import swtcamper.backend.entities.UserRole;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class NavigationViewController {

  @FXML
  public AnchorPane navigationRoot;

  @FXML
  public VBox navBarItems;

  @FXML
  public Button homeButton;

  @FXML
  public Button newOfferButton;

  @FXML
  public Button activeOffersButton;

  @FXML
  public Button dealHistoryButton;

  @FXML
  public Button excludeButton;

  @FXML
  public Button approveButton;

  @FXML
  public Circle approveNotificationDot;

  @FXML
  public Button myBookingsButton;

  @FXML
  public Circle myBookingsNotificationDot;

  @FXML
  public Button loginButton;

  @FXML
  public Button accountButton;

  @FXML
  public Circle accountNotificationDot;

  @FXML
  public Button logoutBtn;

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private NavigationViewController navigationViewController;

  @Autowired
  private UserController userController;

  @FXML
  public Button faqBtn;

  private boolean isShortText = true;

  @FXML
  private void initialize() {
    if (mainViewController.startNavigationHidden) {
      setShortTexts();
    } else {
      setLongTexts();
    }

    setStartButtons();
  }

  private void setStartButtons() {
    navBarItems.getChildren().clear();
    navBarItems.getChildren().addAll(homeButton, loginButton, faqBtn);
  }

  @FXML
  private void handleNavBtnClick(ActionEvent e) throws GenericServiceException {
    Button selectedButton = (Button) e.getTarget();
    mainViewController.changeView(selectedButton.getAccessibleHelp());
  }

  public void setButtonActive(Button btn) {
    // remove all active classes first
    navBarItems
      .getChildren()
      .stream()
      .filter(node -> node instanceof Button)
      .forEach(node -> node.getStyleClass().removeIf(s -> s.contains("active"))
      );

    btn.getStyleClass().add("active");
  }

  public void login(UserDTO userDTO, String startPage)
    throws GenericServiceException {
    UserRole userRole = userDTO.getUserRole();
    boolean isEnabled = userDTO.isEnabled();
    boolean isLocked = userDTO.isLocked();

    ObservableList<Node> navBarList = navBarItems.getChildren();
    navBarList.clear();

    if (isLocked) {
      navBarList.add(dealHistoryButton);
      navBarList.add(accountButton);
    } else {
      switch (userRole) {
        // Enable renter functionalities
        case RENTER:
          navBarList.addAll(
            homeButton,
            dealHistoryButton,
            myBookingsButton,
            accountButton,
            faqBtn
          );
          break;
        // Enable provider functionalities
        case PROVIDER:
          if (isEnabled) {
            navBarList.addAll(
              homeButton,
              newOfferButton,
              activeOffersButton,
              dealHistoryButton,
              excludeButton,
              myBookingsButton,
              faqBtn
            );
          } else {
            navBarList.addAll(
              homeButton,
              dealHistoryButton,
              myBookingsButton,
              accountButton,
              faqBtn
            );
          }
          break;
        // Enable operator functionalities
        case OPERATOR:
          navBarList.addAll(
            homeButton,
            newOfferButton,
            activeOffersButton,
            dealHistoryButton,
            excludeButton,
            approveButton,
            myBookingsButton,
            accountButton,
            faqBtn
          );
          break;
        default:
          navBarList.addAll(homeButton, accountButton, faqBtn);
          break;
      }
    }

    logoutBtn.setVisible(true);

    mainViewController.changeView(startPage);
    if (isShortText) {
      setShortTexts();
    } else {
      setLongTexts();
    }
  }

  public void logout() throws GenericServiceException {
    setStartButtons();
    logoutBtn.setVisible(false);
    mainViewController.changeView("home");
    if (isShortText) {
      setShortTexts();
    } else {
      setLongTexts();
    }
  }

  public void toggleNavBar() {
    if (isShortText) {
      setLongTexts();
    } else {
      setShortTexts();
    }
  }

  /**
   * Removes text from buttons
   */
  private void setShortTexts() {
    isShortText = true;

    for (Object child : navBarItems.getChildren()) {
      if (
        child instanceof Button &&
        ((Button) child).getStyleClass().contains("navBtn")
      ) {
        ((Button) child).setText("");
        ((Button) child).setPrefWidth(45);

        faqBtn.setText("");
        faqBtn.setPrefWidth(45);

        logoutBtn.setText("");
        logoutBtn.setPrefWidth(45);
      }
    }
  }

  /**
   * Adds text to buttons
   */
  private void setLongTexts() {
    isShortText = false;

    for (Object child : navBarItems.getChildren()) {
      if (
        child instanceof Button &&
        ((Button) child).getStyleClass().contains("navBtn")
      ) {
        ((Button) child).setText(((Button) child).getAccessibleText());
        ((Button) child).setPrefWidth(172);

        faqBtn.setText("FAQ");
        faqBtn.setPrefWidth(172);

        logoutBtn.setText(logoutBtn.getAccessibleText());
        logoutBtn.setPrefWidth(172);
      }
    }
  }

  public void showApproveNotification() {
    Tooltip t = new Tooltip("Es können neue Nutzer akzeptiert werden");
    Tooltip.install(approveNotificationDot, t);

    approveNotificationDot.setVisible(true);
  }

  public void hideApproveNotification() {
    approveNotificationDot.setVisible(false);
  }

  public void showBookingNotification() {
    Tooltip t = new Tooltip("Es gibt neue Buchungsanfragen");
    Tooltip.install(myBookingsNotificationDot, t);

    myBookingsNotificationDot.setVisible(true);
  }

  public void hideBookingNotification() {
    myBookingsNotificationDot.setVisible(false);
  }

  public void showAccountNotification() {
    Tooltip t = new Tooltip("Es gibt neue Nutzerbeschwerden");
    Tooltip.install(accountNotificationDot, t);

    accountNotificationDot.setVisible(true);
  }

  public void hideAccountNotification() {
    accountNotificationDot.setVisible(false);
  }
}
