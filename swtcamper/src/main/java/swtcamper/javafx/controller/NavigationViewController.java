package swtcamper.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NavigationViewController {

  @Autowired
  private MainViewController mainViewController;

  @FXML
  public VBox navigationRoot;

  @FXML
  public Button myOfferButton;

  @FXML
  public SVGPath myOfferButtonIcon;

  @FXML
  public Button rentCamperButton;

  @FXML
  public SVGPath rentCamperButtonIcon;

  @FXML
  public Button placeOfferButton;

  @FXML
  public SVGPath placeOfferButtonIcon;

  @FXML
  public Button loginButton;

  @FXML
  public SVGPath loginButtonIcon;

  private boolean isShortText = true;
  private boolean hoverLock;

  @FXML
  private void initialize() {
    if (mainViewController.startNavigationHidden) {
      setShortTexts();
      hoverLock = false;
    } else {
      setLongTexts();
      hoverLock = true;
    }
  }

  /**
   * Removes text from buttons
   */
  private void setShortTexts() {
    isShortText = true;

    for (Object child : navigationRoot.getChildren()) {
      if (
        child instanceof Button &&
        ((Button) child).getStyleClass().contains("navBtn")
      ) {
        ((Button) child).setText("");
      }
    }

    navigationRoot.setPrefWidth(navigationRoot.getMinWidth());
  }

  /**
   * Adds text to buttons
   */
  private void setLongTexts() {
    isShortText = false;

    for (Object child : navigationRoot.getChildren()) {
      if (
        child instanceof Button &&
        ((Button) child).getStyleClass().contains("navBtn")
      ) {
        ((Button) child).setText(((Button) child).getAccessibleText());
      }
    }

    // Pos.TOP_LEFT if you want buttons to be aligned
    navigationRoot.setAlignment(Pos.TOP_CENTER);
    navigationRoot.setPrefWidth(navigationRoot.getMaxWidth());
  }

  @FXML
  private void handleNavBtnClick(ActionEvent e) {
    for (Object child : navigationRoot.getChildren()) {
      if (child instanceof Button) ((Button) child).getStyleClass()
        .removeIf(c -> c.contains("active"));
    }

    Button selectedButton = (Button) e.getTarget();
    selectedButton.getStyleClass().add("active");
    mainViewController.changeView(selectedButton.getAccessibleHelp());
  }

  /**
   * Adds the NavigationBar to the Scene and sets it to its max. width defined in navigationView.fxml
   */
  public void showNavigation() {
    if (isShortText) setLongTexts();
  }

  /**
   * Removes the NavigationBar from the Scene and sets it to its max. width defined in navigationView.fxml
   */
  public void hideNavigation() {
    if (!isShortText) setShortTexts();
  }

  /**
   * Sets a Boolean lock to handle hovering over the ToggleButton
   */
  public void toggleNavigationControl() {
    if (isShortText) {
      showNavigation();
      hoverLock = true;
    } else {
      hideNavigation();
      hoverLock = false;
    }
  }

  /**
   * Adds the NavigationBar to the Scene when the pointer enters the ToggleButton
   */
  public void hoverToggleNavigationOn() {
    if (mainViewController.openNavigationOnHover) showNavigation();
  }

  /**
   * Adds the NavigationBar to the Scene when the pointer leaves the ToggleButton - iff it was not clicked before
   */
  public void hoverToggleNavigationOff() {
    if (!hoverLock) hideNavigation();
  }
}
