package swtcamper.javafx.controller;

import static javafx.scene.control.SelectionMode.MULTIPLE;

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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.Pair;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.LongStringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.BookingDTO;
import swtcamper.api.contract.OfferDTO;
import swtcamper.api.contract.PictureDTO;
import swtcamper.api.controller.*;
import swtcamper.backend.entities.*;
import swtcamper.backend.repositories.BookingRepository;
import swtcamper.backend.repositories.OfferRepository;
import swtcamper.backend.repositories.VehicleRepository;
import swtcamper.backend.services.BookingService;
import swtcamper.backend.services.OfferService;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class ModifyOfferViewController implements EventHandler<KeyEvent> {

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private UserController userController;

  @Autowired
  private OfferController offerController;

  @Autowired
  private PictureController pictureController;

  @Autowired
  private ValidationHelper validationHelper;

  DoubleStringConverter doubleStringConverter = new DoubleStringConverter();
  LongStringConverter longStringConverter = new LongStringConverter();

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
  public HBox picturesHbox;

  @FXML
  public Button placeOfferButton;

  @FXML
  public TextField rentalConditionsTextField;

  @FXML
  private ListView<String> rentalConditionsListView = new ListView<>();

  @FXML
  private DatePicker startDatePicker;

  @FXML
  private DatePicker endDatePicker;

  @FXML
  private ListView blockedDatesListView;

  private ArrayList<Long> bookings = new ArrayList<>();

  private List<String> rentalConditions = new ArrayList<>();

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
  private OfferDTO offerDTO;

  private List<Picture> pictures;

  @Autowired
  private VehicleRepository vehicleRepository;

  @Autowired
  private BookingController bookingController;

  @Autowired
  private OfferRepository offerRepository;

  @Autowired
  private BookingRepository bookingRepository;

  @Autowired
  private OfferService offerService;

  @Autowired
  BookingService bookingService;

  private final SimpleBooleanProperty isEditMode = new SimpleBooleanProperty();

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

    pictures.clear();
    for (PictureDTO pictureDTO : pictureController.getPicturesForVehicle(
      offer.getOfferedObject().getVehicleID()
    )) {
      pictures.add(modelMapper.pictureDTOToPicture(pictureDTO));
    }
    loadPictures(pictures);

    //Speichern-Button erst sichtbar machen wenn sich etwas in einem Textfield oder einer Checkbox etc. verändert
    placeOfferButton.visibleProperty().set(false);
    placeOfferButton.setText("Änderungen speichern");
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
    seatsTextField
      .textProperty()
      .addListener((observable, oldValue, newValue) ->
        placeOfferButton.visibleProperty().set(true)
      );
    bedsTextField
      .textProperty()
      .addListener((observable, oldValue, newValue) ->
        placeOfferButton.visibleProperty().set(true)
      );
    constructionYearTextField
      .textProperty()
      .addListener((observable, oldValue, newValue) ->
        placeOfferButton.visibleProperty().set(true)
      );
    engineTextField
      .textProperty()
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
    picturesHbox
      .getChildren()
      .subList(1, picturesHbox.getChildren().size())
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

      picturesHbox.getChildren().add(imageBox);
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

    vehicleTypeComboBox.setItems(
      FXCollections.observableArrayList(VehicleType.values())
    );
    vehicleTypeComboBox.getSelectionModel().clearSelection();
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
    transmissionComboBox.getSelectionModel().clearSelection();

    seatsTextField.clear();
    bedsTextField.clear();
    roofTentCheckBox.setSelected(false);
    roofRackCheckBox.setSelected(false);
    bikeRackCheckBox.setSelected(false);
    showerCheckBox.setSelected(false);
    toiletCheckBox.setSelected(false);
    kitchenUnitCheckBox.setSelected(false);
    fridgeCheckBox.setSelected(false);

    pictures = new ArrayList<>();
    picturesHbox.getChildren().remove(1, picturesHbox.getChildren().size());

    startDatePicker.setValue(null);
    endDatePicker.setValue(null);
    blockedDatesListView.getItems().clear();

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
        // TODO length etc aus Feldern holen
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

      offeredObject = offerDTO.getOfferedObject();
      savePictures();
      mainViewController.handleInformationMessage(
        String.format("Neues Angebot \"%s\" wurde erstellt.", offerDTO.getID())
      );
    } else if (isEditMode.get()) {
      // TODO raus: ArrayList<Long> bookings = null;

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
  public void importFileChooserAction(ActionEvent event) throws IOException {
    Node source = (Node) event.getSource();
    Window window = source.getScene().getWindow();

    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Bilder hinzufügen");
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
      pictureController.create(
        new PictureDTO(
          picture.getPictureID(),
          offeredObject.getVehicleID(),
          picture.getPath()
        )
      );
    }
  }

  /**
   * action for adding new rental conditions
   */
  public void addRentalConditionButtonAction() {
    if (
      rentalConditionsTextField.getText() != null &&
      !rentalConditionsTextField.getText().isEmpty()
    ) {
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
    } else {
      mainViewController.handleExceptionMessage("Nichts zum Hinzufügen!");
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
      !ValidationHelper.checkRentingDates(
        startDatePicker.getValue(),
        endDatePicker.getValue()
      )
    ) {
      mainViewController.handleExceptionMessage(
        "Das Startdatum darf nicht nach oder am selben Tag wie das Enddatum liegen!"
      );
    } else if (
      isEditMode.get() &&
      !ValidationHelper.checkRentingDatesWithOffer(
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
