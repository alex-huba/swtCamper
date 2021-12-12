package swtcamper.javafx.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
public class ModifyOfferViewController implements EventHandler<KeyEvent> {

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private OfferController offerController;

  DoubleStringConverter doubleStringConverter = new DoubleStringConverter();
  LongStringConverter longStringConverter = new LongStringConverter();

  @FXML
  public VBox offerDetailsVBox;

  @FXML
  public TextField titleTextField;

  @FXML
  public TextField priceTextField;

  @FXML
  public TextField locationTextField;

  @FXML
  public TextField contactTextField;

  @FXML
  public TextArea particularitiesTextArea;

  @FXML
  public CheckBox activeCheckBox;

  @FXML
  public CheckBox minAgeCheckBox;

  @FXML
  public CheckBox borderCrossingCheckBox;

  @FXML
  public CheckBox depositCheckBox;

  @FXML
  public ComboBox<VehicleType> typeBox;

  @FXML
  public Label errorLabel;

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

  boolean isPriceOk = false;
  boolean isBrandOk = false;
  boolean isModelOk = false;
  boolean isSeatsOk = false;
  boolean isBedsOk = false;

  private long offerID;
  private Vehicle offeredObject;

  @Autowired
  private VehicleRepository vehicleRepository;

  private final SimpleBooleanProperty isEditMode = new SimpleBooleanProperty();

