package swtcamper.javafx.controller;

import static javafx.scene.control.SelectionMode.MULTIPLE;

import java.io.File;
import java.io.IOException;
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
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.LongStringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.OfferDTO;
import swtcamper.api.contract.PictureDTO;
import swtcamper.api.controller.OfferController;
import swtcamper.api.controller.PictureController;
import swtcamper.api.controller.UserController;
import swtcamper.api.controller.ValidationHelper;
import swtcamper.backend.entities.Picture;
import swtcamper.backend.entities.TransmissionType;
import swtcamper.backend.entities.Vehicle;
import swtcamper.backend.entities.VehicleType;
import swtcamper.backend.repositories.VehicleRepository;
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
  ListView<String> rentalConditionsListView;

  List<String> rentalConditions = new ArrayList<>();

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

  private List<Picture> pictures;

  @Autowired
  private VehicleRepository vehicleRepository;

  private final SimpleBooleanProperty isEditMode = new SimpleBooleanProperty();

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

    rentalConditions = offer.getRentalConditions();
    rentalConditionsListView.setItems(
      FXCollections.observableArrayList(rentalConditions)
    );

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

      deleteBtn.setOnAction(event -> removePicture(picture.getPictureID()));

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

    pictures = new ArrayList<>();
    picturesHbox.getChildren().remove(1, picturesHbox.getChildren().size());

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
        userController.getLoggedInUser(),
        titleTextField.getText(),
        locationTextField.getText(),
        contactTextField.getText(),
        particularitiesTextArea.getText(),
        longStringConverter.fromString(priceTextField.getText()),
        (ArrayList<String>) rentalConditions,
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

      offeredObject = offerDTO.getOfferedObject();
      savePictures();

      mainViewController.handleInformationMessage(
        String.format("Neues Angebot \"%s\" wurde erstellt.", offerDTO.getID())
      );
    } else if (isEditMode.get()) {
      ArrayList<Long> bookings = null;

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
  public void addButtonAction() {
    String rentalCondition = rentalConditionsTextField.getText();
    rentalConditionsTextField.clear();
    rentalConditions.add(rentalCondition);
    ObservableList<String> myObservableList = FXCollections.observableList(
      rentalConditions
    );
    rentalConditionsListView.setItems(myObservableList);
    rentalConditionsListView.getSelectionModel().setSelectionMode(MULTIPLE);
  }

  /**
   * action for removing rental conditions
   */
  public void removeButtonAction() {
    rentalConditions.removeAll(
      rentalConditionsListView.getSelectionModel().getSelectedItems()
    );
    rentalConditionsListView.getSelectionModel().clearSelection();
  }
}
