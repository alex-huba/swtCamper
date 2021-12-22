package swtcamper.javafx.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.BookingDTO;
import swtcamper.api.contract.OfferDTO;
import swtcamper.api.controller.BookingController;
import swtcamper.api.controller.OfferController;
import swtcamper.backend.services.exceptions.GenericServiceException;

import java.util.Optional;

@Component
public class MyBookingsViewController {

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private BookingController bookingController;

  @FXML
  public ListView<BookingDTO> bookingsList;


  @FXML
  public void initialize() throws GenericServiceException {
    reloadData();
  }


  public void reloadData() throws GenericServiceException {
    bookingsList.setItems(
      FXCollections.observableArrayList(bookingController.bookings())
    );
  }

  private void showInfoAlert(OfferDTO offerItem) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION, offerItem.toString());
    alert.showAndWait();
  }
}
