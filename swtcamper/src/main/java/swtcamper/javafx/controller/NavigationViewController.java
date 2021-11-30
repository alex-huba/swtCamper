package swtcamper.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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

  public void toggleNavigation() {
    if (navigationRoot.getChildren().contains(navigationButtons)) {
      navigationRoot.getChildren().remove(navigationButtons);
      mainViewController.mainStage.setPrefWidth(830);
    } else {
      navigationRoot.getChildren().add(navigationButtons);
      mainViewController.mainStage.setPrefWidth(677);
    }
//    new EventHandler<ActionEvent>() {
//      @Override
//      public void handle(ActionEvent event) {
//        root.getChildren().add(fileRoot);
//        FadeTransition hideEditorRootTransition = new FadeTransition(Duration.millis(500), editorRoot);
//        hideEditorRootTransition.setFromValue(1.0);
//        hideEditorRootTransition.setToValue(0.0);
//
//        FadeTransition showFileRootTransition = new FadeTransition(Duration.millis(500), fileRoot);
//        showFileRootTransition.setFromValue(0.0);
//        showFileRootTransition.setToValue(1.0);
//        hideEditorRootTransition.play();
//        showFileRootTransition.play();
//      }
  }
}
