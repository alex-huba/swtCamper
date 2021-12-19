package swtcamper.javafx.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.OfferDTO;
import swtcamper.api.controller.OfferController;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class MyOffersViewController {

  private OfferDTO selectedOffer;

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private OfferController offerController;

  @Autowired
  private ModifyOfferViewController modifyOfferViewController;

  @Autowired
  private OfferViewController offerViewController;

  @FXML
  public ListView<OfferDTO> offersList;

  @FXML
  public void initialize() throws GenericServiceException {
    reloadData();
    offersList.setOnMouseClicked(click -> {
      OfferDTO selectedItem = offersList.getSelectionModel().getSelectedItem();
      //Listener for right click
      if (click.isSecondaryButtonDown()) {
        //ignore
      }
      //Listener for double click
      if (click.getClickCount() == 2) {
        showInfoAlert(selectedItem);
      }
    });
  }

  @FXML
  public void placeOfferAction()
    throws GenericServiceException {
    mainViewController.changeView("placeOffer");
  }

  @FXML
  public void viewOfferAction() throws GenericServiceException {
    OfferDTO selectedOffer = offersList.getSelectionModel().getSelectedItem();
    if (selectedOffer != null) {
      mainViewController.changeView("viewOffer");
      offerViewController.initialize(selectedOffer);
    } else {
      showSelectOfferFirstInfo();
    }
  }

  private void showSelectOfferFirstInfo() {
    Alert alert = new Alert(
      Alert.AlertType.WARNING,
      "Bitte wähle erst ein Angebot aus der Liste"
    );
    alert.showAndWait();
  }

  @FXML
  public void updateOfferAction() throws GenericServiceException {
    OfferDTO selectedOffer = offersList.getSelectionModel().getSelectedItem();
    if (selectedOffer != null) {
      mainViewController.changeView("placeOffer");
      modifyOfferViewController.initialize(selectedOffer);
    } else {
      showSelectOfferFirstInfo();
    }
  }

  @FXML
  public void removeOfferAction() throws GenericServiceException {
    OfferDTO selectedOffer = offersList.getSelectionModel().getSelectedItem();
    if (selectedOffer != null) {
      Alert confirmDelete = new Alert(
        Alert.AlertType.CONFIRMATION,
        "Willst du dieses Angebot wirklich entfernen?"
      );
      Optional<ButtonType> result = confirmDelete.showAndWait();

      if (result.isPresent() && result.get() == ButtonType.OK) {
        offerController.delete(selectedOffer.getID());
        reloadData();
      }
    } else {
      showSelectOfferFirstInfo();
    }
  }

  public void reloadData() throws GenericServiceException {
    offersList.setItems(
      FXCollections.observableArrayList(offerController.offers())
    );
  }

  private void showInfoAlert(OfferDTO offerItem) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION, offerItem.toString());
    alert.showAndWait();
  }
}
