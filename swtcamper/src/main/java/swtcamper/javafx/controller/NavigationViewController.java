package swtcamper.javafx.controller;

import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.UserRoleDTO;
import swtcamper.api.controller.UserController;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class NavigationViewController {

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private NavigationViewController navigationViewController;

  @Autowired
  private UserController userController;

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
  public Button myBookingsButton;

  @FXML
  public Circle myBookingsNotificationDot;

  @FXML
  public Button loginButton;

  @FXML
  public Button accountButton;

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
    navBarItems.getChildren().addAll(homeButton, loginButton);
  }

  @FXML
  private void handleNavBtnClick(ActionEvent e) throws GenericServiceException {
    Button selectedButton = (Button) e.getTarget();
    mainViewController.changeView(selectedButton.getAccessibleHelp());
  }

  public void setButtonActive(Button btn) {
    // remove all active classes first
    for (Object child : navBarItems.getChildren()) {
      if (child instanceof Button) ((Button) child).getStyleClass()
        .removeIf(c -> c.contains("active"));
    }

    btn.getStyleClass().add("active");
  }

  public void login(UserRoleDTO userRoleDTO, boolean isEnabled)
    throws GenericServiceException {
    navBarItems.getChildren().clear();

    // Enable renter functionalities
    if (userRoleDTO == UserRoleDTO.RENTER) {
      navBarItems.getChildren().addAll(homeButton, dealHistoryButton, myBookingsButton, accountButton);
      // Enable provider functionalities
    } else if (userRoleDTO == UserRoleDTO.PROVIDER) {
      navBarItems.getChildren().add(homeButton);
      navBarItems.getChildren().add(newOfferButton);
      if (isEnabled) navBarItems.getChildren().add(activeOffersButton);
      navBarItems.getChildren().add(dealHistoryButton);
      if (isEnabled) navBarItems.getChildren().add(excludeButton);
      navBarItems.getChildren().add(myBookingsButton);
      navBarItems.getChildren().add(accountButton);
      // Enable operator functionalities
    } else {
      navBarItems.getChildren().addAll(homeButton,newOfferButton,activeOffersButton,
              dealHistoryButton,
              excludeButton,
              approveButton,
              myBookingsButton,
              accountButton);
    }

    mainViewController.changeView("home");
    if (isShortText) {
      setShortTexts();
    } else {
      setLongTexts();
    }
  }

  public void logout() throws GenericServiceException {
    setStartButtons();
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
      }
    }
  }

  public void showBookingNotification() {
      myBookingsNotificationDot.setVisible(true);
  }

  public void resetBookingNotification() {
    myBookingsNotificationDot.setVisible(false);
  }
}
