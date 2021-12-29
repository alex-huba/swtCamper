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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.LongStringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.OfferDTO;
import swtcamper.api.controller.OfferController;
import swtcamper.api.controller.ValidationHelper;
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

  @Autowired
  private ValidationHelper validationHelper;

  DoubleStringConverter doubleStringConverter = new DoubleStringConverter();
  LongStringConverter longStringConverter = new LongStringConverter();

  @FXML
  public BorderPane offerDetailsMainView;

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
  public ComboBox<VehicleType> vehicleTypeComboBox;

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

  SimpleBooleanProperty isPriceOk = new SimpleBooleanProperty();
  SimpleBooleanProperty isBrandOk = new SimpleBooleanProperty();
  SimpleBooleanProperty isModelOk = new SimpleBooleanProperty();
  SimpleBooleanProperty isSeatsOk = new SimpleBooleanProperty();
  SimpleBooleanProperty isBedsOk = new SimpleBooleanProperty();
  SimpleBooleanProperty isTitleOk = new SimpleBooleanProperty();
  SimpleBooleanProperty isLocationOk = new SimpleBooleanProperty();
  SimpleBooleanProperty isContactOk = new SimpleBooleanProperty();
  SimpleBooleanProperty isVehicleTypeOk = new SimpleBooleanProperty();
  SimpleBooleanProperty isTransmissionTypeOk = new SimpleBooleanProperty();

  private long offerID;
  private Vehicle offeredObject;

  @Autowired
  private VehicleRepository vehicleRepository;

  private final SimpleBooleanProperty isEditMode = new SimpleBooleanProperty();

  private final Background errorBackground = new Background(
    new BackgroundFill(Color.LIGHTPINK, CornerRadii.EMPTY, Insets.EMPTY)
  );
  private final Background neutralBackground = new Background(
    new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)
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
        String.format("Anzeige %s", newValue ? "bearbeiten " : "erstellen")
      )
    );
    isEditMode.set(false);
    activeCheckBox.visibleProperty().bind(isEditMode);

    resetFields();

    priceTextField.setOnKeyTyped(this);
    brandTextField.setOnKeyTyped(this);
    modelTextField.setOnKeyTyped(this);
    seatsTextField.setOnKeyTyped(this);
    bedsTextField.setOnKeyTyped(this);
    titleTextField.setOnKeyTyped(this);
    locationTextField.setOnKeyTyped(this);
    contactTextField.setOnKeyTyped(this);

    vehicleTypeComboBox
      .getSelectionModel()
      .selectedItemProperty()
      .addListener((options, oldValue, newValue) -> {
        validateVehicleType(newValue);
      });
    transmissionComboBox
      .getSelectionModel()
      .selectedItemProperty()
      .addListener((options, oldValue, newValue) -> {
        validateTransmissionType(newValue);
      });

    placeOfferButton
      .disableProperty()
      .bind(
        isPriceOk
          .and(isBrandOk)
          .and(isModelOk)
          .and(isSeatsOk)
          .and(isBedsOk)
          .and(isTitleOk)
          .and(isLocationOk)
          .and(isContactOk)
          .and(isVehicleTypeOk)
          .and(isTransmissionTypeOk)
          .not()
      );
  }

  /**
   * Initialization method for updating an existing offer.
   *
   * @param offer Offer that shall get updated
   */
  @FXML
  public void initialize(OfferDTO offer) {
    isEditMode.set(true);

    this.offerID = offer.getID();
    this.offeredObject = offer.getOfferedObject();
    Vehicle vehicle = vehicleRepository
      .findById(offeredObject.getVehicleID())
      .orElse(null);

    // fill in values from selected offer
    titleTextField.setText(offer.getTitle());
    priceTextField.setText(String.valueOf(offer.getPrice()));
    locationTextField.setText(offer.getLocation());
    contactTextField.setText(offer.getContact());
    particularitiesTextArea.setText(offer.getParticularities());
    activeCheckBox.setSelected(offer.isActive());
    minAgeCheckBox.setSelected(offer.isMinAge25());
    borderCrossingCheckBox.setSelected(offer.isBorderCrossingAllowed());
    depositCheckBox.setSelected(offer.isDepositInCash());

    assert vehicle != null;
    vehicleTypeComboBox.setValue(vehicle.getVehicleFeatures().getVehicleType());
    brandTextField.setText(vehicle.getVehicleFeatures().getMake());
    modelTextField.setText(vehicle.getVehicleFeatures().getModel());
    constructionYearTextField.setText(vehicle.getVehicleFeatures().getYear());
    widthTextField.setText(
      String.valueOf(vehicle.getVehicleFeatures().getWidth())
    );
    lengthTextField.setText(
      String.valueOf(vehicle.getVehicleFeatures().getLength())
    );
    heightTextField.setText(
      String.valueOf(vehicle.getVehicleFeatures().getHeight())
    );
    engineTextField.setText(vehicle.getVehicleFeatures().getEngine());
    transmissionComboBox.setValue(
      vehicle
          .getVehicleFeatures()
          .getTransmission()
          .equals(TransmissionType.AUTOMATIC.toString())
        ? TransmissionType.AUTOMATIC
        : TransmissionType.MANUAL
    );
    seatsTextField.setText(
      String.valueOf(vehicle.getVehicleFeatures().getSeats())
    );
    bedsTextField.setText(
      String.valueOf(vehicle.getVehicleFeatures().getBeds())
    );
    roofTentCheckBox.setSelected(vehicle.getVehicleFeatures().isRoofTent());
    roofRackCheckBox.setSelected(vehicle.getVehicleFeatures().isRoofRack());
    bikeRackCheckBox.setSelected(vehicle.getVehicleFeatures().isBikeRack());
    showerCheckBox.setSelected(vehicle.getVehicleFeatures().isShower());
    toiletCheckBox.setSelected(vehicle.getVehicleFeatures().isToilet());
    kitchenUnitCheckBox.setSelected(
      vehicle.getVehicleFeatures().isKitchenUnit()
    );
    fridgeCheckBox.setSelected(vehicle.getVehicleFeatures().isFridge());

    validateMandatoryFields();
  }

  /**
   * Clears all input fields.
   */
  private void resetFields() {
    // clear input fields
    titleTextField.clear();
    priceTextField.clear();
    locationTextField.clear();
    contactTextField.clear();
    particularitiesTextArea.clear();
    minAgeCheckBox.setSelected(false);
    borderCrossingCheckBox.setSelected(false);
    depositCheckBox.setSelected(false);
    vehicleTypeComboBox.setItems(
      FXCollections.observableArrayList(VehicleType.values())
    );
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

    // resets all backgrounds to neutral
    // mandatory fields
    titleTextField.setStyle("");
    priceTextField.setStyle("");
    locationTextField.setStyle("");
    contactTextField.setStyle("");
    brandTextField.setStyle("");
    modelTextField.setStyle("");
    seatsTextField.setStyle("");
    bedsTextField.setStyle("");

    // reset validated properties
    isTitleOk.set(false);
    isPriceOk.set(false);
    isLocationOk.set(false);
    isContactOk.set(false);
    isBrandOk.set(false);
    isModelOk.set(false);
    isSeatsOk.set(false);
    isBedsOk.set(false);

    errorLabel.setText("");
  }

  @Override
  public void handle(KeyEvent event) {
    validateOfferInput(event);
  }

  /**
   * Places a new offer or updates an existing one, depending on the boolean value of EditMode.
   *
   * @throws GenericServiceException
   */
  @FXML
  public void placeOfferAction() throws GenericServiceException {
    double length = !lengthTextField.getText().isEmpty()
      ? doubleStringConverter.fromString(lengthTextField.getText())
      : 0;
    double width = !widthTextField.getText().isEmpty()
      ? doubleStringConverter.fromString(widthTextField.getText())
      : 0;
    double height = !heightTextField.getText().isEmpty()
      ? doubleStringConverter.fromString(heightTextField.getText())
      : 0;

    if (!isEditMode.get()) {
      String[] pictureURLs = null;

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
        vehicleTypeComboBox.getValue(),
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
        String.format("Neues Angebot \"%s\" wurde erstellt.", offerDTO.getID())
      );
    } else if (isEditMode.get()) {
      String[] pictureURLs = null;
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
        vehicleTypeComboBox.getValue(),
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
    }

    resetFields();
    mainViewController.changeView("activeOffers");
    mainViewController.reloadData();
  }

  /**
   * Cancels the process of placing/ updating an offer and takes the user to the ActiveOffers view.
   *
   * @throws GenericServiceException
   */
  @FXML
  public void cancelAction() throws GenericServiceException {
    Alert confirmDelete = new Alert(
      Alert.AlertType.CONFIRMATION,
      "Willst du wirklich abbrechen? Alle Änderungen gehen verloren!"
    );
    Optional<ButtonType> result = confirmDelete.showAndWait();

    if (result.isPresent() && result.get() == ButtonType.OK) {
      resetFields();
      mainViewController.changeView("activeOffers");
    }
  }

  private void validateOfferInput(KeyEvent event) {
    Object source = event.getSource();

    if (titleTextField.equals(source)) {
      String inputTitle = titleTextField.getText();
      validateTitle(inputTitle);
    } else if (priceTextField.equals(source)) {
      String inputPrice = priceTextField.getText();
      validatePrice(inputPrice);
    } else if (locationTextField.equals(source)) {
      String inputLocation = locationTextField.getText();
      validateLocation(inputLocation);
    } else if (contactTextField.equals(source)) {
      String inputContact = contactTextField.getText();
      validateContact(inputContact);
    } else if (brandTextField.equals(source)) {
      String inputBrand = brandTextField.getText();
      validateBrand(inputBrand);
    } else if (modelTextField.equals(source)) {
      String inputModel = modelTextField.getText();
      validateModel(inputModel);
    } else if (seatsTextField.equals(source)) {
      String inputSeats = seatsTextField.getText();
      validateSeats(inputSeats);
    } else if (bedsTextField.equals(source)) {
      String inputBeds = bedsTextField.getText();
      validateBeds(inputBeds);
    }
  }

  private void validateMandatoryFields() {
    validateTitle(titleTextField.getText());
    validatePrice(priceTextField.getText());
    validateLocation(locationTextField.getText());
    validateContact(contactTextField.getText());
    validateVehicleType(vehicleTypeComboBox.getValue());
    validateBrand(brandTextField.getText());
    validateModel(modelTextField.getText());
    validateTransmissionType(transmissionComboBox.getValue());
    validateSeats(seatsTextField.getText());
    validateBeds(bedsTextField.getText());
  }

  private void validateTrue(Node element) {
    element.setStyle("-fx-background-color: #198754; -fx-text-fill: #FFFFFF");
  }

  private void validateFalse(Node element) {
    element.setStyle("-fx-background-color: #dc3545; -fx-text-fill: #FFFFFF");
  }

  private void validateTitle(String inputTitle) {
    //if (inputTitle.isEmpty() || inputTitle.length() < 5) {
    if (!validationHelper.checkOfferTitle(inputTitle)) {
      errorLabel.setText("Invalid title");
      validateFalse(titleTextField);
      isTitleOk.set(false);
    } else {
      errorLabel.setText("");
      validateTrue(titleTextField);
      isTitleOk.set(true);
    }
  }

  private void validatePrice(String inputPrice) {
    /*if (
      inputPrice.isEmpty() ||
      !inputPrice.matches("[0-9]*") ||
      Integer.parseInt(inputPrice) <= 0
    ) */
    if (!validationHelper.checkOfferPrice(inputPrice)) {
      errorLabel.setText("Ungültiger Preis");
      validateFalse(priceTextField);
      isPriceOk.set(false);
    } else {
      errorLabel.setText("");
      validateTrue(priceTextField);
      isPriceOk.set(true);
    }
  }

  private void validateLocation(String inputLocation) {
    if (inputLocation.isEmpty() || inputLocation.length() < 3) {
      errorLabel.setText("Ungültiger Abholort");
      validateFalse(locationTextField);
      isLocationOk.set(false);
    } else {
      errorLabel.setText("");
      validateTrue(locationTextField);
      isLocationOk.set(true);
    }
  }

  private void validateContact(String inputContact) {
    if (inputContact.isEmpty() || inputContact.length() < 5) {
      errorLabel.setText("Ungültiger Kontakt");
      validateFalse(contactTextField);
      isContactOk.set(false);
    } else {
      errorLabel.setText("");
      validateTrue(contactTextField);
      isContactOk.set(true);
    }
  }

  private void validateVehicleType(VehicleType inputVehicleType) {
    if (inputVehicleType == null) {
      errorLabel.setText("Ungültiger Fahrzeugtyp");
      validateFalse(vehicleTypeComboBox);
      isVehicleTypeOk.set(false);
    } else {
      errorLabel.setText("");
      validateTrue(vehicleTypeComboBox);
      isVehicleTypeOk.set(true);
    }
  }

  private void validateBrand(String inputBrand) {
    if (inputBrand.isEmpty() || inputBrand.length() < 2) {
      errorLabel.setText("Ungültiger Hersteller");
      validateFalse(brandTextField);
      isBrandOk.set(false);
    } else {
      errorLabel.setText("");
      validateTrue(brandTextField);
      isBrandOk.set(true);
    }
  }

  private void validateModel(String inputModel) {
    if (inputModel.isEmpty() || inputModel.length() < 2) {
      errorLabel.setText("Ungültiges Modell");
      validateFalse(modelTextField);
      isModelOk.set(false);
    } else {
      errorLabel.setText("");
      validateTrue(modelTextField);
      isModelOk.set(true);
    }
  }

  private void validateTransmissionType(
    TransmissionType inputTransmissionType
  ) {
    if (inputTransmissionType == null) {
      errorLabel.setText("Ungültige Schaltung");
      validateFalse(transmissionComboBox);
      isTransmissionTypeOk.set(false);
    } else {
      errorLabel.setText("");
      validateTrue(transmissionComboBox);
      isTransmissionTypeOk.set(true);
    }
  }

  private void validateSeats(String inputSeats) {
    if (
      inputSeats.isEmpty() ||
      !inputSeats.matches("[0-9]*") ||
      Integer.parseInt(inputSeats) == 0
    ) {
      errorLabel.setText("Ungültige Anzahl von Sitzplätze");
      validateFalse(seatsTextField);
      isSeatsOk.set(false);
    } else {
      errorLabel.setText("");
      validateTrue(seatsTextField);
      isSeatsOk.set(true);
    }
  }

  private void validateBeds(String inputBeds) {
    if (inputBeds.isEmpty() || !inputBeds.matches("[0-9]*")) {
      errorLabel.setText("Ungültige Anzahl von Betten");
      validateFalse(bedsTextField);
      isBedsOk.set(false);
    } else {
      errorLabel.setText("");
      validateTrue(bedsTextField);
      isBedsOk.set(true);
    }
  }

  /**
   * Imports the path of a file from the filesystem.
   *
   * @param event ActionEvent from FXML to determine the pressed button
   */
  public void importFileChooserAction(ActionEvent event) {
    Node source = (Node) event.getSource();
    Window window = source.getScene().getWindow();

    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Ressourcendatei öffnen");
    File file = fileChooser.showOpenDialog(window);
    if (file == null) return;
    importPath.setText(file.getAbsolutePath().toString());
  }

  /**
   * Uploads one or more before selected pictures.
   */
  public void importButtonAction() {}
}
