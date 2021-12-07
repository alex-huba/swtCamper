package swtcamper.javafx.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.LongStringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.OfferDTO;
import swtcamper.api.controller.OfferController;
import swtcamper.backend.entities.VehicleType;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

@Component
public class PlaceOfferViewController implements EventHandler<KeyEvent> {

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private OfferController offerController;

  DoubleStringConverter doubleStringConverter = new DoubleStringConverter();

  LongStringConverter longStringConverter = new LongStringConverter();

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
  public CheckBox minAgeCheckBox;

  @FXML
  public CheckBox borderCrossingCheckBox;

  @FXML
  public CheckBox depositCheckBox;

  @FXML
  public TextField priceTextField;

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

  @FXML
  public Button placeOfferButton;

  boolean isPriceOk = false;
  boolean isBrandOk = false;
  boolean isModelOk = false;
  boolean isSeatsOk = false;
  boolean isBedsOk = false;

  @FXML
  public void initialize() {
    resetFields();
    priceTextField.setOnKeyTyped(this);
    brandTextField.setOnKeyTyped(this);
    modelTextField.setOnKeyTyped(this);
    seatsTextField.setOnKeyTyped(this);
    bedsTextField.setOnKeyTyped(this);
    placeOfferButton.setDisable(true);
  }

  @Override
  public void handle(KeyEvent event) {
    validateOfferInput(event);
  }

  private void resetFields() {
    typeBox.setItems(FXCollections.observableArrayList(VehicleType.values()));
    brandTextField.clear();
    modelTextField.clear();
    constructionYearTextField.clear();
    minAgeCheckBox.setSelected(false);
    borderCrossingCheckBox.setSelected(false);
    depositCheckBox.setSelected(false);
    priceTextField.clear();
    widthTextField.clear();
    lengthTextField.clear();
    heightTextField.clear();
    engineTextField.clear();
    transmissionTextField.clear();
    roofTentCheckBox.setSelected(false);
    roofRackCheckBox.setSelected(false);
    bikeRackCheckBox.setSelected(false);
    showerCheckBox.setSelected(false);
    toiletCheckBox.setSelected(false);
    kitchenUnitCheckBox.setSelected(false);
    fridgeCheckBox.setSelected(false);
    seatsTextField.clear();
    bedsTextField.clear();
    isPriceOk = false;
    isBrandOk = false;
    isSeatsOk = false;
    isBedsOk = false;
    isModelOk = false;
    placeOfferButton.setDisable(true);
  }

  @FXML
  public void placeOfferAction() {
    String[] pictureURLs = null;
    String[] particularities = null;
    double length = doubleStringConverter.fromString(lengthTextField.getText());
    double width = doubleStringConverter.fromString(widthTextField.getText());
    double height = doubleStringConverter.fromString(heightTextField.getText());
      OfferDTO offerDTO = offerController.create(
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

      mainViewController.handleInformationMessage(
              String.format("New offer \"%s\" has been created.", offerDTO.getID())
      );
      resetFields();
      mainViewController.changeView("activeOffers");
      mainViewController.reloadData();
  }

  @FXML
  public void cancelAction() {
    mainViewController.changeView("activeOffers");
  }



  private void validateOfferInput (KeyEvent event) {
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
          priceTextField.setBackground(new Background(new BackgroundFill(Color.LIGHTPINK, CornerRadii.EMPTY, Insets.EMPTY)));
          isPriceOk = false;
        } else {
          errorLabel.setText("");
          priceTextField.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
          isPriceOk = true;
        }
        break;
      case "brand":
        String inputBrand = brandTextField.getText();
        if (inputBrand.isEmpty()) {
          errorLabel.setText("Invalid brand");
          brandTextField.setBackground(new Background(new BackgroundFill(Color.LIGHTPINK, CornerRadii.EMPTY, Insets.EMPTY)));
          isBrandOk = false;
        } else {
          errorLabel.setText("");
          brandTextField.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
          isBrandOk = true;
        }
        break;
      case "model":
        String inputModel = modelTextField.getText();
        if (inputModel.isEmpty()) {
          errorLabel.setText("Invalid model");
          modelTextField.setBackground(new Background(new BackgroundFill(Color.LIGHTPINK, CornerRadii.EMPTY, Insets.EMPTY)));
          isModelOk = false;
        } else {
          errorLabel.setText("");
          modelTextField.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
          isModelOk = true;
        }
        break;
      case "seats":
        String inputSeats = seatsTextField.getText();
        if (inputSeats.isEmpty() || !inputSeats.matches("[0-9]*")) {
          errorLabel.setText("Invalid seats");
          seatsTextField.setBackground(new Background(new BackgroundFill(Color.LIGHTPINK, CornerRadii.EMPTY, Insets.EMPTY)));
          isSeatsOk = false;
        } else {
          errorLabel.setText("");
          seatsTextField.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
          isSeatsOk = true;
        }
        break;
      case "beds":
        String inputBeds = bedsTextField.getText();
        if (inputBeds.isEmpty() || !inputBeds.matches("[0-9]*")) {
          errorLabel.setText("Invalid beds");
          bedsTextField.setBackground(new Background(new BackgroundFill(Color.LIGHTPINK, CornerRadii.EMPTY, Insets.EMPTY)));
          isBedsOk = false;
        } else {
          errorLabel.setText("");
          bedsTextField.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
          isBedsOk = true;
        }
        break;
    }
    if (isPriceOk && isBrandOk && isModelOk && isSeatsOk && isBedsOk) {
      placeOfferButton.setDisable(false);
    } else {
      placeOfferButton.setDisable(true);
    }

  }

  public void importFileChooserAction(ActionEvent actionEvent) {}

  public void importButtonAction(ActionEvent actionEvent) {}
}
