package swtcamper.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NavigationViewController {

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

  private boolean hoverLock = false;
  private final String activeButtonString =
    "-fx-background-color:#333; -fx-text-fill:#FFF";

  @FXML
  private void initialize() {
    loginButton.setStyle(activeButtonString);
    navigationRoot.getChildren().remove(navigationButtons);
  }

  private void resetStyle() {
    myOfferButton.setStyle("");
    rentCamperButton.setStyle("");
    placeOfferButton.setStyle("");
    loginButton.setStyle("");
  }

  public void showMyOffers() {
    resetStyle();
    myOfferButton.setStyle(activeButtonString);
    mainViewController.changeView("myOffers");
  }

  public void showRentCamper() {
    resetStyle();
    rentCamperButton.setStyle(activeButtonString);
    mainViewController.changeView("rentVan");
  }

  public void showPlaceOffer() {
    resetStyle();
    placeOfferButton.setStyle(activeButtonString);
    mainViewController.changeView("placeOffer");
  }

  public void showLogin() {
    resetStyle();
    loginButton.setStyle(activeButtonString);
    mainViewController.changeView("login");
  }

  /**
   * Adds the NavigationBar to the Scene and sets it to its max. width defined in navigationView.fxml
   */
  public void showNavigation() {
    if (!navigationRoot.getChildren().contains(navigationButtons)) {
      navigationRoot.getChildren().add(navigationButtons);
      navigationRoot.setPrefWidth(navigationRoot.getMaxWidth());
    }
  }

  /**
   * Removes the NavigationBar from the Scene and sets it to its max. width defined in navigationView.fxml
   */
  public void hideNavigation() {
    if (navigationRoot.getChildren().contains(navigationButtons)) {
      navigationRoot.getChildren().remove(navigationButtons);
      navigationRoot.setPrefWidth(navigationRoot.getMinWidth());
    }
  }

  /**
   * Sets a Boolean lock to handle hovering over the ToggleButton
   */
  public void toggleNavigationControl() {
    if (hoverLock) {
      hideNavigation();
      hoverLock = false;
    } else {
      hoverLock = navigationRoot.getChildren().contains(navigationButtons);
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
