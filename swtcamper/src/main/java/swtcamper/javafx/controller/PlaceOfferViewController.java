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
import swtcamper.backend.entities.TransmissionType;
import swtcamper.backend.entities.VehicleType;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class PlaceOfferViewController {

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private OfferController offerController;

  DoubleStringConverter doubleStringConverter = new DoubleStringConverter();

  LongStringConverter longStringConverter = new LongStringConverter();

  @FXML
  public ComboBox<VehicleType> typeBox;

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
  public ComboBox<TransmissionType> transmissionTextField;

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
  public void initialize() {
    resetFields();
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
    transmissionTextField.setItems(
      FXCollections.observableArrayList(TransmissionType.values())
    );
    roofTentCheckBox.setSelected(false);
    roofRackCheckBox.setSelected(false);
    bikeRackCheckBox.setSelected(false);
    showerCheckBox.setSelected(false);
    toiletCheckBox.setSelected(false);
    kitchenUnitCheckBox.setSelected(false);
    fridgeCheckBox.setSelected(false);
    seatsTextField.clear();
    bedsTextField.clear();
  }

  @FXML
  public void placeOfferAction() throws GenericServiceException {
    String[] pictureURLs = null;
    String[] particularities = null;

    OfferDTO offerDTO = offerController.create(
      // Offer-Parameter
      longStringConverter.fromString(priceTextField.getText()),
      minAgeCheckBox.isSelected(),
      borderCrossingCheckBox.isSelected(),
      depositCheckBox.isSelected(),
      //Vehicle-Parameter
      pictureURLs,
      particularities,
      //VehicleFeatures-Parameter
      typeBox.getValue(),
      brandTextField.getText(),
      modelTextField.getText(),
      constructionYearTextField.getText(),
      doubleStringConverter.fromString(lengthTextField.getText()),
      doubleStringConverter.fromString(widthTextField.getText()),
      doubleStringConverter.fromString(heightTextField.getText()),
      engineTextField.getText(),
      transmissionTextField.getValue().toString(),
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
  public void cancelAction() throws GenericServiceException {
    mainViewController.changeView("activeOffers");
  }

  public void importFileChooserAction(ActionEvent actionEvent) {}

  public void importButtonAction(ActionEvent actionEvent) {}
}
