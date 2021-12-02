package swtcamper.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
  public AnchorPane navigationRoot;

  @FXML
  public VBox navBarItems;

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

  @FXML
  private void initialize() {
    if (mainViewController.startNavigationHidden) {
      setShortTexts();
    } else {
      setLongTexts();
    }
  }

  @FXML
  private void handleNavBtnClick(ActionEvent e) {
    for (Object child : navBarItems.getChildren()) {
      if (child instanceof Button) ((Button) child).getStyleClass()
        .removeIf(c -> c.contains("active"));
    }

    Button selectedButton = (Button) e.getTarget();
    selectedButton.getStyleClass().add("active");

    mainViewController.changeView(selectedButton.getAccessibleHelp());
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
}
