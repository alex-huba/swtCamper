package swtcamper.javafx.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.Pair;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.LongStringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.OfferDTO;
import swtcamper.api.contract.PictureDTO;
import swtcamper.api.contract.interfaces.IOfferController;
import swtcamper.api.contract.interfaces.IPictureController;
import swtcamper.api.contract.interfaces.IUserController;
import swtcamper.api.controller.ValidationHelper;
import swtcamper.backend.entities.*;
import swtcamper.backend.repositories.VehicleRepository;
import swtcamper.backend.services.BookingService;
import swtcamper.backend.services.OfferService;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class ModifyOfferViewController implements EventHandler<KeyEvent> {

  private final DoubleStringConverter doubleStringConverter = new DoubleStringConverter();
  private final LongStringConverter longStringConverter = new LongStringConverter();
  private final ArrayList<Long> bookings = new ArrayList<>();
  private final SimpleBooleanProperty isEditMode = new SimpleBooleanProperty();
  private final SimpleBooleanProperty isPriceOk = new SimpleBooleanProperty();
  private final SimpleBooleanProperty isBrandOk = new SimpleBooleanProperty();
  private final SimpleBooleanProperty isModelOk = new SimpleBooleanProperty();
  private final SimpleBooleanProperty isSeatsOk = new SimpleBooleanProperty();
  private final SimpleBooleanProperty isBedsOk = new SimpleBooleanProperty();
  private final SimpleBooleanProperty isTitleOk = new SimpleBooleanProperty();
  private final SimpleBooleanProperty isLocationOk = new SimpleBooleanProperty();
  private final SimpleBooleanProperty isContactOk = new SimpleBooleanProperty();
  private final SimpleBooleanProperty isVehicleTypeOk = new SimpleBooleanProperty();
  private final SimpleBooleanProperty isTransmissionTypeOk = new SimpleBooleanProperty();
  private final SimpleBooleanProperty isWidthOk = new SimpleBooleanProperty();
  private final SimpleBooleanProperty isLengthOk = new SimpleBooleanProperty();
  private final SimpleBooleanProperty isHeightOk = new SimpleBooleanProperty();
  private final SimpleBooleanProperty isYearOk = new SimpleBooleanProperty();

  @FXML
  private TextField titleTextField;

  @FXML
  private TextField priceTextField;

  @FXML
  private TextField locationTextField;

  @FXML
  private TextField contactTextField;

  @FXML
  private TextArea particularitiesTextArea;

  @FXML
  private CheckBox activeCheckBox;

  @FXML
  private ComboBox<VehicleType> vehicleTypeComboBox;

  @FXML
  private Label errorLabel;

  @FXML
  private TextField brandTextField;

  @FXML
  private TextField modelTextField;

  @FXML
  private TextField constructionYearTextField;

  @FXML
  private TextField widthTextField;

  @FXML
  private TextField lengthTextField;

  @FXML
  private TextField heightTextField;

  @FXML
  private ComboBox<FuelType> fuelComboBox;

  @FXML
  private ComboBox<TransmissionType> transmissionComboBox;

  @FXML
  private ComboBox<String> seatsComboBox;

  @FXML
  private ComboBox<String> bedsComboBox;

  @FXML
  private CheckBox roofTentCheckBox;

  @FXML
  private CheckBox roofRackCheckBox;

  @FXML
  private CheckBox bikeRackCheckBox;

  @FXML
  private CheckBox showerCheckBox;

  @FXML
  private CheckBox toiletCheckBox;

  @FXML
  private CheckBox kitchenUnitCheckBox;

  @FXML
  private CheckBox fridgeCheckBox;

  @FXML
  private HBox picturesHBox;

  @FXML
  private Button placeOfferButton;

  @FXML
  private TextField rentalConditionsTextField;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private IUserController userController;

  @Autowired
  private IOfferController offerController;

  @Autowired
  private IPictureController pictureController;

  @Autowired
  private ValidationHelper validationHelper;

  @FXML
  private ListView<String> rentalConditionsListView;

  @FXML
  private DatePicker startDatePicker;

  @FXML
  private DatePicker endDatePicker;

  @FXML
  private ListView<Pair> blockedDatesListView;

  private List<String> rentalConditions = new ArrayList<>();
  private long offerID;
  private Vehicle offeredObject;
  private OfferDTO offerDTO;
  private List<Picture> pictures;

  @Autowired
  private VehicleRepository vehicleRepository;

  @Autowired
  private OfferService offerService;

  @Autowired
  private BookingService bookingService;

  private ArrayList<Pair> blockedDates = new ArrayList<>();

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
    seatsComboBox
      .valueProperty()
      .addListener((observable, oldValue, newValue) -> {
        if (newValue != null) validateSeats();
      });
    bedsComboBox
      .valueProperty()
      .addListener((observable, oldValue, newValue) -> {
        if (newValue != null) validateBeds();
      });
    titleTextField.setOnKeyTyped(this);
    locationTextField.setOnKeyTyped(this);
    contactTextField.setOnKeyTyped(this);
    widthTextField.setOnKeyTyped(this);
    lengthTextField.setOnKeyTyped(this);
    heightTextField.setOnKeyTyped(this);
    constructionYearTextField.setOnKeyTyped(this);

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
          .and(isWidthOk)
          .and(isLengthOk)
          .and(isHeightOk)
          .and(isYearOk)
          .not()
      );

    rentalConditionsListView
      .getSelectionModel()
      .setSelectionMode(SelectionMode.MULTIPLE);

    blockedDatesListView
      .getSelectionModel()
      .setSelectionMode(SelectionMode.MULTIPLE);

    startDatePicker.getEditor().setDisable(true);
    startDatePicker.getEditor().setOpacity(1);
    endDatePicker.getEditor().setDisable(true);
    endDatePicker.getEditor().setOpacity(1);
    setCellFactory(startDatePicker, null);
    setCellFactory(endDatePicker, null);

    startDatePicker
      .valueProperty()
      .addListener((observable, oldValue, newValue) -> {
        if (endDatePicker.getValue() == null) endDatePicker.setValue(newValue);
      });

    seatsComboBox.setItems(
      FXCollections.observableArrayList(
        "0",
        "1",
        "2",
        "3",
        "4",
        "5",
        "6",
        "7",
        "8",
        "9",
        "10"
      )
    );
    bedsComboBox.setItems(
      FXCollections.observableArrayList(
        "0",
        "1",
        "2",
        "3",
        "4",
        "5",
        "6",
        "7",
        "8",
        "9",
        "10"
      )
    );
    fuelComboBox.setItems(
      FXCollections.observableArrayList((FuelType.values()))
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
    this.offerDTO = offer;
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

    rentalConditions = offer.getRentalConditions();
    rentalConditionsListView.setItems(
      FXCollections.observableArrayList(rentalConditions)
    );

    startDatePicker.getEditor().setDisable(true);
    startDatePicker.getEditor().setOpacity(1);
    endDatePicker.getEditor().setDisable(true);
    endDatePicker.getEditor().setOpacity(1);
    setCellFactory(startDatePicker, offer);
    setCellFactory(endDatePicker, offer);
    blockedDates = offer.getBlockedDates();
    blockedDatesListView.setItems(
      FXCollections.observableArrayList(blockedDates)
    );
    styleBlockedDatesListView(blockedDatesListView);

    activeCheckBox.setSelected(offer.isActive());

    assert vehicle != null;
    vehicleTypeComboBox.setValue(vehicle.getVehicleType());
    brandTextField.setText(vehicle.getMake());
    modelTextField.setText(vehicle.getModel());
    seatsComboBox.setValue(String.valueOf(vehicle.getSeats()));
    bedsComboBox.setValue(String.valueOf(vehicle.getBeds()));
    constructionYearTextField.setText(vehicle.getYear());
    widthTextField.setText(String.valueOf(vehicle.getWidth()));
    lengthTextField.setText(String.valueOf(vehicle.getLength()));
    heightTextField.setText(String.valueOf(vehicle.getHeight()));
    fuelComboBox.setValue(vehicle.getFuelType());
    transmissionComboBox.setValue(
      vehicle.getTransmission() != null
        ? (
          vehicle
              .getTransmission()
              .equals(TransmissionType.AUTOMATIC.toString())
            ? TransmissionType.AUTOMATIC
            : TransmissionType.MANUAL
        )
        : null
    );
    roofTentCheckBox.setSelected(vehicle.isRoofTent());
    roofRackCheckBox.setSelected(vehicle.isRoofRack());
    bikeRackCheckBox.setSelected(vehicle.isBikeRack());
    showerCheckBox.setSelected(vehicle.isShower());
    toiletCheckBox.setSelected(vehicle.isToilet());
    kitchenUnitCheckBox.setSelected(vehicle.isKitchenUnit());
    fridgeCheckBox.setSelected(vehicle.isFridge());

    pictures.clear();
    for (PictureDTO pictureDTO : pictureController.getPicturesForVehicle(
      offer.getOfferedObject().getVehicleID()
    )) {
      pictures.add(modelMapper.pictureDTOToPicture(pictureDTO));
    }
    loadPictures(pictures);

    //Speichern-Button erst sichtbar machen wenn sich etwas in einem Textfield oder einer Checkbox etc. ver??ndert
    placeOfferButton.visibleProperty().set(false);
    placeOfferButton.setText("??nderungen speichern");
    activeCheckBox
      .selectedProperty()
      .addListener((observable, oldValue, newValue) ->
        placeOfferButton.visibleProperty().set(true)
      );
    titleTextField
      .textProperty()
      .addListener((observable, oldValue, newValue) ->
        placeOfferButton.visibleProperty().set(true)
      );
    contactTextField
      .textProperty()
      .addListener((observable, oldValue, newValue) ->
        placeOfferButton.visibleProperty().set(true)
      );
    priceTextField
      .textProperty()
      .addListener((observable, oldValue, newValue) ->
        placeOfferButton.visibleProperty().set(true)
      );
    locationTextField
      .textProperty()
      .addListener((observable, oldValue, newValue) ->
        placeOfferButton.visibleProperty().set(true)
      );
    particularitiesTextArea
      .textProperty()
      .addListener((observable, oldValue, newValue) ->
        placeOfferButton.visibleProperty().set(true)
      );
    vehicleTypeComboBox
      .valueProperty()
      .addListener((observable, oldValue, newValue) ->
        placeOfferButton.visibleProperty().set(true)
      );
    brandTextField
      .textProperty()
      .addListener((observable, oldValue, newValue) ->
        placeOfferButton.visibleProperty().set(true)
      );
    modelTextField
      .textProperty()
      .addListener((observable, oldValue, newValue) ->
        placeOfferButton.visibleProperty().set(true)
      );
    transmissionComboBox
      .valueProperty()
      .addListener((observable, oldValue, newValue) ->
        placeOfferButton.visibleProperty().set(true)
      );
    seatsComboBox
      .valueProperty()
      .addListener((observable, oldValue, newValue) ->
        placeOfferButton.visibleProperty().set(true)
      );
    bedsComboBox
      .valueProperty()
      .addListener((observable, oldValue, newValue) ->
        placeOfferButton.visibleProperty().set(true)
      );
    constructionYearTextField
      .textProperty()
      .addListener((observable, oldValue, newValue) ->
        placeOfferButton.visibleProperty().set(true)
      );
    fuelComboBox
      .valueProperty()
      .addListener((observable, oldValue, newValue) ->
        placeOfferButton.visibleProperty().set(true)
      );
    lengthTextField
      .textProperty()
      .addListener((observable, oldValue, newValue) ->
        placeOfferButton.visibleProperty().set(true)
      );
    widthTextField
      .textProperty()
      .addListener((observable, oldValue, newValue) ->
        placeOfferButton.visibleProperty().set(true)
      );
    heightTextField
      .textProperty()
      .addListener((observable, oldValue, newValue) ->
        placeOfferButton.visibleProperty().set(true)
      );
    roofTentCheckBox
      .selectedProperty()
      .addListener((observable, oldValue, newValue) ->
        placeOfferButton.visibleProperty().set(true)
      );
    roofRackCheckBox
      .selectedProperty()
      .addListener((observable, oldValue, newValue) ->
        placeOfferButton.visibleProperty().set(true)
      );
    bikeRackCheckBox
      .selectedProperty()
      .addListener((observable, oldValue, newValue) ->
        placeOfferButton.visibleProperty().set(true)
      );
    showerCheckBox
      .selectedProperty()
      .addListener((observable, oldValue, newValue) ->
        placeOfferButton.visibleProperty().set(true)
      );
    toiletCheckBox
      .selectedProperty()
      .addListener((observable, oldValue, newValue) ->
        placeOfferButton.visibleProperty().set(true)
      );
    kitchenUnitCheckBox
      .selectedProperty()
      .addListener((observable, oldValue, newValue) ->
        placeOfferButton.visibleProperty().set(true)
      );
    fridgeCheckBox
      .selectedProperty()
      .addListener((observable, oldValue, newValue) ->
        placeOfferButton.visibleProperty().set(true)
      );

    validateMandatoryFields();
  }

  private void removePicture(long pictureId) {
    pictures.removeIf(picture -> picture.getPictureID() == pictureId);
    loadPictures(pictures);
  }

  private void loadPictures(List<Picture> pictureList) {
    picturesHBox
      .getChildren()
      .subList(1, picturesHBox.getChildren().size())
      .clear();

    for (Picture picture : pictureList) {
      ImageView thumbnail = new ImageView(new Image(picture.getPath()));
      thumbnail.setFitHeight(60);
      thumbnail.setPreserveRatio(true);

      Button deleteBtn = new Button("x");
      deleteBtn.getStyleClass().addAll("bg-danger", "border-0");

      deleteBtn.setOnAction(event -> {
        removePicture(picture.getPictureID());
        if (isEditMode.get()) {
          placeOfferButton.visibleProperty().set(true);
        }
      });

      HBox imageBox = new HBox(thumbnail, deleteBtn);
      imageBox.setSpacing(-15);

      picturesHBox.getChildren().add(imageBox);
    }
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

    rentalConditions.clear();
    rentalConditionsListView.setItems(
      FXCollections.observableArrayList(rentalConditions)
    );

    vehicleTypeComboBox.setItems(
      FXCollections.observableArrayList(VehicleType.values())
    );
    vehicleTypeComboBox.setValue(null);
    brandTextField.clear();
    modelTextField.clear();
    constructionYearTextField.clear();
    widthTextField.clear();
    lengthTextField.clear();
    heightTextField.clear();
    fuelComboBox.setValue(null);
    transmissionComboBox.setItems(
      FXCollections.observableArrayList(TransmissionType.values())
    );
    transmissionComboBox.setValue(null);
    seatsComboBox.setValue(null);
    bedsComboBox.setValue(null);
    roofTentCheckBox.setSelected(false);
    roofRackCheckBox.setSelected(false);
    bikeRackCheckBox.setSelected(false);
    showerCheckBox.setSelected(false);
    toiletCheckBox.setSelected(false);
    kitchenUnitCheckBox.setSelected(false);
    fridgeCheckBox.setSelected(false);

    pictures = new ArrayList<>();
    picturesHBox.getChildren().remove(1, picturesHBox.getChildren().size());

    startDatePicker.setValue(null);
    endDatePicker.setValue(null);
    blockedDatesListView.getItems().clear();

    // resets all backgrounds to neutral
    // mandatory fields
    titleTextField.setStyle("");
    priceTextField.setStyle("");
    locationTextField.setStyle("");
    contactTextField.setStyle("");
    vehicleTypeComboBox.setStyle("");
    brandTextField.setStyle("");
    modelTextField.setStyle("");
    transmissionComboBox.setStyle("");
    seatsComboBox.setStyle("");
    bedsComboBox.setStyle("");

    // reset validated properties
    isTitleOk.set(false);
    isPriceOk.set(false);
    isLocationOk.set(false);
    isContactOk.set(false);
    isVehicleTypeOk.set(false);
    isBrandOk.set(false);
    isModelOk.set(false);
    isTransmissionTypeOk.set(false);
    isSeatsOk.set(false);
    isBedsOk.set(false);

    //because offer can be saved without these parameters
    isWidthOk.set(true);
    isLengthOk.set(true);
    isHeightOk.set(true);
    isYearOk.set(true);

    errorLabel.setText("");
  }

  @Override
  public void handle(KeyEvent event) {
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
    } else if (seatsComboBox.equals(source)) {
      validateSeats();
    } else if (bedsComboBox.equals(source)) {
      validateBeds();
    } else if (lengthTextField.equals(source)) {
      validateLength(lengthTextField.getText());
    } else if (widthTextField.equals(source)) {
      validateWidth(widthTextField.getText());
    } else if (heightTextField.equals(source)) {
      validateHeight(heightTextField.getText());
    } else if (constructionYearTextField.equals(source)) {
      int inputYear = Integer.parseInt(constructionYearTextField.getText());
      validateYear(inputYear);
    }
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
      OfferDTO offerDTO = offerController.create(
        titleTextField.getText(),
        locationTextField.getText(),
        contactTextField.getText(),
        particularitiesTextArea.getText(),
        longStringConverter.fromString(priceTextField.getText()),
        (ArrayList<String>) rentalConditions,
        blockedDates,
        vehicleTypeComboBox.getValue(),
        brandTextField.getText(),
        modelTextField.getText(),
        constructionYearTextField.getText(),
        length,
        width,
        height,
        fuelComboBox.getValue(),
        transmissionComboBox.getValue() != null
          ? transmissionComboBox.getValue().toString()
          : null,
        Integer.parseInt(seatsComboBox.getValue()),
        Integer.parseInt(bedsComboBox.getValue()),
        roofTentCheckBox.isSelected(),
        roofRackCheckBox.isSelected(),
        bikeRackCheckBox.isSelected(),
        showerCheckBox.isSelected(),
        toiletCheckBox.isSelected(),
        kitchenUnitCheckBox.isSelected(),
        fridgeCheckBox.isSelected()
      );

      offeredObject = offerDTO.getOfferedObject();
      savePictures();
      mainViewController.handleInformationMessage(
        String.format("Neues Angebot \"%s\" wurde erstellt.", offerDTO.getID())
      );
    } else if (isEditMode.get()) {
      offerController.update(
        offerID,
        userController.getLoggedInUser(),
        offeredObject,
        titleTextField.getText(),
        locationTextField.getText(),
        contactTextField.getText(),
        particularitiesTextArea.getText(),
        bookings,
        longStringConverter.fromString(priceTextField.getText()),
        activeCheckBox.isSelected(),
        (ArrayList<String>) rentalConditions,
        blockedDates,
        vehicleTypeComboBox.getValue(),
        brandTextField.getText(),
        modelTextField.getText(),
        constructionYearTextField.getText(),
        length,
        width,
        height,
        fuelComboBox.getValue(),
        transmissionComboBox.getValue() != null
          ? transmissionComboBox.getValue().toString()
          : null,
        Integer.parseInt(seatsComboBox.getValue()),
        Integer.parseInt(bedsComboBox.getValue()),
        roofTentCheckBox.isSelected(),
        roofRackCheckBox.isSelected(),
        bikeRackCheckBox.isSelected(),
        showerCheckBox.isSelected(),
        toiletCheckBox.isSelected(),
        kitchenUnitCheckBox.isSelected(),
        fridgeCheckBox.isSelected()
      );
    }

    savePictures();
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
      "Willst du wirklich abbrechen? Alle ??nderungen gehen verloren!"
    );
    Optional<ButtonType> result = confirmDelete.showAndWait();

    if (result.isPresent() && result.get() == ButtonType.OK) {
      resetFields();
      mainViewController.changeView("activeOffers");
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
    validateSeats();
    validateBeds();
  }

  private void validateTrue(Node element) {
    element.setStyle("-fx-background-color: #1987547f;");
  }

  private void validateFalse(Node element) {
    element.setStyle("-fx-background-color: #dc35457f;");
  }

  private void validateTitle(String inputTitle) {
    if (!ValidationHelper.checkOfferTitle(inputTitle)) {
      errorLabel.setText("Ung??ltiger Titel");
      validateFalse(titleTextField);
      isTitleOk.set(false);
    } else {
      errorLabel.setText("");
      validateTrue(titleTextField);
      isTitleOk.set(true);
    }
  }

  private void validatePrice(String inputPrice) {
    if (!ValidationHelper.checkOfferPrice(inputPrice)) {
      errorLabel.setText("Ung??ltiger Preis");
      validateFalse(priceTextField);
      isPriceOk.set(false);
    } else {
      errorLabel.setText("");
      validateTrue(priceTextField);
      isPriceOk.set(true);
    }
  }

  private void validateLocation(String inputLocation) {
    if (!ValidationHelper.checkOfferLocation(inputLocation)) {
      errorLabel.setText("Ung??ltiger Abholort");
      validateFalse(locationTextField);
      isLocationOk.set(false);
    } else {
      errorLabel.setText("");
      validateTrue(locationTextField);
      isLocationOk.set(true);
    }
  }

  private void validateContact(String inputContact) {
    if (!ValidationHelper.checkOfferContact(inputContact)) {
      errorLabel.setText("Ung??ltiger Kontakt");
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
      errorLabel.setText("Ung??ltiger Fahrzeugtyp");
      validateFalse(vehicleTypeComboBox);
      isVehicleTypeOk.set(false);
    } else {
      if (inputVehicleType.equals(VehicleType.TRAILER)) {
        transmissionComboBox.setDisable(true);
        transmissionComboBox.setValue(null);
        isTransmissionTypeOk.set(true);
        fuelComboBox.setDisable(true);
        fuelComboBox.setValue(null);
      } else {
        transmissionComboBox.setDisable(false);
        isTransmissionTypeOk.set(false);
        fuelComboBox.setDisable(false);
      }
      errorLabel.setText("");
      validateTrue(vehicleTypeComboBox);
      isVehicleTypeOk.set(true);
    }
    if (seatsComboBox.getValue() != null) validateSeats();
  }

  private void validateBrand(String inputBrand) {
    if (!ValidationHelper.checkStringLength(inputBrand, 2, 25)) {
      errorLabel.setText("Ung??ltiger Hersteller");
      validateFalse(brandTextField);
      isBrandOk.set(false);
    } else {
      errorLabel.setText("");
      validateTrue(brandTextField);
      isBrandOk.set(true);
    }
  }

  private void validateModel(String inputModel) {
    if (!ValidationHelper.checkStringLength(inputModel, 2, 25)) {
      errorLabel.setText("Ung??ltiges Modell");
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
    if (
      vehicleTypeComboBox.getValue() != null &&
      vehicleTypeComboBox.getValue().equals(VehicleType.TRAILER)
    ) {
      isTransmissionTypeOk.set(true);
      return;
    }
    if (inputTransmissionType == null) {
      errorLabel.setText("Ung??ltige Schaltung");
      validateFalse(transmissionComboBox);
      isTransmissionTypeOk.set(false);
    } else {
      errorLabel.setText("");
      validateTrue(transmissionComboBox);
      isTransmissionTypeOk.set(true);
    }
  }

  private void validateSeats() {
    if (
      (
        vehicleTypeComboBox.getValue() != null &&
        vehicleTypeComboBox.getValue().equals(VehicleType.TRAILER)
      ) ||
      Integer.parseInt(seatsComboBox.getValue()) > 0
    ) {
      errorLabel.setText("");
      validateTrue(seatsComboBox);
      isSeatsOk.set(true);
    } else {
      errorLabel.setText("Ung??ltige Anzahl von Sitzpl??tze");
      validateFalse(seatsComboBox);
      isSeatsOk.set(false);
    }
  }

  private void validateBeds() {
    validateTrue(bedsComboBox);
    isBedsOk.set(true);
  }

  private void validateWidth(String inputWidth) {
    try {
      if (
        inputWidth.isEmpty() ||
        validationHelper.checkSizeParameter(Integer.parseInt(inputWidth))
      ) {
        errorLabel.setText("");
        validateTrue(widthTextField);
        isWidthOk.set(true);
      } else {
        errorLabel.setText("Ung??ltige Breite");
        validateFalse(widthTextField);
        isWidthOk.set(false);
      }
    } catch (NumberFormatException e) {
      errorLabel.setText("Ung??ltige Breite");
      validateFalse(widthTextField);
      isWidthOk.set(false);
    }
  }

  private void validateLength(String inputLength) {
    try {
      if (
        inputLength.isEmpty() ||
        validationHelper.checkSizeParameter(Integer.parseInt(inputLength))
      ) {
        errorLabel.setText("");
        validateTrue(lengthTextField);
        isLengthOk.set(true);
      } else {
        errorLabel.setText("Ung??ltige L??nge");
        validateFalse(lengthTextField);
        isLengthOk.set(false);
      }
    } catch (NumberFormatException e) {
      errorLabel.setText("Ung??ltige L??nge");
      validateFalse(lengthTextField);
      isLengthOk.set(false);
    }
  }

  private void validateHeight(String inputHeight) {
    try {
      if (
        inputHeight.isEmpty() ||
        validationHelper.checkSizeParameter(Integer.parseInt(inputHeight))
      ) {
        errorLabel.setText("");
        validateTrue(heightTextField);
        isHeightOk.set(true);
      } else {
        errorLabel.setText("Ung??ltige H??he");
        validateFalse(heightTextField);
        isHeightOk.set(false);
      }
    } catch (NumberFormatException e) {
      errorLabel.setText("Ung??ltige H??he");
      validateFalse(heightTextField);
      isHeightOk.set(false);
    }
  }

  private void validateYear(int inputYear) {
    if (!validationHelper.checkYear(inputYear)) {
      errorLabel.setText("Ung??ltiges Baujahr");
      validateFalse(constructionYearTextField);
      isYearOk.set(false);
    } else {
      errorLabel.setText("");
      validateTrue(constructionYearTextField);
      isYearOk.set(true);
    }
  }

  /**
   * Imports the path of a file from the filesystem.
   *
   * @param event ActionEvent from FXML to determine the pressed button
   */
  public void importFileChooserAction(ActionEvent event) throws IOException {
    Node source = (Node) event.getSource();
    Window window = source.getScene().getWindow();

    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Bilder hinzuf??gen");
    fileChooser
      .getExtensionFilters()
      .add(
        new FileChooser.ExtensionFilter("Bilder", "*.png", "*.jpg", "*.jpeg")
      );
    List<File> fileList = fileChooser.showOpenMultipleDialog(window);

    for (File file : fileList) {
      Picture newPicturePath = new Picture("file:///" + file.getAbsolutePath());
      pictures.add(newPicturePath);
      if (isEditMode.get()) {
        placeOfferButton.visibleProperty().set(true);
      }
    }

    loadPictures(pictures);
  }

  /**
   * Uploads one or more before selected pictures to the database
   *
   * @return
   */
  public void savePictures() {
    // filter for pictures that are already in the database
    for (PictureDTO pictureDTO : pictureController.getPicturesForVehicle(
      offeredObject.getVehicleID()
    )) {
      boolean pictureIsInDatabaseAlready = false;
      for (Picture picture : pictures) {
        if (picture.getPictureID() == pictureDTO.getPictureID()) {
          pictureIsInDatabaseAlready = true;
          break;
        }
      }
      // delete picture if it is not needed anymore
      if (!pictureIsInDatabaseAlready) {
        pictureController.deletePictureById(pictureDTO.getPictureID());
      }
    }

    // add all newly needed pictures to the database
    for (Picture picture : pictures) {
      Picture pictureToSave = new Picture();
      pictureToSave.setPictureID(picture.getPictureID());
      pictureToSave.setVehicleID(offeredObject.getVehicleID());
      pictureToSave.setPath(picture.getPath());

      pictureController.create(pictureToSave);
    }
  }

  /**
   * action for adding new rental conditions
   */
  public void addRentalConditionButtonAction() {
    if (rentalConditionsTextField.getText().isEmpty()) {
      mainViewController.handleExceptionMessage("Nichts zum Hinzuf??gen!");
    } else if (
      !ValidationHelper.checkStringLength(
        rentalConditionsTextField.getText(),
        3,
        25
      )
    ) {
      mainViewController.handleExceptionMessage(
        "Die Mietvoraussetzung ist zu lang!"
      );
    } else {
      String rentalCondition = rentalConditionsTextField.getText();
      rentalConditionsTextField.clear();
      rentalConditions.add(rentalCondition);
      ObservableList<String> myObservableList = FXCollections.observableList(
        rentalConditions
      );
      rentalConditionsListView.setItems(myObservableList);
      if (isEditMode.get()) {
        placeOfferButton.visibleProperty().set(true);
      }
    }
  }

  /**
   * action for removing rental conditions
   */
  public void removeRentalConditionButtonAction() {
    if (
      rentalConditionsListView.getSelectionModel().getSelectedItems() != null &&
      !rentalConditionsListView.getSelectionModel().getSelectedItems().isEmpty()
    ) {
      rentalConditions.removeAll(
        rentalConditionsListView.getSelectionModel().getSelectedItems()
      );
      ObservableList<String> myObservableList = FXCollections.observableList(
        rentalConditions
      );
      rentalConditionsListView.setItems(myObservableList);
      if (isEditMode.get()) {
        placeOfferButton.visibleProperty().set(true);
      }
    } else {
      mainViewController.handleExceptionMessage("Nichts zum Entfernen!");
    }
  }

  /**
   * Action for adding dates to blockedDates (listView and actual list)
   */
  public void addDatesButtonAction() {
    if (
      startDatePicker.getValue() == null ||
      endDatePicker.getValue() == null ||
      ValidationHelper.checkRentingDates(
        startDatePicker.getValue(),
        endDatePicker.getValue()
      )
    ) {
      mainViewController.handleExceptionMessage(
        "Das Startdatum darf nicht nach oder am selben Tag wie das Enddatum liegen!"
      );
    } else if (
      isEditMode.get() &&
      ValidationHelper.checkRentingDatesWithOffer(
        startDatePicker.getValue(),
        endDatePicker.getValue(),
        offerDTO,
        bookingService,
        offerService,
        mainViewController
      )
    ) {
      mainViewController.handleExceptionMessage(
        "Zwischen Start- und Enddatum darf keine andere Buchung liegen!"
      );
    } else {
      Pair<LocalDate, LocalDate> startAndEndDate = new Pair<>(
        startDatePicker.getValue(),
        endDatePicker.getValue()
      );
      startDatePicker.getEditor().clear();
      endDatePicker.getEditor().clear();
      blockedDates.add(startAndEndDate);
      ObservableList<Pair> myObservableList = FXCollections.observableList(
        blockedDates
      );
      blockedDatesListView.setItems(myObservableList);
      styleBlockedDatesListView(blockedDatesListView);
      if (isEditMode.get()) {
        placeOfferButton.visibleProperty().set(true);
      }
    }
  }

  /**
   * Action for removing dates from blockedDates (listView and actual list)
   */
  public void removeDatesButtonAction() {
    if (
      blockedDatesListView.getSelectionModel().getSelectedItems() != null &&
      !blockedDatesListView.getSelectionModel().getSelectedItems().isEmpty()
    ) {
      blockedDates.removeAll(
        blockedDatesListView.getSelectionModel().getSelectedItems()
      );
      ObservableList<Pair> myObservableList = FXCollections.observableList(
        blockedDates
      );
      blockedDatesListView.setItems(myObservableList);
      styleBlockedDatesListView(blockedDatesListView);
      if (isEditMode.get()) {
        placeOfferButton.visibleProperty().set(true);
      }
    } else {
      mainViewController.handleExceptionMessage("Nichts zum Entfernen!");
    }
  }

  /**
   * Method for styling blockedDates ListView entries
   *
   * @param listView
   */
  public void styleBlockedDatesListView(ListView listView) {
    listView.setCellFactory(
      new Callback<ListView<Pair>, ListCell<Pair>>() {
        @Override
        public ListCell<Pair> call(ListView<Pair> param) {
          return new ListCell<>() {
            @Override
            public void updateItem(Pair pair, boolean empty) {
              super.updateItem(pair, empty);
              if (empty || pair == null) {
                setText(null);
              } else {
                setText(pair.getKey() + " bis " + pair.getValue());
              }
            }
          };
        }
      }
    );
  }

  /**
   * Creates and sets a cellFactory for the given DatePicker, which makes all days before today un-clickable,
   * and in addition, if an existing offer is being updated, also makes all blockedDays pink and un-clickable
   *
   * @param datePicker
   * @param offerDTO
   */
  public void setCellFactory(DatePicker datePicker, OfferDTO offerDTO) {
    if (!isEditMode.get()) {
      datePicker.setDayCellFactory(
        new Callback<DatePicker, DateCell>() {
          @Override
          public DateCell call(DatePicker param) {
            return new DateCell() {
              @Override
              public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (!empty && date != null) {
                  LocalDate today = LocalDate.now();
                  setDisable(empty || date.compareTo(today) < 0);
                }
              }
            };
          }
        }
      );
    } else {
      try {
        final List<LocalDate> blockedDates = offerService.getBlockedDates(
          offerDTO.getID()
        );
        datePicker.setDayCellFactory(
          new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
              return new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                  super.updateItem(date, empty);
                  if (!empty && date != null) {
                    LocalDate today = LocalDate.now();
                    setDisable(empty || date.compareTo(today) < 0);
                    if (blockedDates.contains(date)) {
                      // Aussehen und Verhalten der Zellen setzen
                      this.setStyle("-fx-background-color: pink");
                      setDisable(true);
                    }
                  }
                }
              };
            }
          }
        );
      } catch (GenericServiceException e) {
        mainViewController.handleExceptionMessage(e.getMessage());
      }
    }
  }
}
