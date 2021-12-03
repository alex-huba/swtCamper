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

  @FXML
  public ComboBox typebox;

  @FXML
  public TextField brandTextField;

  @FXML
  public TextField modelTextField;

  @FXML
  public TextField constructionYearTextField;

  @FXML
  public CheckBox minAgeCheckbox;

  @FXML
  public CheckBox borderCrossingCheckbox;

  @FXML
  public CheckBox depositCheckbox;

  @FXML
  public TextField price;
  LongStringConverter longStringConverter;

  @FXML
  public TextField width;
  DoubleStringConverter doubleStringConverter;

  @FXML
  public TextField length;

  @FXML
  public TextField height;

  @FXML
  public TextField engine;

  @FXML
  public TextField transmission;

  @FXML
  public CheckBox roofTentCheckbox;

  @FXML
  public CheckBox roofRackCheckbox;

  @FXML
  public CheckBox bikeRackcheckbox;

  @FXML
  public CheckBox showerCheckbox;

  @FXML
  public CheckBox toiletCheckbox;

  @FXML
  public CheckBox kitchenUnitCheckbox;

  @FXML
  public CheckBox fridgeCheckbox;

  /*@FXML
  public TextField contactTextfield;

  @FXML
  public TextField locationTextfield;

  @FXML
  public TextField descriptionTextfield;*/

  @FXML
  public TextField seatsTextfield;

  @FXML
  public TextField bedsTextfield;

  private void resetFields() {
  }

  @FXML
  public void placeOfferAction() {
    String[] pictureURLs = null;
    String[] particularities = null;
    VehicleType vehicleType = null;
    OfferDTO offerDTO = offerController.create(
            longStringConverter.fromString(price.getText()),
            minAgeCheckbox.isSelected(),
            borderCrossingCheckbox.isSelected(),
            depositCheckbox.isSelected(),
            pictureURLs,
            particularities,
            vehicleType,
            brandTextField.getText(),
            modelTextField.getText(),
            constructionYearTextField.getText(),
            doubleStringConverter.fromString(length.getText()),
            doubleStringConverter.fromString(width.getText()),
            doubleStringConverter.fromString(height.getText()),
            engine.getText(),
            transmission.getText(),
            Integer.parseInt(seatsTextfield.getText()),
            Integer.parseInt(bedsTextfield.getText()),
            roofTentCheckbox.isSelected(),
            roofRackCheckbox.isSelected(),
            bikeRackcheckbox.isSelected(),
            showerCheckbox.isSelected(),
            toiletCheckbox.isSelected(),
            kitchenUnitCheckbox.isSelected(),
            fridgeCheckbox.isSelected()
    );
    //resetFields();
    mainViewController.handleInformationMessage(
      String.format("New offer \"%s\" has been created.", offerDTO)
    );
    //mainViewController.jumpToTab("offers");
    //mainViewController.reloadData();
  }

  @FXML
  public void cancelAction() {
    mainViewController.jumpToTab("offers");
  }

  public void importFileChooserAction(ActionEvent actionEvent) {}

  public void importButtonAction(ActionEvent actionEvent) {}
}
