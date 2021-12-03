package swtcamper.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.controller.OfferController;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class MainViewController {

  @Autowired
  private MyOffersViewController myOffersViewController;

  @FXML
  public TabPane tabPane;

  @FXML
  public Tab loginTab;

  @FXML
  public Tab offersTab;

  @FXML
  public Tab rentCamperTab;

  @FXML
  public Tab placeOfferTab;

  @FXML
  public Tab updateOfferTab;

  @FXML
  private void initialize() {
    reloadData();
  }

  public void reloadData() {
    try {
      myOffersViewController.reloadData();
    } catch (GenericServiceException e) {
      handleException(e);
    }
  }

  public void jumpToTab(String tabSpecifier) {
    if (tabSpecifier.equals("offers")) tabPane
      .getSelectionModel()
      .select(offersTab);
    if (tabSpecifier.equals("update")) tabPane
      .getSelectionModel()
      .select(updateOfferTab);
    if (tabSpecifier.equals("placeOffer")) tabPane
      .getSelectionModel()
      .select(placeOfferTab);
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
}
