package swtcamper.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NavigationViewController {

  /**
   * change settings here
   */
  private boolean isShortText = false;
  private final String activeButtonString =
    "-fx-background-color:#333; -fx-text-fill:#FFF";

  @Autowired
  private MainViewController mainViewController;

  @FXML
  public VBox navigationRoot;

  @FXML
  public VBox navigationButtons;

  @FXML
  public Button myOfferButton;

  @FXML
  public Button rentCamperButton;

  @FXML
  public Button placeOfferButton;

  @FXML
  public Button loginButton;

  private boolean hoverLock;

  @FXML
  private void initialize() {
    if (isShortText) {
      setShortTexts();
      hoverLock = false;
    } else {
      setLongTexts();
      hoverLock = true;
    }
  }

  private void makeButtonsTransparent() {
    myOfferButton.setStyle("-fx-background-color: transparent;");
    rentCamperButton.setStyle("-fx-background-color: transparent;");
    placeOfferButton.setStyle("-fx-background-color: transparent;");
    loginButton.setStyle("-fx-background-color: transparent;");
  }

  private void removeCustomStyling() {
    myOfferButton.setStyle("");
    rentCamperButton.setStyle("");
    placeOfferButton.setStyle("");
    loginButton.setStyle("");
  }

  private void setShortTexts() {
    isShortText = true;
    makeButtonsTransparent();

    myOfferButton.setText("");
    rentCamperButton.setText("");
    placeOfferButton.setText("");
    loginButton.setText("");

    navigationRoot.setPrefWidth(navigationRoot.getMinWidth());
  }

  private void setLongTexts() {
    isShortText = false;
    removeCustomStyling();

    myOfferButton.setText("My Offers");
    rentCamperButton.setText("Rent Van");
    placeOfferButton.setText("New Offer");
    loginButton.setText("Login");

    navigationRoot.setPrefWidth(navigationRoot.getMaxWidth());
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
      hoverLock = loginButton.getText().equals("Login");
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
