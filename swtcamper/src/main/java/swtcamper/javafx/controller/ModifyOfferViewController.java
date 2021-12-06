package swtcamper.javafx.controller;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.LongStringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.OfferDTO;
import swtcamper.api.controller.OfferController;
import swtcamper.backend.entities.TransmissionType;
import swtcamper.backend.entities.Vehicle;
import swtcamper.backend.entities.VehicleType;
import swtcamper.backend.repositories.VehicleRepository;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class ModifyOfferViewController {

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private OfferController offerController;

  DoubleStringConverter doubleStringConverter = new DoubleStringConverter();

  LongStringConverter longStringConverter = new LongStringConverter();

  @FXML
  public TextField titleTextField;

  @FXML
  public TextField priceTextField;

  @FXML
  public TextField locationTextField;

  @FXML
  public DatePicker availableFromDatePicker;

  @FXML
  public DatePicker availableUntilDatePicker;

  @FXML
  public TextField contactTextField;

  @FXML
  public CheckBox minAgeCheckBox;

  @FXML
  public CheckBox borderCrossingCheckBox;

  @FXML
  public CheckBox depositCheckBox;

  @FXML
  public ComboBox<VehicleType> typeBox;

  @FXML
  public TextField brandTextField;

  @FXML
  public TextField modelTextField;

  @FXML
  public TextField constructionYearTextField;

  @FXML
  public TextField widthTextField;

  @FXML
  public TextField lengthTextField;

  @FXML
  public TextField heightTextField;

  @FXML
  public TextField engineTextField;

  @FXML
  public ComboBox<TransmissionType> transmissionComboBox;

  @FXML
  public TextField seatsTextField;

  @FXML
  public TextField bedsTextField;

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

  @FXML
  public TextField importPath;

  @FXML
  public Button placeOfferButton;

  private long offerID;
  private Vehicle offeredObject;

  @Autowired
  private VehicleRepository vehicleRepository;

  @FXML
  public void initialize() {
    placeOfferButton.setText("Place Offer");
    resetFields();
  }

  @FXML
  public void initialize(OfferDTO offer) {
    placeOfferButton.setText("Update Offer");

    this.offerID = offer.getID();
    this.offeredObject = offer.getOfferedObject();
    Optional<Vehicle> vehicleResponse = vehicleRepository.findById(
      offeredObject.getVehicleID()
    );
    Vehicle vehicle = vehicleResponse.get();

    // TODO implement new fields

    //      titleTextField.setText(offer.getTitle());
    priceTextField.setText(String.valueOf(offer.getPrice()));
    //      locationTextField.setText(offer.getLocation());
    //      availableFromDatePicker.setValue(offer.getAvailableFrom());
    //      availableUntilDatePicker.setValue(offer.getAvailableUntil());
    //      contactTextField.setText(offer.getContact());
    minAgeCheckBox.setSelected(offer.isMinAge25());
    borderCrossingCheckBox.setSelected(offer.isBorderCrossingAllowed());
    depositCheckBox.setSelected(offer.isDepositInCash());
    typeBox.setValue(
      offer.getOfferedObject().getVehicleFeatures().getVehicleType()
    );
    brandTextField.setText(
      offer.getOfferedObject().getVehicleFeatures().getMake()
    );
    modelTextField.setText(
      offer.getOfferedObject().getVehicleFeatures().getModel()
    );
    constructionYearTextField.setText(
      offer.getOfferedObject().getVehicleFeatures().getYear()
    );
    widthTextField.setText(
      String.valueOf(offer.getOfferedObject().getVehicleFeatures().getWidth())
    );
    lengthTextField.setText(
      String.valueOf(offer.getOfferedObject().getVehicleFeatures().getLength())
    );
    heightTextField.setText(
      String.valueOf(offer.getOfferedObject().getVehicleFeatures().getHeight())
    );
    engineTextField.setText(
      offer.getOfferedObject().getVehicleFeatures().getEngine()
    );
    transmissionComboBox.setValue(
      offer
          .getOfferedObject()
          .getVehicleFeatures()
          .getTransmission()
          .equals(TransmissionType.AUTOMATIC.toString())
        ? TransmissionType.AUTOMATIC
        : TransmissionType.MANUAL
    );
    seatsTextField.setText(
      String.valueOf(offer.getOfferedObject().getVehicleFeatures().getSeats())
    );
    bedsTextField.setText(
      String.valueOf(offer.getOfferedObject().getVehicleFeatures().getBeds())
    );
    roofTentCheckBox.setSelected(
      offer.getOfferedObject().getVehicleFeatures().isRoofTent()
    );
    roofRackCheckBox.setSelected(
      offer.getOfferedObject().getVehicleFeatures().isRoofRack()
    );
    bikeRackCheckBox.setSelected(
      offer.getOfferedObject().getVehicleFeatures().isBikeRack()
    );
    showerCheckBox.setSelected(
      offer.getOfferedObject().getVehicleFeatures().isShower()
    );
    toiletCheckBox.setSelected(
      offer.getOfferedObject().getVehicleFeatures().isToilet()
    );
    kitchenUnitCheckBox.setSelected(
      offer.getOfferedObject().getVehicleFeatures().isKitchenUnit()
    );
    fridgeCheckBox.setSelected(
      offer.getOfferedObject().getVehicleFeatures().isFridge()
    );
  }

  private void resetFields() {
    titleTextField.clear();
    priceTextField.clear();
    locationTextField.clear();
    availableFromDatePicker.setValue(LocalDate.now());
    availableUntilDatePicker.setValue(LocalDate.now());
    contactTextField.clear();
    minAgeCheckBox.setSelected(false);
    borderCrossingCheckBox.setSelected(false);
    depositCheckBox.setSelected(false);
    typeBox.setItems(FXCollections.observableArrayList(VehicleType.values()));
    brandTextField.clear();
    modelTextField.clear();
    constructionYearTextField.clear();
    widthTextField.clear();
    lengthTextField.clear();
    heightTextField.clear();
    engineTextField.clear();
    transmissionComboBox.setItems(
      FXCollections.observableArrayList(TransmissionType.values())
    );
    seatsTextField.clear();
    bedsTextField.clear();
    roofTentCheckBox.setSelected(false);
    roofRackCheckBox.setSelected(false);
    bikeRackCheckBox.setSelected(false);
    showerCheckBox.setSelected(false);
    toiletCheckBox.setSelected(false);
    kitchenUnitCheckBox.setSelected(false);
    fridgeCheckBox.setSelected(false);
    importPath.clear();
  }

  @FXML
  public void placeOfferAction() throws GenericServiceException {
    if (placeOfferButton.getText().equals("Place Offer")) {
      String[] pictureURLs = null;
      String[] particularities = null;

      OfferDTO offerDTO = offerController.create(
        // TODO add missing fields
        longStringConverter.fromString(priceTextField.getText()),
        minAgeCheckBox.isSelected(),
        borderCrossingCheckBox.isSelected(),
        depositCheckBox.isSelected(),
        pictureURLs,
        particularities,
        typeBox.getValue(),
        brandTextField.getText(),
        modelTextField.getText(),
        constructionYearTextField.getText(),
        doubleStringConverter.fromString(lengthTextField.getText()),
        doubleStringConverter.fromString(widthTextField.getText()),
        doubleStringConverter.fromString(heightTextField.getText()),
        engineTextField.getText(),
        transmissionComboBox.getValue().toString(),
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

      mainViewController.handleInformationMessage(
        String.format("New offer \"%s\" has been created.", offerDTO.getID())
      );
    }

    if (placeOfferButton.getText().equals("Update Offer")) {
      // TODO entsprechende Felder noch anlegen
      String[] pictureURLs = null;
      String[] particularities = null;
      VehicleType vehicleType = null;
      ArrayList<Long> bookings = null;
      offerController.update(
        offerID,
        offeredObject,
        bookings,
        longStringConverter.fromString(priceTextField.getText()),
        true,
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
        transmissionComboBox.getValue().toString(),
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
    }

    resetFields();
    mainViewController.changeView("activeOffers");
    mainViewController.reloadData();
  }

  @FXML
  public void cancelAction() {
    Alert confirmDelete = new Alert(
      Alert.AlertType.WARNING,
      "Willst du wirklich abbrechen? Alle Änderungen gehen verloren!"
    );
    Optional<ButtonType> result = confirmDelete.showAndWait();

    if (result.isPresent() && result.get() == ButtonType.OK) {
      mainViewController.changeView("activeOffers");
    }
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

  public void importButtonAction() {
    // TODO
    //    String importPathString = importPath.getText();
    //    if (importPathString.isEmpty()) {
    //      mainViewController.handleExceptionMessage(
    //              "The prior selected file path is empty."
    //      );
    //      return;
    //    }
    //    Path path = Paths.get(importPathString);
    //    if (!Files.exists(path)) {
    //      mainViewController.handleExceptionMessage(
    //              String.format(
    //                      "The prior selected file \"%s\" does not exists.",
    //                      path.toAbsolutePath()
    //              )
    //      );
    //      return;
    //    }
    //
    //    String json;
    //    try {
    //      json = Files.readString(path);
    //    } catch (IOException e) {
    //      mainViewController.handleExceptionMessage(
    //              String.format(
    //                      "An error occurred while trying to read file %s",
    //                      path.toAbsolutePath()
    //              )
    //      );
    //      return;
    //    }
    //    try {
    //      mainViewController.handleInformationMessage("The import was successful!");
    //      mainViewController.reloadData();
    //    } catch (Exception e) {
    //      mainViewController.handleException(e);
    //      return;
    //    }
  }
}
