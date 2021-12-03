package swtcamper.javafx.controller;

import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainViewController {

  /**
   * Quick Settings
   */
  public final boolean startNavigationHidden = true;

  @Autowired
  public NavigationViewController navigationViewController;

  @FXML
  public AnchorPane mainStage;

  @FXML
  public Pane homeViewBox;

  @FXML
  public Pane placeOfferViewBox;

  @FXML
  public Pane activeOffersViewBox;

  @FXML
  public Pane dealHistoryViewBox;

  @FXML
  public Pane excludeRenterViewBox;

  @FXML
  public Pane approveDealViewBox;

  @FXML
  public Pane myBookingsViewBox;

  @FXML
  public Pane loginViewBox;

  @FXML
  public Pane accountViewBox;

  @FXML
  private void initialize() {
    reloadData();
    changeView("login");
  }

  public void reloadData() {}

  public void clearView() {
    List<Pane> toRemove = new ArrayList<>();
    for (Object child : mainStage.getChildren()) {
      if (child instanceof Pane) {
        toRemove.add((Pane) child);
      }
    }
    mainStage.getChildren().removeAll(toRemove);
  }

  public void changeView(String switchTo) {
    clearView();

    switch (switchTo) {
      case "home":
        mainStage.getChildren().add(homeViewBox);
        break;
      case "placeOffer":
        mainStage.getChildren().add(placeOfferViewBox);
        break;
      case "activeOffers":
        mainStage.getChildren().add(activeOffersViewBox);
        break;
      case "history":
        mainStage.getChildren().add(dealHistoryViewBox);
        break;
      case "exclude":
        mainStage.getChildren().add(excludeRenterViewBox);
        break;
      case "approve":
        mainStage.getChildren().add(approveDealViewBox);
        break;
      case "myBookings":
        mainStage.getChildren().add(myBookingsViewBox);
        break;
      case "login":
        mainStage.getChildren().add(loginViewBox);
        break;
      case "account":
        mainStage.getChildren().add(accountViewBox);
        break;
    }
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

  public void login() {
    navigationViewController.login();
  }

  public void logout() {
    navigationViewController.logout();
  }
}
