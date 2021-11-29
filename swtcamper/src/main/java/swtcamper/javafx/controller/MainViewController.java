package swtcamper.javafx.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.springframework.stereotype.Component;

@Component
public class MainViewController {

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
    myOffersViewBox.setVisible(false);
    rentVanViewBox.setVisible(false);
    placeOfferViewBox.setVisible(false);
    loginViewBox.setVisible(true);

    reloadData();
  }

  public void reloadData() {}

  public void changeView(String switchTo) {
    myOffersViewBox.setVisible(false);
    rentVanViewBox.setVisible(false);
    placeOfferViewBox.setVisible(false);
    loginViewBox.setVisible(false);

    if (switchTo.equals("myOffers")) myOffersViewBox.setVisible(true);
    if (switchTo.equals("rentVan")) rentVanViewBox.setVisible(true);
    if (switchTo.equals("placeOffer")) placeOfferViewBox.setVisible(true);
    if (switchTo.equals("login")) loginViewBox.setVisible(true);
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
