package swtcamper.javafx.controller;

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

  @Autowired
  private OfferController offerController;

  @FXML
  public TitledPane searchBoxTitledPane;

  @FXML
  public TextField locationTextField;

  @FXML
  public ComboBox<VehicleType> vehicleTypeComboBox;

  @FXML
  public Button resetVehicleTypeBtn;

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
  public Button resetTransmissionTypeBtn;

  @FXML
  public TextField seatAmountTextField;

  @FXML
  public TextField bedAmountTextField;

  @FXML
  public CheckBox excludeInactiveCheckBox;

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
  public CheckBox min21CheckBox;

  @FXML
  public CheckBox crossBordersCheckBox;

  @FXML
  public CheckBox payCashCheckBox;

  @FXML
  public ListView<OfferDTO> offersList;

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

    searchBoxTitledPane.setExpanded(false);
    excludeInactiveCheckBox.setSelected(true);
    vehicleTypeComboBox.setItems(
      FXCollections.observableArrayList(VehicleType.values())
    );
    vehicleTypeComboBox.setButtonCell(
      new ListCell<>() {
        @Override
        protected void updateItem(VehicleType item, boolean empty) {
          super.updateItem(item, empty);
          if (empty || item == null) {
            setText(vehicleTypeComboBox.getPromptText());
          } else {
            setText(item.toString());
          }
        }
      }
    );
    resetVehicleTypeBtn
      .visibleProperty()
      .bind(vehicleTypeComboBox.valueProperty().isNotNull());
    transmissionComboBox.setItems(
      FXCollections.observableArrayList(TransmissionType.values())
    );
    transmissionComboBox.setButtonCell(
      new ListCell<>() {
        @Override
        protected void updateItem(TransmissionType item, boolean empty) {
          super.updateItem(item, empty);
          if (empty || item == null) {
            setText(transmissionComboBox.getPromptText());
          } else {
            setText(item.toString());
          }
        }
      }
    );
    resetTransmissionTypeBtn
      .visibleProperty()
      .bind(transmissionComboBox.valueProperty().isNotNull());
  }

  /**
   * Gets all available offers from the database .
   * @throws GenericServiceException
   */
  public void reloadData() throws GenericServiceException {
    offersList.setItems(
      FXCollections.observableArrayList(offerController.offers())
    );
  }

  private void showInfoAlert(OfferDTO offerItem) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION, offerItem.toString());
    alert.showAndWait();
  }

  /**
   * Creates a new Filter Object and gets all offers from the database that fit to this filter.
   * @throws GenericServiceException
   */
  public void startSearch() throws GenericServiceException {
    Filter newFilter = new Filter();
    if (!locationTextField.getText().isEmpty()) newFilter.setLocation(
      locationTextField.getText()
    );
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
    newFilter.setExcludeInactive(excludeInactiveCheckBox.isSelected());
    newFilter.setRoofTent(roofTentCheckBox.isSelected());
    newFilter.setRoofRack(roofRackCheckBox.isSelected());
    newFilter.setBikeRack(bikeRackCheckBox.isSelected());
    newFilter.setShower(showerCheckBox.isSelected());
    newFilter.setToilet(toiletCheckBox.isSelected());
    newFilter.setKitchen(kitchenCheckBox.isSelected());
    newFilter.setFridge(fridgeCheckBox.isSelected());

    newFilter.setMinAge21(min21CheckBox.isSelected());
    newFilter.setCrossingBordersAllowed(crossBordersCheckBox.isSelected());
    newFilter.setDepositInCash(payCashCheckBox.isSelected());

    offersList.setItems(
      FXCollections.observableArrayList(
        offerController.getFilteredOffers(newFilter)
      )
    );
  }

  /**
   * Resets VehicleTypeComboBox to its initial state.
   */
  public void resetVehicleTypeComboBox() {
    vehicleTypeComboBox.valueProperty().set(null);
  }

  /**
   * Resets TransmissionTypeComboBox to its initial state.
   */
  public void resetTransmissionTypeComboBox() {
    transmissionComboBox.valueProperty().set(null);
  }
}
