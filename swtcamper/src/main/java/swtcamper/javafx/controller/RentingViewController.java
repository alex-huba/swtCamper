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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class RentingViewController {

    @FXML
    public TextField locationTextField;
    @FXML
    public DatePicker pickUpDatePicker;
    @FXML
    public DatePicker returnDatePicker;
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


    @Autowired
    private MainViewController mainViewController;

    @FXML
    public ListView<OfferDTO> offersList;

    private OfferController offerController;

    @FXML
    private void initialize() throws GenericServiceException {
    }

    public void getOffers() throws GenericServiceException {
        offersList.setItems(
                FXCollections.observableArrayList(offerController.offers())
        );
    }

    public void openFilterView() throws GenericServiceException {
        mainViewController.changeView("filterOptions");

        vehicleTypeComboBox.setItems(FXCollections.observableArrayList(VehicleType.values()));
        transmissionComboBox.setItems(FXCollections.observableArrayList(TransmissionType.values()));
    }

    public void startSearch() throws GenericServiceException {
        Filter newFilter = new Filter();
        if(!locationTextField.getText().isEmpty()) newFilter.setLocation(locationTextField.getText());
        if(pickUpDatePicker.getValue() != null) newFilter.setPickUpDate(pickUpDatePicker.getValue());
        if(returnDatePicker.getValue() != null) newFilter.setReturnDate(returnDatePicker.getValue());
        if(vehicleTypeComboBox.getValue() != null) newFilter.setVehicleType(vehicleTypeComboBox.getValue());
        if(!vehicleBrandTextField.getText().isEmpty()) newFilter.setVehicleBrand(vehicleBrandTextField.getText());
        if(!constructionYearTextField.getText().isEmpty()) newFilter.setConstructionYear(Integer.parseInt(constructionYearTextField.getText()));
        if(!maxPricePerDayTextField.getText().isEmpty()) newFilter.setMaxPricePerDay(Integer.parseInt(maxPricePerDayTextField.getText()));
        if(!engineTextField.getText().isEmpty()) newFilter.setEngine(engineTextField.getText());
        if(transmissionComboBox.getValue() != null) newFilter.setTransmissionType(transmissionComboBox.getValue());
        if(!seatAmountTextField.getText().isEmpty()) newFilter.setSeatAmount(Integer.parseInt(seatAmountTextField.getText()));
        if(!bedAmountTextField.getText().isEmpty()) newFilter.setBedAmount(Integer.parseInt(bedAmountTextField.getText()));
        newFilter.setRoofTent(roofTentCheckBox.isSelected());
        newFilter.setRoofRack(roofRackCheckBox.isSelected());
        newFilter.setBikeRack(bikeRackCheckBox.isSelected());
        newFilter.setShower(showerCheckBox.isSelected());
        newFilter.setToilet(toiletCheckBox.isSelected());
        newFilter.setKitchen(kitchenCheckBox.isSelected());
        newFilter.setFridge(fridgeCheckBox.isSelected());

        offersList.setItems(
                FXCollections.observableArrayList(offerController.getFilteredOffers(newFilter))
        );
    }

    public void applyFilters() throws GenericServiceException {
        mainViewController.changeView("home");
    }
}

