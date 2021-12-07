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
import swtcamper.api.controller.OfferController;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class MainViewController {

  /**
   * Quick Settings
   */
  public final boolean startNavigationHidden = true;

  @Autowired
  public MyOffersViewController myOffersViewController;

  @Autowired
  public NavigationViewController navigationViewController;

  @Autowired
  public RentingViewController rentingViewController;

  @FXML
  public AnchorPane mainStage;

  @FXML
  public Pane homeViewBox;

  @FXML
  public Pane filterOptionsViewBox;

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
  private void initialize() throws GenericServiceException {
    //    reloadData();
    changeView("home");
  }

  public void reloadData() {
    try {
      myOffersViewController.reloadData();
    } catch (GenericServiceException e) {
      handleException(e);
    }
  }

  public void clearView() {
    List<Pane> toRemove = new ArrayList<>();
    for (Object child : mainStage.getChildren()) {
      if (child instanceof Pane) {
        toRemove.add((Pane) child);
      }
    }
    mainStage.getChildren().removeAll(toRemove);
  }

  public void changeView(String switchTo) throws GenericServiceException {
    changeView(switchTo, true);
  }

  public void changeView(String switchTo, boolean reloadData)
    throws GenericServiceException {
    clearView();

    switch (switchTo) {
      case "home":
        mainStage.getChildren().add(homeViewBox);
        if (reloadData) rentingViewController.reloadData();
        navigationViewController.setButtonActive(
          navigationViewController.homeButton
        );
        break;
      case "filterOptions":
        mainStage.getChildren().add(filterOptionsViewBox);
        break;
      case "placeOffer":
        mainStage.getChildren().add(placeOfferViewBox);
        modifyOfferViewController.initialize();
        navigationViewController.setButtonActive(
          navigationViewController.newOfferButton
        );
        break;
      case "activeOffers":
        mainStage.getChildren().add(activeOffersViewBox);
        navigationViewController.setButtonActive(
          navigationViewController.activeOffersButton
        );
        break;
      case "history":
        mainStage.getChildren().add(dealHistoryViewBox);
        navigationViewController.setButtonActive(
          navigationViewController.dealHistoryButton
        );
        break;
      case "exclude":
        mainStage.getChildren().add(excludeRenterViewBox);
        navigationViewController.setButtonActive(
          navigationViewController.excludeButton
        );
        break;
      case "approve":
        mainStage.getChildren().add(approveDealViewBox);
        navigationViewController.setButtonActive(
          navigationViewController.approveButton
        );
        break;
      case "myBookings":
        mainStage.getChildren().add(myBookingsViewBox);
        navigationViewController.setButtonActive(
          navigationViewController.myBookingsButton
        );
        break;
      case "login":
        mainStage.getChildren().add(loginViewBox);
        navigationViewController.setButtonActive(
          navigationViewController.loginButton
        );
        break;
      case "account":
        mainStage.getChildren().add(accountViewBox);
        navigationViewController.setButtonActive(
          navigationViewController.accountButton
        );
        break;
      default:
        System.out.println("gibts nicht");
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

  public void login() throws GenericServiceException {
    navigationViewController.login();
  }

  public void logout() throws GenericServiceException {
    navigationViewController.logout();
  }
}
