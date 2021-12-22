package swtcamper.javafx.controller;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.LongStringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.BookingDTO;
import swtcamper.api.contract.OfferDTO;
import swtcamper.api.controller.BookingController;
import swtcamper.api.controller.OfferController;
import swtcamper.api.controller.UserController;
import swtcamper.api.controller.ValidationHelper;
import swtcamper.backend.entities.Offer;
import swtcamper.backend.entities.User;
import swtcamper.backend.entities.Vehicle;
import swtcamper.backend.entities.VehicleType;
import swtcamper.backend.repositories.OfferRepository;
import swtcamper.backend.repositories.UserRepository;
import swtcamper.backend.repositories.VehicleRepository;
import swtcamper.backend.services.BookingService;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class OfferViewController {

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private BookingService bookingService;

  @Autowired
  private BookingController bookingController;

  @Autowired
  private OfferController offerController;

  @Autowired
  private VehicleRepository vehicleRepository;

  @Autowired
  private OfferRepository offerRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ValidationHelper validationHelper;

  @Autowired
  private ModifyOfferViewController modifyOfferViewController;

  @Autowired
  private UserController userController;

  private long offerID;

  private Vehicle offeredObject;

  LongStringConverter longStringConverter = new LongStringConverter();

  DoubleStringConverter doubleStringConverter = new DoubleStringConverter();

  @FXML
  public Label typeLabel;

  @FXML
  public Label brandLabel;

  @FXML
  public Label modelLabel;

  @FXML
  public Label constructionLabel;

  @FXML
  public Label minAgeLabel;

  @FXML
  public Label borderCrossingLabel;

  @FXML
  public Label depositLabel;

  @FXML
  public Label priceLabel;

  @FXML
  public Label widthLabel;

  @FXML
  public Label lengthLabel;

  @FXML
  public Label heightLabel;

  @FXML
  public Label engineLabel;

  @FXML
  public Label transmissionLabel;

  @FXML
  public Label roofTentLabel;

  @FXML
  public Label roofRackLabel;

  @FXML
  public Label bikeRackLabel;

  @FXML
  public Label showerLabel;

  @FXML
  public Label toiletLabel;

  @FXML
  public Label kitchenUnitLabel;

  @FXML
  public Label fridgeLabel;

  @FXML
  public Label contactLabel;

  @FXML
  public Label locationLabel;

  @FXML
  public Label particularitiesLabel;

  @FXML
  public Label seatsLabel;

  @FXML
  public Label bedsLabel;

  @FXML
  public Label titleLabel;

  @FXML
  public Button modifyButton;

  @FXML
  public Button bookingButton;

  @FXML
  public DatePicker startDate;

  @FXML
  public DatePicker endDate;

  public OfferDTO viewedOffer;

  private final SimpleBooleanProperty isRentingMode = new SimpleBooleanProperty();

  public void initialize(OfferDTO offer, boolean rentingMode) {
    checkMode(rentingMode);
    this.offerID = offer.getID();
    this.offeredObject = offer.getOfferedObject();
    this.viewedOffer = offer;

    titleLabel.setText(offer.getTitle());
    contactLabel.setText(offer.getContact());
    priceLabel.setText(longStringConverter.toString(offer.getPrice()));
    locationLabel.setText(offer.getLocation());
    particularitiesLabel.setText(offer.getDescription());
    minAgeLabel.setOpacity(labelOpacity(offer.isMinAge25()));
    borderCrossingLabel.setOpacity(
      labelOpacity(offer.isBorderCrossingAllowed())
    );
    depositLabel.setOpacity(labelOpacity(offer.isDepositInCash()));

    //typeLabel.setText(offeredObject.getVehicleFeatures().getType().toString());
    brandLabel.setText(offeredObject.getVehicleFeatures().getMake());
    modelLabel.setText(offeredObject.getVehicleFeatures().getModel());
    transmissionLabel.setText(
      offeredObject.getVehicleFeatures().getTransmission()
    );
    seatsLabel.setText(
      Integer.toString(offeredObject.getVehicleFeatures().getSeats())
    );
    bedsLabel.setText(
      Integer.toString(offeredObject.getVehicleFeatures().getBeds())
    );
    constructionLabel.setText(offeredObject.getVehicleFeatures().getYear());
    engineLabel.setText(offeredObject.getVehicleFeatures().getEngine());
    widthLabel.setText(
      doubleStringConverter.toString(
        offeredObject.getVehicleFeatures().getWidth()
      )
    );
    lengthLabel.setText(
      doubleStringConverter.toString(
        offeredObject.getVehicleFeatures().getLength()
      )
    );
    heightLabel.setText(
      doubleStringConverter.toString(
        offeredObject.getVehicleFeatures().getHeight()
      )
    );

    roofTentLabel.setOpacity(
      labelOpacity(offeredObject.getVehicleFeatures().isRoofTent())
    );
    roofRackLabel.setOpacity(
      labelOpacity(offeredObject.getVehicleFeatures().isRoofRack())
    );
    bikeRackLabel.setOpacity(
      labelOpacity(offeredObject.getVehicleFeatures().isBikeRack())
    );
    showerLabel.setOpacity(
      labelOpacity(offeredObject.getVehicleFeatures().isShower())
    );
    toiletLabel.setOpacity(
      labelOpacity(offeredObject.getVehicleFeatures().isToilet())
    );
    kitchenUnitLabel.setOpacity(
      labelOpacity(offeredObject.getVehicleFeatures().isKitchenUnit())
    );
    fridgeLabel.setOpacity(
      labelOpacity(offeredObject.getVehicleFeatures().isFridge())
    );
  }

  public double labelOpacity(boolean checkBox) {
    if (checkBox) {
      return 1;
    } else {
      return 0.3;
    }
  }

  public void checkMode(boolean rentingMode) {
    bookingButton.setVisible(false);
    modifyButton.setVisible(false);
    this.isRentingMode.set(rentingMode);
    if (isRentingMode.get()) {
      if (userController.getLoggedInUser() != null) {
        bookingButton.setVisible(true);
      }
      startDate.setVisible(true);
      endDate.setVisible(true);
    } else {
      modifyButton.setVisible(true);
      startDate.setVisible(false);
      endDate.setVisible(false);
    }
  }

  @FXML
  public void backAction() throws GenericServiceException {
    if (isRentingMode.get()) {
      mainViewController.changeView("home");
    } else {
      mainViewController.changeView("activeOffers");
    }
  }

  @FXML
  public void modifyAction() throws GenericServiceException {
    mainViewController.changeView("placeOffer");
    modifyOfferViewController.initialize(viewedOffer);
  }

  @FXML
  public void bookingAction() throws GenericServiceException {
    if (startDate.getValue() != null && endDate.getValue() != null) {
      if (
        !validationHelper.checkRentingDate(
          startDate.getValue(),
          endDate.getValue()
        )
      ) {
        Alert confirmBooking = new Alert(
          Alert.AlertType.WARNING,
          "Willst du das Angebot wirklich von " +
          startDate.getValue() +
          " bis " +
          endDate.getValue() +
          " buchen?"
        );
        Optional<ButtonType> result = confirmBooking.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
          Offer offer = offerController.offerDTOToOffer(viewedOffer);
          User user = userController.getLoggedInUser();
          BookingDTO bookingDTO = bookingController.create(
            user,
            offer,
            startDate.getValue(),
            endDate.getValue(),
            true
          );
          mainViewController.handleInformationMessage(
            "Buchungsanfrage verschickt. Buchungsnummer: " + bookingDTO.getId()
          );
        }
      } else {
        mainViewController.handleExceptionMessage(
          "Bitte wähle ein korrektes Datum aus!"
        );
      }
    } else {
      mainViewController.handleExceptionMessage(
        "Bitte wähle ein korrektes Datum aus!"
      );
    }
  }
}
