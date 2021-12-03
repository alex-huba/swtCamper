package swtcamper.javafx.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.LongStringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.OfferDTO;
import swtcamper.api.controller.OfferController;
import swtcamper.backend.entities.Offer;
import swtcamper.backend.entities.Vehicle;
import swtcamper.backend.entities.VehicleFeatures;
import swtcamper.backend.entities.VehicleType;
import swtcamper.backend.repositories.OfferRepository;
import swtcamper.backend.repositories.VehicleFeaturesRepository;
import swtcamper.backend.repositories.VehicleRepository;
import swtcamper.backend.services.OfferService;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class UpdateOfferViewController {

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private OfferController offerController;

  @Autowired
  OfferService offerService;

  private long offerId;

  private long offeredObjectId;

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
  public CheckBox activeCheckbox;

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

  @FXML
  public TextField seatsTextfield;

  @FXML
  public TextField bedsTextfield;



  public void initialize(OfferDTO offer) {
    this.offerId = offer.getID();
    this.offeredObjectId = offer.getOfferedObjectID();
    // TODO alle Inhalte der Felder setten
//    this.titleTextField.setText(offer.getTitle());
//    this.pricePerDayTextField.setText(offer.getPricePerDay());

  }
  @FXML
  public void updateOfferAction() throws GenericServiceException {
    // TODO Boxen dafür anlegen
    String[] pictureURLs = null;
    String[] particularities = null;
    VehicleType vehicleType = null;
    ArrayList<Long> bookings = null;

    offerController.update(
            offerId,
            offeredObjectId,
            bookings,
            longStringConverter.fromString(price.getText()),
            activeCheckbox.isSelected(),
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
            // TODO alle sachen aus den boxen
    );

    mainViewController.updateOfferTab.setDisable(true);
    mainViewController.jumpToTab("offers");
    mainViewController.reloadData();
  }

  @FXML
  public void cancelUpdateAction() {
    mainViewController.jumpToTab("offers");
    mainViewController.updateOfferTab.setDisable(true);
  }

  public void importFileChooserAction(ActionEvent event) {
    Node source = (Node) event.getSource();
    Window window = source.getScene().getWindow();

    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Resource File");
    File file = fileChooser.showOpenDialog(window);
    if (file == null) {
      return;
    }
    importPath.setText(file.getAbsolutePath().toString());
  }

  @FXML
  public TextField importPath;

  public void importButtonAction() {
    String importPathString = importPath.getText();
    if (importPathString.isEmpty()) {
      mainViewController.handleExceptionMessage(
        "The prior selected file path is empty."
      );
      return;
    }
    Path path = Paths.get(importPathString);
    if (!Files.exists(path)) {
      mainViewController.handleExceptionMessage(
        String.format(
          "The prior selected file \"%s\" does not exists.",
          path.toAbsolutePath()
        )
      );
      return;
    }

    String json;
    try {
      json = Files.readString(path);
    } catch (IOException e) {
      mainViewController.handleExceptionMessage(
        String.format(
          "An error occurred while trying to read file %s",
          path.toAbsolutePath()
        )
      );
      return;
    }
    try {
      //            personController.importPeopleFromJSON(json);
      mainViewController.handleInformationMessage("The import was successful!");
      mainViewController.reloadData();
    } catch (Exception e) {
      mainViewController.handleException(e);
      return;
    }
  }
}
