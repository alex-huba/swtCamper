package swtcamper.javafx.controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainViewController {

  /**
   * Quick Settings
   */
  public final boolean darkMode = true;
  public final String darkPrimaryColor = "#383551";
  public final String darkSecondaryColor = "#FF4355";
  public final String lightActiveClass =
    "-fx-background-color: lightgrey; -fx-background-radius: 50; -fx-text-fill: #000;";
  public final String darkActiveClass =
    "-fx-background-color: #514d7c; -fx-background-radius: 50; -fx-text-fill: #FFF;";
  public final boolean startNavigationHidden = true;
  public final boolean openNavigationOnHover = false;

  @Autowired
  public NavigationViewController navigationViewController;

  @FXML
  public HBox headerHBox;

  @FXML
  public Pane mainStage;

  @FXML
  public Pane myOffersViewBox;

  @FXML
  public Pane rentVanViewBox;

  @FXML
  public Pane placeOfferViewBox;

  @FXML
  public Pane loginViewBox;

  @FXML
  private void initialize() {
    reloadData();
    changeView("login");

    if (darkMode) {
      headerHBox.setStyle(
        String.format("-fx-background-color: %s;", darkSecondaryColor)
      );
    }
  }

  public void reloadData() {}

  public void clearView() {
    mainStage.getChildren().remove(myOffersViewBox);
    mainStage.getChildren().remove(rentVanViewBox);
    mainStage.getChildren().remove(placeOfferViewBox);
    mainStage.getChildren().remove(loginViewBox);
  }

  public void changeView(String switchTo) {
    clearView();

    if (switchTo.equals("myOffers")) mainStage
      .getChildren()
      .add(myOffersViewBox);
    if (switchTo.equals("rentVan")) mainStage.getChildren().add(rentVanViewBox);
    if (switchTo.equals("placeOffer")) mainStage
      .getChildren()
      .add(placeOfferViewBox);
    if (switchTo.equals("login")) mainStage.getChildren().add(loginViewBox);
  }

  public void handleExceptionMessage(String message) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Exception");
    alert.setHeaderText("There has been an error processing your request");
    alert.setContentText(String.format("Message: %s", message));
    alert.showAndWait();
  }

  public void handleInformationMessage(String message) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Information");
    alert.setHeaderText("Note the following");
    alert.setContentText(String.format("Message: %s", message));
    alert.showAndWait();
  }

  public void handleException(Exception e) {
    handleExceptionMessage(e.getMessage());
  }

  public void toggleNavigationControl() {
    navigationViewController.toggleNavigationControl();
  }
}
