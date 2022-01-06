package swtcamper.javafx.controller;

import java.util.Optional;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
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
import swtcamper.backend.entities.*;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class OfferViewController {

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private BookingController bookingController;

  @Autowired
  private OfferController offerController;

  @Autowired
  private ValidationHelper validationHelper;

  @Autowired
  private ModifyOfferViewController modifyOfferViewController;

  @Autowired
  private UserController userController;

  LongStringConverter longStringConverter = new LongStringConverter();

  DoubleStringConverter doubleStringConverter = new DoubleStringConverter();

  @FXML
  public Label vehicleTypeLabel;

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
  public Label dateLabel;

  @FXML
  public DatePicker startDate;

  @FXML
  public DatePicker endDate;

  @FXML
  public HBox rentHBox;

  @FXML
  public Label rentLabel;

  @FXML
  public Button abortBookingRequestBtn;

  public OfferDTO viewedOffer;
  private final SimpleBooleanProperty isRentingMode = new SimpleBooleanProperty();

  public void initialize(OfferDTO offer, boolean rentingMode) {
    this.viewedOffer = offer;
    checkMode(rentingMode);
    Vehicle offeredObject = offer.getOfferedObject();

    titleLabel.setText(offer.getTitle());
    contactLabel.setText(offer.getContact());
    priceLabel.setText(longStringConverter.toString(offer.getPrice()));
    locationLabel.setText(offer.getLocation());
    particularitiesLabel.setText(offer.getParticularities());
    minAgeLabel.setOpacity(labelOpacity(offer.isMinAge25()));
    borderCrossingLabel.setOpacity(
      labelOpacity(offer.isBorderCrossingAllowed())
    );
    depositLabel.setOpacity(labelOpacity(offer.isDepositInCash()));

    vehicleTypeLabel.setText(
      String.valueOf(offeredObject.getVehicleFeatures().getVehicleType())
    );
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

  private double labelOpacity(boolean checkBox) {
    if (checkBox) {
      return 1;
    } else {
      return 0.3;
    }
  }

  private void checkMode(boolean rentingMode) {
    // visible
    bookingButton.setVisible(false);
    modifyButton.setVisible(false);
    dateLabel.setVisible(false);
    startDate.setVisible(false);
    endDate.setVisible(false);
    rentHBox.setVisible(false);
    // disable
    dateLabel.setDisable(false);
    startDate.setDisable(false);
    endDate.setDisable(false);

    isRentingMode.set(rentingMode);
    if (isRentingMode.get()) {
      if (userController.getLoggedInUser() != null) {
        // remove ability to book own offer
        if (
          viewedOffer
            .getCreator()
            .getId()
            .equals(userController.getLoggedInUser().getId())
        ) {
          // ... but make it possible to make changes then
          modifyButton.setVisible(true);
          // ... if it's not booked right now
          boolean offerIsInRent = false;
          for (Booking booking : bookingController.getBookingsForUser(
            userController.getLoggedInUser()
          )) {
            if (
              booking.getOffer().getOfferID() == viewedOffer.getID() &&
              booking.isActive()
            ) {
              offerIsInRent = true;
              break;
            }
          }
          modifyButton.setDisable(offerIsInRent);
        } else {
          bookingButton.setVisible(true);
          dateLabel.setVisible(true);
          startDate.setVisible(true);
          endDate.setVisible(true);
        }

        // remove possibility to send a request twice at once
        for (Booking booking : bookingController
          .getAllBookings()
          .stream()
          .filter(booking ->
            booking
              .getRenter()
              .getId()
              .equals(userController.getLoggedInUser().getId())
          )
          .collect(Collectors.toList())) {
          if (booking.getOffer().getOfferID() == viewedOffer.getID()) {
            bookingButton.setVisible(false);
            dateLabel.setDisable(true);
            startDate.setDisable(true);
            endDate.setDisable(true);
            rentLabel.setText(
              "Buchungsanfrage verschickt. Buchungsnummer: " + booking.getId()
            );
            rentHBox.setVisible(true);

            // abort open booking request
            abortBookingRequestBtn.setOnAction(event -> {
              try {
                bookingController.delete(booking.getId());
                checkMode(true);
              } catch (GenericServiceException e) {
                mainViewController.handleExceptionMessage(e.getMessage());
              }
            });
          }
        }
      }
    } else {
      modifyButton.setVisible(true);
      dateLabel.setVisible(false);
      startDate.setVisible(false);
      endDate.setVisible(false);
      rentHBox.setVisible(false);
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
          Alert.AlertType.CONFIRMATION,
          "Willst du das Angebot wirklich von " +
          startDate.getValue() +
          " bis " +
          endDate.getValue() +
          " buchen?"
        );
        Optional<ButtonType> result = confirmBooking.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
          Offer offer = offerController.getOfferById(viewedOffer.getID());
          User user = userController.getLoggedInUser();
          BookingDTO bookingDTO = bookingController.create(
            user,
            offer,
            startDate.getValue(),
            endDate.getValue(),
            false
          );
          checkMode(true);
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
