package swtcamper.javafx.controller;

import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
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
    navBarItems.getChildren().removeIf(b -> true);
    navBarItems.getChildren().add(homeButton);
    navBarItems.getChildren().add(loginButton);
  }

  @FXML
  private void handleNavBtnClick(ActionEvent e) {
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

  public void login() {
    navBarItems.getChildren().removeIf(b -> true);
    Button[] toAdd = {
      homeButton,
      newOfferButton,
      activeOffersButton,
      dealHistoryButton,
      excludeButton,
      approveButton,
      myBookingsButton,
      accountButton,
    };
    navBarItems.getChildren().addAll(toAdd);
    mainViewController.changeView("account");
    if (isShortText) {
      setShortTexts();
    } else {
      setLongTexts();
    }
  }

  public void logout() {
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
}
