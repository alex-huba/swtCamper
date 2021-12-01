package swtcamper.javafx.controller;

import java.awt.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NavigationViewController {

  /**
   * change settings here
   */
  private boolean isShortText = false;
  private final boolean darkMode = false;
  private final String darkPrimary = "#383551";
  private final String darkSecondary = "#FF4355";

  @Autowired
  private MainViewController mainViewController;

  @FXML
  public VBox navigationRoot;

  @FXML
  public VBox toggleBtnBg;

  @FXML
  public VBox navigationButtons;

  @FXML
  public Button myOfferButton;

  @FXML
  public ImageView myOfferButtonIcon;

  @FXML
  public Button rentCamperButton;

  @FXML
  public ImageView rentCamperButtonIcon;

  @FXML
  public Button placeOfferButton;

  @FXML
  public ImageView placeOfferButtonIcon;

  @FXML
  public Button loginButton;

  @FXML
  public ImageView loginButtonIcon;

  private boolean hoverLock;

  @FXML
  private void initialize() {
    if (darkMode) {
      navigationRoot.setStyle(
        String.format("-fx-background-color: %s;", darkPrimary)
      );
      toggleBtnBg.setStyle(
        String.format("-fx-background-color: %s;", darkSecondary)
      );

      myOfferButtonIcon.setImage(new Image("./icons/list-ul_light.png"));
      rentCamperButtonIcon.setImage(
        new Image("./icons/comments-dollar_light.png")
      );
      placeOfferButtonIcon.setImage(new Image("./icons/plus-circle_light.png"));
      loginButtonIcon.setImage(new Image("./icons/user-circle_light.png"));
    }

    if (isShortText) {
      setShortTexts();
      hoverLock = false;
    } else {
      setLongTexts();
      hoverLock = true;
    }
  }

  /**
   * Removes background from buttons (and makes text white if darkMode is active)
   */
  private void makeButtonsTransparent() {
    myOfferButton.setStyle(
      "-fx-background-color: transparent;" +
      (darkMode ? "-fx-text-fill: #FFF;" : "")
    );
    rentCamperButton.setStyle(
      "-fx-background-color: transparent;" +
      (darkMode ? "-fx-text-fill: #FFF;" : "")
    );
    placeOfferButton.setStyle(
      "-fx-background-color: transparent;" +
      (darkMode ? "-fx-text-fill: #FFF;" : "")
    );
    loginButton.setStyle(
      "-fx-background-color: transparent;" +
      (darkMode ? "-fx-text-fill: #FFF;" : "")
    );
  }

  /**
   * Resets buttons' styles
   */
  private void removeCustomStyling() {
    myOfferButton.setStyle("");
    rentCamperButton.setStyle("");
    placeOfferButton.setStyle("");
    loginButton.setStyle("");
  }

  /**
   * Removes text from buttons
   */
  private void setShortTexts() {
    isShortText = true;
    makeButtonsTransparent();

    myOfferButton.setText("");
    rentCamperButton.setText("");
    placeOfferButton.setText("");
    loginButton.setText("");

    navigationRoot.setPrefWidth(navigationRoot.getMinWidth());
    toggleBtnBg.setPrefWidth(navigationRoot.getPrefWidth());
  }

  /**
   * Adds text to buttons
   */
  private void setLongTexts() {
    isShortText = false;

    if (darkMode) {
      makeButtonsTransparent();
    } else {
      removeCustomStyling();
    }

    myOfferButton.setText("My Offers");
    rentCamperButton.setText("Rent Van");
    placeOfferButton.setText("New Offer");
    loginButton.setText("Login");

    navigationRoot.setPrefWidth(navigationRoot.getMaxWidth());
    toggleBtnBg.setPrefWidth(navigationRoot.getPrefWidth());
  }

  public void showMyOffers() {
    mainViewController.changeView("myOffers");
  }

  public void showRentCamper() {
    mainViewController.changeView("rentVan");
  }

  public void showPlaceOffer() {
    mainViewController.changeView("placeOffer");
  }

  public void showLogin() {
    mainViewController.changeView("login");
  }

  /**
   * Adds the NavigationBar to the Scene and sets it to its max. width defined in navigationView.fxml
   */
  public void showNavigation() {
    if (isShortText) {
      setLongTexts();
    }
  }

  /**
   * Removes the NavigationBar from the Scene and sets it to its max. width defined in navigationView.fxml
   */
  public void hideNavigation() {
    if (!isShortText) {
      setShortTexts();
    }
  }

  /**
   * Sets a Boolean lock to handle hovering over the ToggleButton
   */
  public void toggleNavigationControl() {
    if (hoverLock) {
      hoverLock = false;
    } else {
      hoverLock = !isShortText;
    }
  }

  /**
   * Adds the NavigationBar to the Scene when the pointer enters the ToggleButton
   */
  public void hoverToggleNavigationOn() {
    showNavigation();
  }

  /**
   * Adds the NavigationBar to the Scene when the pointer leaves the ToggleButton - iff it was not clicked before
   */
  public void hoverToggleNavigationOff() {
    if (!hoverLock) hideNavigation();
  }
}
