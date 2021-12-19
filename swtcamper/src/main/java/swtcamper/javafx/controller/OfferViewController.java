package swtcamper.javafx.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.LongStringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.OfferDTO;
import swtcamper.api.controller.OfferController;
import swtcamper.backend.entities.Vehicle;
import swtcamper.backend.entities.VehicleType;
import swtcamper.backend.repositories.VehicleRepository;
import swtcamper.backend.services.exceptions.GenericServiceException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

@Component
public class OfferViewController {

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private OfferController offerController;

  @Autowired
  private VehicleRepository vehicleRepository;

  private long offerID;

  private Vehicle offeredObject;

  LongStringConverter longStringConverter = new LongStringConverter();

  DoubleStringConverter doubleStringConverter = new DoubleStringConverter();

  @FXML
  public Label typeLabel;

  @FXML
  public Label brandLabel;

  @FXML
  public Label modelLabel;

  @FXML
  public Label constructionYearLabel;

  @FXML
  public CheckBox minAgeCheckBox;

  @FXML
  public CheckBox borderCrossingCheckBox;

  @FXML
  public CheckBox depositCheckBox;

  @FXML
  public Label priceLabel;

  @FXML
  public Label widthLabel;

  @FXML
  public Label lengthLabel;

  @FXML
  public Label heightLabel;

  @FXML
  public Label engineLabel;

  @FXML
  public Label transmissionLabel;

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
  public CheckBox kitchenUnitCheckBox;

  @FXML
  public CheckBox fridgeCheckBox;

  /*@FXML
  public Label contactLabel;

  @FXML
  public Label locationLabel;

  @FXML
  public Label descriptionLabel;*/

  @FXML
  public Label seatsLabel;

  @FXML
  public Label bedsLabel;

  public void initialize(OfferDTO offer) {
    this.offerID = offer.getID();
    this.offeredObject = offer.getOfferedObject();
    Optional<Vehicle> vehicleResponse = vehicleRepository.findById(
      offeredObject.getVehicleID()
    );
    Vehicle vehicle = vehicleResponse.get();
    // TODO felder füllen
   /* this.typeBox.setItems(
        FXCollections.observableArrayList(
          vehicle.getVehicleFeatures().getVehicleType()
        )
      );*/
    brandLabel.setText("Brand: " + vehicle.getVehicleFeatures().getMake());
    modelLabel.setText("Model: " + vehicle.getVehicleFeatures().getModel());
    constructionYearLabel.setText( "Year of construction: " +
        vehicle.getVehicleFeatures().getYear()
      );
    minAgeCheckBox.setSelected(offer.isMinAge25());
    borderCrossingCheckBox.setSelected(offer.isBorderCrossingAllowed());
    depositCheckBox.setSelected(offer.isDepositInCash());
    priceLabel.setText("Price per night: " + longStringConverter.toString(offer.getPrice()));
    widthLabel.setText("Width: " +
        doubleStringConverter.toString(vehicle.getVehicleFeatures().getWidth())
      );
    lengthLabel.setText("Length: " +
        doubleStringConverter.toString(vehicle.getVehicleFeatures().getLength())
      );
    heightLabel.setText("Height: " +
        doubleStringConverter.toString(vehicle.getVehicleFeatures().getHeight())
      );
    engineLabel.setText("Engine: " + vehicle.getVehicleFeatures().getEngine());
    transmissionLabel.setText("Transmission: " +
        vehicle.getVehicleFeatures().getTransmission()
      );
    roofTentCheckBox.setSelected(
        vehicle.getVehicleFeatures().isRoofTent()
      );
    roofRackCheckBox.setSelected(
        vehicle.getVehicleFeatures().isRoofRack()
      );
    bikeRackCheckBox.setSelected(
        vehicle.getVehicleFeatures().isBikeRack()
      );
    showerCheckBox.setSelected(vehicle.getVehicleFeatures().isShower());
    toiletCheckBox.setSelected(vehicle.getVehicleFeatures().isToilet());
    kitchenUnitCheckBox.setSelected(
        vehicle.getVehicleFeatures().isKitchenUnit()
      );
    fridgeCheckBox.setSelected(vehicle.getVehicleFeatures().isFridge());
    seatsLabel.setText("Seats: " +
        vehicle.getVehicleFeatures().getSeats()
      );
    bedsLabel.setText("Beds: " +
        vehicle.getVehicleFeatures().getBeds()
      );
  }


  @FXML
  public void backAction() throws GenericServiceException {

      mainViewController.changeView("activeOffers");

  }



}
