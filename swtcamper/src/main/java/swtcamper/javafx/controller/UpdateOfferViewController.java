package swtcamper.javafx.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

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
import swtcamper.backend.entities.Offer;
import swtcamper.backend.entities.Vehicle;
import swtcamper.backend.entities.VehicleType;
import swtcamper.backend.repositories.VehicleRepository;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class UpdateOfferViewController {

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private OfferController offerController;

  @Autowired
  private VehicleRepository vehicleRepository;

  DoubleStringConverter doubleStringConverter = new DoubleStringConverter();

  LongStringConverter longStringConverter = new LongStringConverter();

  private long offerID;

  private long offeredObjectID;

  @FXML
  public ComboBox typeBox;

  @FXML
  public TextField brandTextField;

  @FXML
  public TextField modelTextField;

  @FXML
  public TextField constructionYearTextField;

  @FXML
  public CheckBox minAgeCheckBox;

  @FXML
  public CheckBox borderCrossingCheckBox;

  @FXML
  public CheckBox depositCheckBox;

  @FXML
  public TextField priceTextField;

  @FXML
  public CheckBox activeCheckBox;

  @FXML
  public TextField widthTextField;

  @FXML
  public TextField lengthTextField;

  @FXML
  public TextField heightTextField;

  @FXML
  public TextField engineTextField;

  @FXML
  public TextField transmissionTextField;

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
  public TextField contactTextField;

  @FXML
  public TextField locationTextField;

  @FXML
  public TextField descriptionTextField;*/

  @FXML
  public TextField seatsTextField;

  @FXML
  public TextField bedsTextField;

  public void initialize(OfferDTO offer) {
    this.offerID = offer.getID();
    this.offeredObjectID = offer.getOfferedObjectID();
    Optional<Vehicle> vehicleResponse = vehicleRepository.findById(offeredObjectID);
    Vehicle vehicle = vehicleResponse.get();
    // TODO felder füllen
    this.brandTextField.setText(vehicle.getVehicleFeatures().getMake());
    this.modelTextField.setText(vehicle.getVehicleFeatures().getModel());
    this.constructionYearTextField.setText(vehicle.getVehicleFeatures().getYear());
    this.minAgeCheckBox.setSelected(offer.isMinAge25());
    this.borderCrossingCheckBox.setSelected(offer.isBorderCrossingAllowed());
    this.depositCheckBox.setSelected(offer.isDepositInCash());
    this.priceTextField.setText(longStringConverter.toString(offer.getPrice()));
    this.activeCheckBox.setSelected(offer.isActive());
    this.widthTextField.setText(doubleStringConverter.toString(vehicle.getVehicleFeatures().getWidth()));
    this.lengthTextField.setText(doubleStringConverter.toString(vehicle.getVehicleFeatures().getLength()));
    this.heightTextField.setText(doubleStringConverter.toString(vehicle.getVehicleFeatures().getHeight()));
    this.engineTextField.setText(vehicle.getVehicleFeatures().getEngine());
    this.transmissionTextField.setText(vehicle.getVehicleFeatures().getTransmission());
    this.roofTentCheckBox.setSelected(vehicle.getVehicleFeatures().isRoofTent());
    this.roofRackCheckBox.setSelected(vehicle.getVehicleFeatures().isRoofRack());
    this.bikeRackCheckBox.setSelected(vehicle.getVehicleFeatures().isBikeRack());
    this.showerCheckBox.setSelected(vehicle.getVehicleFeatures().isShower());
    this.toiletCheckBox.setSelected(vehicle.getVehicleFeatures().isToilet());
    this.kitchenUnitCheckBox.setSelected(vehicle.getVehicleFeatures().isKitchenUnit());
    this.fridgeCheckBox.setSelected(vehicle.getVehicleFeatures().isFridge());
    this.seatsTextField.setText(Integer.toString(vehicle.getVehicleFeatures().getSeats()));
    this.bedsTextField.setText(Integer.toString(vehicle.getVehicleFeatures().getBeds()));
  }

  @FXML
  public void updateOfferAction() throws GenericServiceException {
    // TODO entsprechende Felder noch anlegen
    String[] pictureURLs = null;
    String[] particularities = null;
    VehicleType vehicleType = null;
    ArrayList<Long> bookings = null;
    offerController.update(
            offerID,
            offeredObjectID,
            bookings,
            longStringConverter.fromString(priceTextField.getText()),
            activeCheckBox.isSelected(),
            minAgeCheckBox.isSelected(),
            borderCrossingCheckBox.isSelected(),
            depositCheckBox.isSelected(),
            pictureURLs,
            particularities,
            vehicleType,
            brandTextField.getText(),
            modelTextField.getText(),
            constructionYearTextField.getText(),
            doubleStringConverter.fromString(lengthTextField.getText()),
            doubleStringConverter.fromString(widthTextField.getText()),
            doubleStringConverter.fromString(heightTextField.getText()),
            engineTextField.getText(),
            transmissionTextField.getText(),
            Integer.parseInt(seatsTextField.getText()),
            Integer.parseInt(bedsTextField.getText()),
            roofTentCheckBox.isSelected(),
            roofRackCheckBox.isSelected(),
            bikeRackCheckBox.isSelected(),
            showerCheckBox.isSelected(),
            toiletCheckBox.isSelected(),
            kitchenUnitCheckBox.isSelected(),
            fridgeCheckBox.isSelected()
    );
    mainViewController.updateOfferTab.setDisable(true);
    mainViewController.jumpToTab("offers");
    mainViewController.reloadData();
  }

  @FXML
  public void cancelUpdateAction() {
    mainViewController.jumpToTab("offers");
    mainViewController.updateOfferTab.setDisable(true);
  }

  public void importFileChooserAction(ActionEvent event) {
    Node source = (Node) event.getSource();
    Window window = source.getScene().getWindow();

    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Resource File");
    File file = fileChooser.showOpenDialog(window);
    if (file == null) {
      return;
    }
    importPath.setText(file.getAbsolutePath().toString());
  }

  @FXML
  public TextField importPath;

  public void importButtonAction() {
    String importPathString = importPath.getText();
    if (importPathString.isEmpty()) {
      mainViewController.handleExceptionMessage(
        "The prior selected file path is empty."
      );
      return;
    }
    Path path = Paths.get(importPathString);
    if (!Files.exists(path)) {
      mainViewController.handleExceptionMessage(
        String.format(
          "The prior selected file \"%s\" does not exists.",
          path.toAbsolutePath()
        )
      );
      return;
    }

    String json;
    try {
      json = Files.readString(path);
    } catch (IOException e) {
      mainViewController.handleExceptionMessage(
        String.format(
          "An error occurred while trying to read file %s",
          path.toAbsolutePath()
        )
      );
      return;
    }
    try {
      mainViewController.handleInformationMessage("The import was successful!");
      mainViewController.reloadData();
    } catch (Exception e) {
      mainViewController.handleException(e);
      return;
    }
  }
}
