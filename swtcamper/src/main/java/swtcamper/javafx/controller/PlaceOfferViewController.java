package swtcamper.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.LongStringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.OfferDTO;
import swtcamper.api.controller.OfferController;
import swtcamper.backend.entities.VehicleType;

@Component
public class PlaceOfferViewController {

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private OfferController offerController;

  DoubleStringConverter doubleStringConverter = new DoubleStringConverter();

  LongStringConverter longStringConverter = new LongStringConverter();

  @FXML
  public ComboBox typeBox;

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

  private void resetFields() {
    //typeBox
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
  }

  @FXML
  public void placeOfferAction() {
    // TODO entsprechende Felder noch anlegen
    String[] pictureURLs = null;
    String[] particularities = null;
    VehicleType vehicleType = null;
    OfferDTO offerDTO = offerController.create(
            longStringConverter.fromString(priceTextField.getText()),
            minAgeCheckBox.isSelected(),
            borderCrossingCheckBox.isSelected(),
            depositCheckBox.isSelected(),
            pictureURLs,
            particularities,
            vehicleType,
            brandTextField.getText(),
            modelTextField.getText(),
            constructionYearTextField.getText(),
            doubleStringConverter.fromString(lengthTextField.getText()),
            doubleStringConverter.fromString(widthTextField.getText()),
            doubleStringConverter.fromString(heightTextField.getText()),
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
    mainViewController.jumpToTab("offers");
    mainViewController.reloadData();
  }

  @FXML
  public void cancelAction() {
    mainViewController.jumpToTab("offers");
  }

  public void importFileChooserAction(ActionEvent actionEvent) {}

  public void importButtonAction(ActionEvent actionEvent) {}
}