  private final Background errorBackground = new Background(
    new BackgroundFill(Color.LIGHTPINK, CornerRadii.EMPTY, Insets.EMPTY)
  );
  private final Background successBackground = new Background(
    new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)
  );

  /**
   * Initialization method for placing a new offer.
   */
  @FXML
  public void initialize() {
    isEditMode.addListener((observable, oldValue, newValue) ->
      placeOfferButton.setText(
        String.format("%s Offer", newValue ? "Update " : "Place")
      )
    );
    isEditMode.set(false);

    offerDetailsVBox.getChildren().remove(activeCheckBox);
    resetFields();

    priceTextField.setOnKeyTyped(this);
    brandTextField.setOnKeyTyped(this);
    modelTextField.setOnKeyTyped(this);
    seatsTextField.setOnKeyTyped(this);
    bedsTextField.setOnKeyTyped(this);
    placeOfferButton.setDisable(true);
  }

  /**
   * Initialization method for updating an existing offer.
   * @param offer Offer that shall get updated
   */
  @FXML
  public void initialize(OfferDTO offer) {
    isEditMode.set(true);
    offerDetailsVBox.getChildren().add(activeCheckBox);

    this.offerID = offer.getID();
    this.offeredObject = offer.getOfferedObject();
    Optional<Vehicle> vehicleResponse = vehicleRepository.findById(
      offeredObject.getVehicleID()
    );
    Vehicle vehicle = vehicleResponse.get();

    titleTextField.setText(offer.getTitle());
    priceTextField.setText(String.valueOf(offer.getPrice()));
    locationTextField.setText(offer.getLocation());
    contactTextField.setText(offer.getContact());
    particularitiesTextArea.setText(offer.getDescription());
    activeCheckBox.setSelected(offer.isActive());
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

  /**
   * Clears all input fields.
   */
  private void resetFields() {
    titleTextField.clear();
    priceTextField.clear();
    locationTextField.clear();
    contactTextField.clear();
    particularitiesTextArea.clear();
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

    isPriceOk = false;
    isBrandOk = false;
    isSeatsOk = false;
    isBedsOk = false;
    isModelOk = false;
    placeOfferButton.setDisable(true);
    errorLabel.setText("");
  }

  @Override
  public void handle(KeyEvent event) {
    validateOfferInput(event);
  }

  /**
   * Places a new offer or updates an existing one, depending on the boolean value of EditMode.
   * @throws GenericServiceException
   */
  @FXML
  public void placeOfferAction() throws GenericServiceException {
    if (!isEditMode.get()) {
      String[] pictureURLs = null;
      String[] particularities = null;

      double length = doubleStringConverter.fromString(
        lengthTextField.getText()
      );
      double width = doubleStringConverter.fromString(widthTextField.getText());
      double height = doubleStringConverter.fromString(
        heightTextField.getText()
      );

      OfferDTO offerDTO = offerController.create(
        titleTextField.getText(),
        locationTextField.getText(),
        contactTextField.getText(),
        particularitiesTextArea.getText(),
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
        length,
        width,
        height,
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

    if (isEditMode.get()) {
      // TODO entsprechende Felder noch anlegen
      String[] pictureURLs = null;
      String[] particularities = null;
      ArrayList<Long> bookings = null;

      offerController.update(
        offerID,
        offeredObject,
        titleTextField.getText(),
        locationTextField.getText(),
        contactTextField.getText(),
        particularitiesTextArea.getText(),
        bookings,
        longStringConverter.fromString(priceTextField.getText()),
        activeCheckBox.isSelected(),
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
    }

    resetFields();
    mainViewController.changeView("activeOffers");
    mainViewController.reloadData();
  }

  /**
   * Cancels the process of placing/ updating an offer and takes the user to the ActiveOffers view.
   * @throws GenericServiceException
   */
  @FXML
  public void cancelAction() throws GenericServiceException {
    Alert confirmDelete = new Alert(
      Alert.AlertType.WARNING,
      "Willst du wirklich abbrechen? Alle Änderungen gehen verloren!"
    );
    Optional<ButtonType> result = confirmDelete.showAndWait();

    if (
      result.isPresent() && result.get() == ButtonType.OK
    ) mainViewController.changeView("activeOffers");
  }

  private void validateOfferInput(KeyEvent event) {
    String source = "";
    source = event.getSource().toString();
    if (source.contains("priceTextField")) {
      source = "price";
    }
    if (source.contains("brandTextField")) {
      source = "brand";
    }
    if (source.contains("modelTextField")) {
      source = "model";
    }
    if (source.contains("seatsTextField")) {
      source = "seats";
    }
    if (source.contains("bedsTextField")) {
      source = "beds";
    }

    switch (source) {
      case "price":
        String inputPrice = priceTextField.getText();
        if (inputPrice.isEmpty() || !inputPrice.matches("[0-9]*")) {
          errorLabel.setText("Invalid price");
          priceTextField.setBackground(errorBackground);
          isPriceOk = false;
        } else {
          errorLabel.setText("");
          priceTextField.setBackground(successBackground);
          isPriceOk = true;
        }
        break;
      case "brand":
        String inputBrand = brandTextField.getText();
        if (inputBrand.isEmpty()) {
          errorLabel.setText("Invalid brand");
          brandTextField.setBackground(errorBackground);
          isBrandOk = false;
        } else {
          errorLabel.setText("");
          brandTextField.setBackground(successBackground);
          isBrandOk = true;
        }
        break;
      case "model":
        String inputModel = modelTextField.getText();
        if (inputModel.isEmpty()) {
          errorLabel.setText("Invalid model");
          modelTextField.setBackground(errorBackground);
          isModelOk = false;
        } else {
          errorLabel.setText("");
          modelTextField.setBackground(successBackground);
          isModelOk = true;
        }
        break;
      case "seats":
        String inputSeats = seatsTextField.getText();
        if (
          inputSeats.isEmpty() ||
          !inputSeats.matches("[0-9]*") ||
          inputSeats.equals("0")
        ) {
          errorLabel.setText("Invalid seat amount");
          seatsTextField.setBackground(errorBackground);
          isSeatsOk = false;
        } else {
          errorLabel.setText("");
          seatsTextField.setBackground(successBackground);
          isSeatsOk = true;
        }
        break;
      case "beds":
        String inputBeds = bedsTextField.getText();
        if (inputBeds.isEmpty() || !inputBeds.matches("[0-9]*")) {
          errorLabel.setText("Invalid beds");
          bedsTextField.setBackground(errorBackground);
          isBedsOk = false;
        } else {
          errorLabel.setText("");
          bedsTextField.setBackground(successBackground);
          isBedsOk = true;
        }
        break;
    }
    placeOfferButton.setDisable(
      !(
        isPriceOk &&
        !isBrandOk &&
        !isModelOk &&
        !isSeatsOk &&
        !isBedsOk &&
        !titleTextField.getText().isEmpty() &&
        !locationTextField.getText().isEmpty() &&
        !contactTextField.getText().isEmpty() &&
        typeBox.getValue() != null &&
        transmissionComboBox.getValue() != null
      )
    );
  }

  /**
   * Imports the path of a file from the filesystem.
   * @param event ActionEvent from FXML to determine the pressed button
   */
  public void importFileChooserAction(ActionEvent event) {
    Node source = (Node) event.getSource();
    Window window = source.getScene().getWindow();

    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Resource File");
    File file = fileChooser.showOpenDialog(window);
    if (file == null) return;
    importPath.setText(file.getAbsolutePath().toString());
  }

  /**
   * Uploads one or more before selected pictures.
   */
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
