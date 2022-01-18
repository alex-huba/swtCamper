package swtcamper.javafx.controller;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.OfferDTO;
import swtcamper.api.controller.OfferController;
import swtcamper.api.controller.PictureController;
import swtcamper.api.controller.UserController;
import swtcamper.api.controller.ValidationHelper;
import swtcamper.backend.entities.Filter;
import swtcamper.backend.entities.FuelType;
import swtcamper.backend.entities.TransmissionType;
import swtcamper.backend.entities.VehicleType;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class RentingViewController implements EventHandler<KeyEvent> {

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private OfferViewController offerViewController;

  @Autowired
  private OfferController offerController;

  @Autowired
  private PictureController pictureController;

  @Autowired
  private UserController userController;

  @Autowired
  private ValidationHelper validationHelper;

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
  public ComboBox<FuelType> fuelTypeComboBox;

  @FXML
  public Button resetFuelTypeBtn;

  @FXML
  public ComboBox<TransmissionType> transmissionComboBox;

  @FXML
  public Button resetTransmissionTypeBtn;

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

  @FXML
  public HBox paginationHBox;

  @FXML
  public ChoiceBox<Integer> offersPerPageChoiceBox;

  @FXML
  public HBox offersPerPageHBox;

  @FXML
  public HBox offerListBox;

  @FXML
  public ScrollPane offerListScroll;

  @FXML
  public VBox offerListRoot;

  @FXML
  public VBox rootVBOX;

  @FXML
  public AnchorPane rootAnchorPane;

  @FXML
  public Label errorLabel;

  private List<OfferDTO> offerDTOList;
  private List<List<OfferDTO>> subListsList;
  int lastPageVisited;

  @FXML
  private void initialize() {
    offersPerPageChoiceBox.setItems(
      FXCollections.observableArrayList(1, 5, 10, 20)
    );
    offersPerPageChoiceBox.setValue(5);
    offersPerPageChoiceBox
      .valueProperty()
      .addListener((observable, oldValue, newValue) -> {
        try {
          lastPageVisited = 0;
          reloadData();
        } catch (GenericServiceException ignore) {}
      });

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

    fuelTypeComboBox.setItems(
      FXCollections.observableArrayList((FuelType.values()))
    );
    fuelTypeComboBox.setButtonCell(
      new ListCell<>() {
        @Override
        protected void updateItem(FuelType item, boolean empty) {
          super.updateItem(item, empty);
          if (empty || item == null) {
            setText(fuelTypeComboBox.getPromptText());
          } else {
            setText(item.toString());
          }
        }
      }
    );
    resetFuelTypeBtn
      .visibleProperty()
      .bind(fuelTypeComboBox.valueProperty().isNotNull());

    offerListBox.setHgrow(offerListScroll, Priority.ALWAYS);
    offerListScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
    offerListRoot.setVgrow(offerListScroll, Priority.ALWAYS);
    offerListScroll.setPrefHeight(
      rootVBOX.getHeight() - rootAnchorPane.getHeight()
    );
    errorLabel.setText("");

    constructionYearTextField.setOnKeyTyped(this);
    maxPricePerDayTextField.setOnKeyTyped(this);
    seatAmountTextField.setOnKeyTyped(this);
    bedAmountTextField.setOnKeyTyped(this);
  }

  @Override
  public void handle(KeyEvent event) {
    validateInput(event);
  }

  private void validateInput(KeyEvent event) {
    Object source = event.getSource();

    if (constructionYearTextField.equals(source)) {
      int inputYear = Integer.parseInt(constructionYearTextField.getText());
      validateYear(inputYear);
    } else if (maxPricePerDayTextField.equals(source)) {
      String inputPrice = maxPricePerDayTextField.getText();
      validatePrice(inputPrice);
    } else if (seatAmountTextField.equals(source)) {
      String inputSeats = seatAmountTextField.getText();
      validateSeats(inputSeats);
    } else if (bedAmountTextField.equals(source)) {
      String inputBeds = bedAmountTextField.getText();
      validateBeds(inputBeds);
    }
  }

  private void validateYear(int inputYear) {
    if (!validationHelper.checkYear(inputYear)) {
      errorLabel.setText("Wählen Sie bitte ein gültiges Baujahr");
      validateFalse(constructionYearTextField);
    } else {
      errorLabel.setText("");
      validateTrue(constructionYearTextField);
    }
  }

  private void validatePrice(String inputPrice) {
    if (!validationHelper.checkOfferPrice(inputPrice)) {
      errorLabel.setText("Wählen Sie bitte ein gültigen Preis");
      validateFalse(maxPricePerDayTextField);
    } else {
      errorLabel.setText("");
      validateTrue(maxPricePerDayTextField);
    }
  }

  private void validateSeats(String inputSeats) {
    if (!validationHelper.checkSeats(inputSeats)) {
      errorLabel.setText("Wählen Sie bitte eine gültige Anzahl von Sitzplätze");
      validateFalse(seatAmountTextField);
    } else {
      errorLabel.setText("");
      validateTrue(seatAmountTextField);
    }
  }

  private void validateBeds(String inputBeds) {
    if (!validationHelper.checkBeds(inputBeds)) {
      errorLabel.setText("Wählen Sie bitte eine gültige Anzahl von Betten");
      validateFalse(bedAmountTextField);
    } else {
      errorLabel.setText("");
      validateTrue(bedAmountTextField);
    }
  }

  private void validateTrue(Node element) {
    element.setStyle(
      "-fx-background-color: #transparent; -fx-text-fill: #000000"
    );
  }

  private void validateFalse(Node element) {
    element.setStyle("-fx-background-color: #dc3545; -fx-text-fill: #FFFFFF");
  }

  /**
   * Gets all available offers from the database and creates subLists.
   * @throws GenericServiceException
   */
  public void reloadData() throws GenericServiceException {
    // get available offers
    offerDTOList =
      offerController
        .offers()
        .parallelStream()
        .filter(OfferDTO::isActive)
        .filter(offerDTO ->
          // returns offerDTOs in which the loggedInUser is not excluded
          // and in which the field excludedRenters is not null
          // returns true (= every offerDTO) otherwise
          userController.getLoggedInUser() == null ||
          offerDTO.getCreator().getExcludedRenters() == null ||
          !offerDTO
            .getCreator()
            .getExcludedRenters()
            .contains(userController.getLoggedInUser().getId())
        )
        .collect(Collectors.toList());

    // partition them according to offersPerPageChoiceBox's value
    subListsList =
      createOfferSublists(offerDTOList, offersPerPageChoiceBox.getValue());
    // and load the first chunk
    loadData(subListsList.get(0));
  }

  /**
   * Adds a pagination to the list of offers
   */
  private void addPagination() {
    // get amount of pages by rounding up the result of offerDTOList's size divided by offersPerPageChoiceBox's value
    int pageAmount = (int) Math.ceil(
      offerDTOList.size() / Double.valueOf(offersPerPageChoiceBox.getValue())
    );

    paginationHBox.getChildren().clear();
    for (int i = 0; i < pageAmount; i++) {
      Button pageButton = new Button(String.valueOf(i + 1));
      pageButton
        .getStyleClass()
        .add(i == lastPageVisited ? "bg-primary" : "bg-secondary");

      int finalI = i;
      pageButton.setOnAction(event -> {
        lastPageVisited = finalI;
        loadData(subListsList.get(finalI));
      });
      paginationHBox.getChildren().add(pageButton);
    }
    paginationHBox.getChildren().add(offersPerPageHBox);

    offerListRoot.getChildren().add(paginationHBox);
  }

  /**
   * Partitions an offer-list into sublists of a specific max length
   * @param inputList offer-list to partition
   * @param size max length of each sublist
   * @return list of lists, each with a max length
   */
  private List<List<OfferDTO>> createOfferSublists(
    List<OfferDTO> inputList,
    int size
  ) {
    final AtomicInteger counter = new AtomicInteger(0);
    return FXCollections.observableArrayList(
      inputList
        .stream()
        .collect(Collectors.groupingBy(s -> counter.getAndIncrement() / size))
        .values()
    );
  }

  /**
   * Loads a specific list to the visible view
   * @param offersList list to load
   */
  private void loadData(List<OfferDTO> offersList) {
    offerListRoot.getChildren().clear();

    Label header = new Label(
      String.format(
        "%s Angebote für Sie",
        offersList.size() > 0 ? "Passende" : "Keine passenden"
      )
    );
    header.setStyle("-fx-font-size: 30");
    offerListRoot.getChildren().add(header);

    for (OfferDTO offer : offersList) {
      VBox root = new VBox();
      Label promoteLabel = new Label("");

      // visually highlight promoted offer
      if (offer.isPromoted()) {
        root.setStyle(
          "-fx-background-color: #add8e6; -fx-background-radius: 20px"
        );
        promoteLabel.setText("-- Von uns empfohlen! --");
        promoteLabel.setStyle(
          "-fx-font-size: 20; -fx-font-family: Arial Rounded MT Bold;"
        );
      } else {
        root.setStyle(
          "-fx-background-color: #c9dfce; -fx-background-radius: 20px"
        );
      }

      root.setEffect(new DropShadow(4d, 0d, +6d, Color.BLACK));

      Image image;
      if (
        pictureController
          .getPicturesForVehicle(offer.getOfferedObject().getVehicleID())
          .size() >
        0
      ) {
        image =
          new Image(
            pictureController
              .getPicturesForVehicle(offer.getOfferedObject().getVehicleID())
              .get(0)
              .getPath()
          );
      } else {
        image = new Image("/pictures/noImg.png");
      }

      // thumbnail
      ImageView thumbnail = new ImageView(image);
      thumbnail.setFitHeight(150);
      thumbnail.setPreserveRatio(true);
      HBox thumbnailHbox = new HBox(thumbnail);
      thumbnailHbox.setAlignment(Pos.TOP_CENTER);
      thumbnailHbox.setStyle("-fx-padding: 20 20 20 20");

      // title
      Label titleLabel = new Label(offer.getTitle());
      titleLabel.setStyle(
        "-fx-font-size: 35; -fx-font-family: \"Arial Rounded MT Bold\"; -fx-text-fill: #040759"
      );

      // location
      Label locationLabel = new Label("Abholort: " + offer.getLocation());
      locationLabel.setStyle(
        "-fx-font-size: 20; -fx-font-family: \"Arial Rounded MT Bold\";"
      );

      // price
      Label priceLabel = new Label("Preis pro Tag: € " + offer.getPrice());
      priceLabel.setStyle(
        "-fx-font-size: 20; -fx-font-family: \"Arial Rounded MT Bold\";"
      );

      // brand
      Label brandLabel = new Label(
        "Marke: " + offer.getOfferedObject().getVehicleFeatures().getMake()
      );
      brandLabel.setStyle(
        "-fx-font-size: 20; -fx-font-family: \"Arial Rounded MT Bold\";"
      );

      // model
      Label modelLabel = new Label(
        "Modell: " + offer.getOfferedObject().getVehicleFeatures().getModel()
      );
      modelLabel.setStyle(
        "-fx-font-size: 20; -fx-font-family: \"Arial Rounded MT Bold\";"
      );

      VBox locationPriceBrandModelBox = new VBox(
        locationLabel,
        priceLabel,
        brandLabel,
        modelLabel
      );
      locationPriceBrandModelBox.setStyle("-fx-padding: 0 0 0 30");

      Button moreBtn = new Button("Mehr Information");
      moreBtn.getStyleClass().add("bg-primary");
      moreBtn.setOnAction(event -> {
        try {
          mainViewController.changeView("viewOffer");
          offerViewController.initialize(offer, true);
        } catch (GenericServiceException ignore) {}
      });

      HBox btnBox = new HBox(moreBtn);
      btnBox.setAlignment(Pos.TOP_RIGHT);
      btnBox.setStyle("-fx-padding: 0 30 30 0");

      VBox detailsVBox = new VBox(
        promoteLabel,
        titleLabel,
        locationPriceBrandModelBox,
        btnBox
      );
      detailsVBox.setAlignment(Pos.TOP_CENTER);
      HBox.setHgrow(detailsVBox, Priority.ALWAYS);

      HBox offerDetails = new HBox(thumbnailHbox, detailsVBox);

      root.getChildren().add(offerDetails);
      offerListRoot.getChildren().add(root);
    }

    if (offersList.size() > 0) addPagination();
  }

  /**
   * Creates a new Filter Object and gets all offers from the database that fit to this filter.
   *
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
    if (fuelTypeComboBox.getValue() != null) newFilter.setFuelType(
      fuelTypeComboBox.getValue()
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
    newFilter.setRoofTent(roofTentCheckBox.isSelected());
    newFilter.setRoofRack(roofRackCheckBox.isSelected());
    newFilter.setBikeRack(bikeRackCheckBox.isSelected());
    newFilter.setShower(showerCheckBox.isSelected());
    newFilter.setToilet(toiletCheckBox.isSelected());
    newFilter.setKitchen(kitchenCheckBox.isSelected());
    newFilter.setFridge(fridgeCheckBox.isSelected());
    loadData(offerController.getFilteredOffers(newFilter));
  }

  public void resetFilter() throws GenericServiceException {
    locationTextField.clear();
    vehicleTypeComboBox.setValue(null);
    vehicleBrandTextField.clear();
    constructionYearTextField.clear();
    maxPricePerDayTextField.clear();
    engineTextField.clear();
    transmissionComboBox.setValue(null);
    seatAmountTextField.clear();
    bedAmountTextField.clear();
    roofTentCheckBox.setSelected(false);
    roofRackCheckBox.setSelected(false);
    bikeRackCheckBox.setSelected(false);
    showerCheckBox.setSelected(false);
    toiletCheckBox.setSelected(false);
    kitchenCheckBox.setSelected(false);
    fridgeCheckBox.setSelected(false);

    reloadData();
  }

  /**
   * Resets VehicleTypeComboBox to its initial state.
   */
  public void resetVehicleTypeComboBox() {
    vehicleTypeComboBox.valueProperty().set(null);
  }

  /**
   * Resets VehicleTypeComboBox to its initial state.
   */
  public void resetFuelTypeComboBox() {
    fuelTypeComboBox.valueProperty().set(null);
  }

  /**
   * Resets TransmissionTypeComboBox to its initial state.
   */
  public void resetTransmissionTypeComboBox() {
    transmissionComboBox.valueProperty().set(null);
  }
}
