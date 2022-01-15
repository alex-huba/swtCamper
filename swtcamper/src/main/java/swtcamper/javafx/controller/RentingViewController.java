package swtcamper.javafx.controller;

import java.util.List;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.OfferDTO;
import swtcamper.api.controller.OfferController;
import swtcamper.api.controller.PictureController;
import swtcamper.backend.entities.Filter;
import swtcamper.backend.entities.TransmissionType;
import swtcamper.backend.entities.VehicleType;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class RentingViewController {

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private OfferViewController offerViewController;

  @Autowired
  private OfferController offerController;

  @Autowired
  private PictureController pictureController;

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
  private void initialize() throws GenericServiceException {
    reloadData();

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

    offerListBox.setHgrow(offerListScroll, Priority.ALWAYS);
    offerListScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
    offerListRoot.setVgrow(offerListScroll, Priority.ALWAYS);
    offerListScroll.setPrefHeight(
      rootVBOX.getHeight() - rootAnchorPane.getHeight()
    );
  }

  /**
   * Gets all available offers from the database .
   * @throws GenericServiceException
   */
  public void reloadData() throws GenericServiceException {
    loadData(
      offerController
        .offers()
        .stream()
        .filter(OfferDTO::isActive)
        .collect(Collectors.toList())
    );
  }

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
      root.setStyle(
        "-fx-background-color: #c9dfce; -fx-background-radius: 20px"
      );
      Label promoteLabel = new Label("");
      promoteLabel.setStyle(
        "-fx-font-size: 20; -fx-font-family: Arial Rounded MT Bold;"
      );

      // visually highlight promoted offer
      if (offer.isPromoted()) {
        root.setStyle(
          "-fx-background-color: #add8e6; -fx-background-radius: 20px"
        );
        promoteLabel.setText("-- Von uns empfohlen! --");
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
    if (!engineTextField.getText().isEmpty()) newFilter.setEngine(
      engineTextField.getText()
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
   * Resets TransmissionTypeComboBox to its initial state.
   */
  public void resetTransmissionTypeComboBox() {
    transmissionComboBox.valueProperty().set(null);
  }
}
