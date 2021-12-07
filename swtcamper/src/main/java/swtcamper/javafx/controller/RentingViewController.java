package swtcamper.javafx.controller;

import java.util.Arrays;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.OfferDTO;
import swtcamper.api.controller.OfferController;
import swtcamper.backend.entities.Filter;
import swtcamper.backend.entities.TransmissionType;
import swtcamper.backend.entities.VehicleType;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class RentingViewController {

  @FXML
  public TextField locationTextField;

  @FXML
  public TextField keywordTextField;

  @FXML
  public ComboBox<VehicleType> vehicleTypeComboBox;

  @FXML
  public TextField vehicleBrandTextField;

  @FXML
  public TextField constructionYearTextField;

  @FXML
  public TextField maxPricePerDayTextField;

  @FXML
  public TextField engineTextField;

  @FXML
  public ComboBox<TransmissionType> transmissionComboBox;

  @FXML
  public TextField seatAmountTextField;

  @FXML
  public TextField bedAmountTextField;

  @FXML
  public CheckBox roofTentCheckBox;

  @FXML
  public CheckBox roofRackCheckBox;

  @FXML
  public CheckBox bikeRackCheckBox;

  @FXML
  public CheckBox showerCheckBox;

  @FXML
  public CheckBox toiletCheckBox;

  @FXML
  public CheckBox kitchenCheckBox;

  @FXML
  public CheckBox fridgeCheckBox;

  @FXML
  public ListView<OfferDTO> offersList;

  @Autowired
  private OfferController offerController;

  @FXML
  private void initialize() throws GenericServiceException {
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

  public void reloadData() throws GenericServiceException {
    offersList.setItems(
      FXCollections.observableArrayList(offerController.offers())
    );

    vehicleTypeComboBox.setItems(
      FXCollections.observableArrayList(VehicleType.values())
    );
    transmissionComboBox.setItems(
      FXCollections.observableArrayList(TransmissionType.values())
    );
  }

  private void showInfoAlert(OfferDTO offerItem) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION, offerItem.toString());
    alert.showAndWait();
  }

  public void startSearch() throws GenericServiceException {
    Filter newFilter = new Filter();
    if (!locationTextField.getText().isEmpty()) newFilter.setLocation(
      locationTextField.getText()
    );
    //    if (!keywordTextField.getText().isEmpty()) newFilter.setKeywords(
    //      keywordTextField.getText().split(",")
    //    );
    if (vehicleTypeComboBox.getValue() != null) newFilter.setVehicleType(
      vehicleTypeComboBox.getValue()
    );
    if (!vehicleBrandTextField.getText().isEmpty()) newFilter.setVehicleBrand(
      vehicleBrandTextField.getText()
    );
    if (
      !constructionYearTextField.getText().isEmpty()
    ) newFilter.setConstructionYear(
      Integer.parseInt(constructionYearTextField.getText())
    );
    if (
      !maxPricePerDayTextField.getText().isEmpty()
    ) newFilter.setMaxPricePerDay(
      Integer.parseInt(maxPricePerDayTextField.getText())
    );
    if (!engineTextField.getText().isEmpty()) newFilter.setEngine(
      engineTextField.getText()
    );
    if (transmissionComboBox.getValue() != null) newFilter.setTransmissionType(
      transmissionComboBox.getValue()
    );
    if (!seatAmountTextField.getText().isEmpty()) newFilter.setSeatAmount(
      Integer.parseInt(seatAmountTextField.getText())
    );
    if (!bedAmountTextField.getText().isEmpty()) newFilter.setBedAmount(
      Integer.parseInt(bedAmountTextField.getText())
    );
    newFilter.setRoofTent(roofTentCheckBox.isSelected());
    newFilter.setRoofRack(roofRackCheckBox.isSelected());
    newFilter.setBikeRack(bikeRackCheckBox.isSelected());
    newFilter.setShower(showerCheckBox.isSelected());
    newFilter.setToilet(toiletCheckBox.isSelected());
    newFilter.setKitchen(kitchenCheckBox.isSelected());
    newFilter.setFridge(fridgeCheckBox.isSelected());

    offersList.setItems(
      FXCollections.observableArrayList(
        offerController.getFilteredOffers(newFilter)
      )
    );
  }
}
