package swtcamper.javafx.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.OfferDTO;
import swtcamper.api.contract.interfaces.IOfferController;
import swtcamper.api.contract.interfaces.IPictureController;
import swtcamper.api.contract.interfaces.IUserController;
import swtcamper.backend.entities.Filter;
import swtcamper.backend.entities.FuelType;
import swtcamper.backend.entities.TransmissionType;
import swtcamper.backend.entities.VehicleType;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class RentingViewController {

  @FXML
  private TextField locationTextField;

  @FXML
  private ComboBox<VehicleType> vehicleTypeComboBox;

  @FXML
  private Button resetVehicleTypeBtn;

  @FXML
  private TextField vehicleBrandTextField;

  @FXML
  private TextField constructionYearTextField;

  @FXML
  private TextField maxPricePerDayTextField;

  @FXML
  private ComboBox<FuelType> fuelTypeComboBox;

  @FXML
  private Button resetFuelTypeBtn;

  @FXML
  private ComboBox<TransmissionType> transmissionComboBox;

  @FXML
  private Button resetTransmissionTypeBtn;

  @FXML
  private ComboBox<Integer> seatAmountComboBox;

  @FXML
  private Button resetSeatAmountBtn;

  @FXML
  private ComboBox<Integer> bedAmountComboBox;

  @FXML
  private Button resetBedAmountBtn;

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
  private CheckBox kitchenCheckBox;

  @FXML
  private CheckBox fridgeCheckBox;

  @FXML
  private DatePicker startDatePicker;

  @FXML
  private Button resetStartDatePickerBtn;

  @FXML
  private DatePicker endDatePicker;

  @FXML
  private Button resetEndDatePickerBtn;

  @FXML
  private HBox paginationHBox;

  @FXML
  private HBox paginationButtonsHBox;

  @FXML
  private ChoiceBox<Integer> offersPerPageChoiceBox;

  @FXML
  private HBox offersPerPageHBox;

  @FXML
  private HBox offerListBox;

  @FXML
  private ScrollPane offerListScroll;

  @FXML
  private VBox offerListRoot;

  @FXML
  private VBox rootVBOX;

  @FXML
  private AnchorPane rootAnchorPane;

  /**
   * needed for pagination
   */
  private int lastPageVisited;

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private OfferViewController offerViewController;

  @Autowired
  private IOfferController offerController;

  @Autowired
  private IPictureController pictureController;

  @Autowired
  private IUserController userController;

  private List<OfferDTO> offerDTOList;
  private List<List<OfferDTO>> subListsList;

  @FXML
  private void initialize() throws GenericServiceException {
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
    resetStartDatePickerBtn
      .visibleProperty()
      .bind(startDatePicker.valueProperty().isNotNull());
    resetEndDatePickerBtn
      .visibleProperty()
      .bind(endDatePicker.valueProperty().isNotNull());
    resetSeatAmountBtn
      .visibleProperty()
      .bind(seatAmountComboBox.valueProperty().isNotNull());
    resetBedAmountBtn
      .visibleProperty()
      .bind(bedAmountComboBox.valueProperty().isNotNull());

    startDatePicker.getEditor().setDisable(true);
    startDatePicker.getEditor().setOpacity(1);
    endDatePicker.getEditor().setDisable(true);
    endDatePicker.getEditor().setOpacity(1);
    startDatePicker.setDayCellFactory(
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
    endDatePicker.setDayCellFactory(
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

    seatAmountComboBox.setButtonCell(
      new ListCell<>() {
        @Override
        protected void updateItem(Integer item, boolean empty) {
          super.updateItem(item, empty);
          if (empty || item == null) {
            setText(seatAmountComboBox.getPromptText());
          } else {
            setText(item.toString());
          }
        }
      }
    );

    bedAmountComboBox.setButtonCell(
      new ListCell<>() {
        @Override
        protected void updateItem(Integer item, boolean empty) {
          super.updateItem(item, empty);
          if (empty || item == null) {
            setText(bedAmountComboBox.getPromptText());
          } else {
            setText(item.toString());
          }
        }
      }
    );

    seatAmountComboBox.setItems(
      FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    );
    bedAmountComboBox.setItems(
      FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    );

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

    HBox.setHgrow(offerListScroll, Priority.ALWAYS);
    offerListScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
    VBox.setVgrow(offerListScroll, Priority.ALWAYS);
    offerListScroll.setPrefHeight(
      rootVBOX.getHeight() - rootAnchorPane.getHeight()
    );

    // make it impossible to enter characters instead of numbers for number fields
    constructionYearTextField
      .textProperty()
      .addListener((observable, oldValue, newValue) -> {
        if (!newValue.matches("\\d*")) {
          constructionYearTextField.setText(newValue.replaceAll("[^\\d]", ""));
        }
      });

    maxPricePerDayTextField
      .textProperty()
      .addListener((observable, oldValue, newValue) -> {
        if (!newValue.matches("\\d*")) {
          maxPricePerDayTextField.setText(newValue.replaceAll("[^\\d]", ""));
        }
      });
  }

  /**
   * Gets all available offers from the database and creates subLists.
   *
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

    // made pagination invisible if there are no offers to show
    paginationHBox.setVisible(!offerDTOList.isEmpty());

    // partition them according to offersPerPageChoiceBox's value
    subListsList =
      createOfferSublists(offerDTOList, offersPerPageChoiceBox.getValue());
    // and load the first chunk
    loadData(!subListsList.isEmpty() ? subListsList.get(0) : new ArrayList<>());
  }

  /**
   * Adds a pagination to the list of offers
   */
  private void addPagination() {
    // get amount of pages by rounding up the result of offerDTOList's size divided by offersPerPageChoiceBox's value
    int pageAmount = (int) Math.ceil(
      offerDTOList.size() / Double.valueOf(offersPerPageChoiceBox.getValue())
    );

    paginationButtonsHBox.getChildren().clear();
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
      paginationButtonsHBox.getChildren().add(pageButton);
    }

    offerListRoot.getChildren().add(paginationHBox);
  }

  /**
   * Partitions an offer-list into sublists of a specific max length
   *
   * @param inputList offer-list to partition
   * @param size      max length of each sublist
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
   *
   * @param offersList list to load
   */
  private void loadData(List<OfferDTO> offersList) {
    offerListRoot.getChildren().clear();

    Label header = new Label(
      String.format(
        "%s Angebote für Sie",
        !offersList.isEmpty()
          ? (offerDTOList.size() + " Passende")
          : "Keine passenden"
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

      root.setEffect(new DropShadow(10, Color.BLACK));

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
      thumbnail.setFitWidth(150);
      thumbnail.setPreserveRatio(true);
      HBox thumbnailHbox = new HBox(thumbnail);
      thumbnailHbox.setPrefWidth(150);
      thumbnailHbox.setPrefHeight(150);
      thumbnailHbox.setAlignment(Pos.BASELINE_CENTER);
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
        "Marke: " + offer.getOfferedObject().getMake()
      );
      brandLabel.setStyle(
        "-fx-font-size: 20; -fx-font-family: \"Arial Rounded MT Bold\";"
      );

      // model
      Label modelLabel = new Label(
        "Modell: " + offer.getOfferedObject().getModel()
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
    if (!offersList.isEmpty()) addPagination();
  }

  /**
   * Creates a new Filter Object and gets all offers from the database that fit to this filter.
   *
   * @throws GenericServiceException
   */
  public void startSearch() throws GenericServiceException {
    Filter newFilter = new Filter();
    try {
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
      if (
        transmissionComboBox.getValue() != null
      ) newFilter.setTransmissionType(transmissionComboBox.getValue());
      if (seatAmountComboBox.getValue() != null) newFilter.setSeatAmount(
        seatAmountComboBox.getValue()
      );
      if (bedAmountComboBox.getValue() != null) newFilter.setBedAmount(
        bedAmountComboBox.getValue()
      );
      if (startDatePicker.getValue() != null) newFilter.setStartDate(
        startDatePicker.getValue()
      );
      if (endDatePicker.getValue() != null) newFilter.setEndDate(
        endDatePicker.getValue()
      );
    } catch (NumberFormatException e) {
      mainViewController.handleExceptionMessage(
        "Bitte nutze für die Suche realistische Zahlen."
      );
    }

    newFilter.setRoofTent(roofTentCheckBox.isSelected());
    newFilter.setRoofRack(roofRackCheckBox.isSelected());
    newFilter.setBikeRack(bikeRackCheckBox.isSelected());
    newFilter.setShower(showerCheckBox.isSelected());
    newFilter.setToilet(toiletCheckBox.isSelected());
    newFilter.setKitchen(kitchenCheckBox.isSelected());
    newFilter.setFridge(fridgeCheckBox.isSelected());

    // get filtered offers and save them as (global) offerDTOList
    offerDTOList = offerController.getFilteredOffers(newFilter);
    // partition them according to offersPerPageChoiceBox's value
    subListsList =
      createOfferSublists(offerDTOList, offersPerPageChoiceBox.getValue());
    // and load the first chunk
    loadData(!subListsList.isEmpty() ? subListsList.get(0) : new ArrayList<>());
  }

  public void resetFilter() throws GenericServiceException {
    locationTextField.clear();
    vehicleTypeComboBox.setValue(null);
    vehicleBrandTextField.clear();
    constructionYearTextField.clear();
    maxPricePerDayTextField.clear();
    transmissionComboBox.setValue(null);
    fuelTypeComboBox.setValue(null);
    seatAmountComboBox.setValue(null);
    bedAmountComboBox.setValue(null);
    roofTentCheckBox.setSelected(false);
    roofRackCheckBox.setSelected(false);
    bikeRackCheckBox.setSelected(false);
    showerCheckBox.setSelected(false);
    toiletCheckBox.setSelected(false);
    kitchenCheckBox.setSelected(false);
    fridgeCheckBox.setSelected(false);
    startDatePicker.setValue(null);
    endDatePicker.setValue(null);

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

  /**
   * resets the startDate DatePicker
   */
  public void resetStartDatePicker() {
    startDatePicker.getEditor().clear();
    startDatePicker.setValue(null);
  }

  /**
   * resets the endDate DatePicker
   */
  public void resetEndDatePicker() {
    endDatePicker.getEditor().clear();
    endDatePicker.setValue(null);
  }

  /**
   * resets the seat amount in the filter
   */
  public void resetSeatAmountComboBox() {
    seatAmountComboBox.setValue(null);
  }

  /**
   * resets the bed amount in the filter
   */
  public void resetBedAmountComboBox() {
    bedAmountComboBox.setValue(null);
  }
}
