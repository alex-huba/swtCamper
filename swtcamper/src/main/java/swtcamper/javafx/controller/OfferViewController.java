package swtcamper.javafx.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.LongStringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.BookingDTO;
import swtcamper.api.contract.OfferDTO;
import swtcamper.api.contract.PictureDTO;
import swtcamper.api.controller.*;
import swtcamper.backend.entities.*;
import swtcamper.backend.services.BookingService;
import swtcamper.backend.services.OfferService;
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
  private PictureController pictureController;

  @Autowired
  private ValidationHelper validationHelper;

  @Autowired
  private ModifyOfferViewController modifyOfferViewController;

  @Autowired
  private UserController userController;

  @Autowired
  private BookingService bookingService;

  @Autowired
  private OfferService offerService;

  LongStringConverter longStringConverter = new LongStringConverter();

  DoubleStringConverter doubleStringConverter = new DoubleStringConverter();

  @FXML
  public HBox pictureHorizontHBox;

  @FXML
  public Label vehicleTypeLabel;

  @FXML
  public Label brandLabel;

  @FXML
  public Label modelLabel;

  @FXML
  public Label constructionLabel;

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
  public VBox rentalConditionsVBox;

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
  public Button promotingButton;

  @FXML
  public Label dateLabel;

  @FXML
  public DatePicker startDatePicker;

  @FXML
  public DatePicker endDatePicker;

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
    // enable button to promote / degrade offer
    // ...only if user role is OPERATOR
    checkUserRole();
    checkOfferStatus();

    Vehicle offeredObject = offer.getOfferedObject();

    pictureHorizontHBox.getChildren().clear();
    for (PictureDTO pictureDTO : pictureController.getPicturesForVehicle(
      offer.getOfferedObject().getVehicleID()
    )) {
      ImageView thumbnail = new ImageView(new Image(pictureDTO.getPath()));
      thumbnail.setFitHeight(150);
      thumbnail.setPreserveRatio(true);

      pictureHorizontHBox.getChildren().add(thumbnail);
    }

    titleLabel.setText(offer.getTitle());
    contactLabel.setText(offer.getContact());
    priceLabel.setText(longStringConverter.toString(offer.getPrice()));
    locationLabel.setText(offer.getLocation());
    particularitiesLabel.setText(offer.getParticularities());

    // show rental conditions
    rentalConditionsVBox.getChildren().clear();
    if (
      offer.getRentalConditions() != null &&
      offer.getRentalConditions().size() > 0
    ) {
      for (String rentalCondition : offer.getRentalConditions()) {
        rentalConditionsVBox
          .getChildren()
          .add(new Label("- " + rentalCondition));
      }
    } else {
      rentalConditionsVBox.getChildren().add(new Label(" / "));
    }

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
    engineLabel.setText(
      String.valueOf(offeredObject.getVehicleFeatures().getFuelType())
    );
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
    startDatePicker.getEditor().setDisable(true);
    startDatePicker.getEditor().setOpacity(1);
    endDatePicker.getEditor().setDisable(true);
    endDatePicker.getEditor().setOpacity(1);
    startDatePicker.setValue(null);
    endDatePicker.setValue(null);
    setCellFactory(startDatePicker, viewedOffer);
    setCellFactory(endDatePicker, viewedOffer);
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
    startDatePicker.setVisible(false);
    endDatePicker.setVisible(false);
    rentHBox.setVisible(false);
    // disable
    dateLabel.setDisable(false);
    startDatePicker.setDisable(false);
    endDatePicker.setDisable(false);

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
              booking.isActive() &&
              !booking.isRejected()
            ) {
              offerIsInRent = true;
              break;
            }
          }
          modifyButton.setDisable(offerIsInRent);
        } else {
          bookingButton.setVisible(true);
          dateLabel.setVisible(true);
          startDatePicker.setVisible(true);
          endDatePicker.setVisible(true);
        }

        // remove possibility to send a request twice at once
        for (Booking booking : bookingController
          .getAllBookings()
          .stream()
          .filter(booking -> !booking.isRejected())
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
            startDatePicker.setDisable(true);
            endDatePicker.setDisable(true);
            rentHBox.setVisible(true);

            if (booking.isActive()) {
              rentLabel.setText(
                      "Du mietest diese Anzeige gerade. Buchungsnummer: " +
                              booking.getId()
              );

              // abort renting
              abortBookingRequestBtn.setText("Buchung abbrechen");
              abortBookingRequestBtn.setOnAction(event -> {
                bookingController.reject(booking.getId());
                checkMode(true);
              });
            } else {
              rentLabel.setText(
                      "Buchungsanfrage verschickt. Buchungsnummer: " + booking.getId()
              );

              // abort open booking request
              abortBookingRequestBtn.setText("Anfrage abbrechen");
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
      }
    } else {
      modifyButton.setVisible(true);
      dateLabel.setVisible(false);
      startDatePicker.setVisible(false);
      endDatePicker.setVisible(false);
      rentHBox.setVisible(false);
    }
  }

  /**
   * Makes promote / degrade offer button visible, only if operator is logged in.
   */
  public void checkUserRole() {
    if (
      userController.getLoggedInUser() != null &&
      userController.getLoggedInUser().getUserRole().equals(UserRole.OPERATOR)
    ) {
      promotingButton.setVisible(true);
    } else {
      promotingButton.setVisible(false);
    }
  }

  /**
   * Checks if offer is promoted and sets button text accordingly.
   */
  public void checkOfferStatus() {
    if (this.viewedOffer.isPromoted()) {
      promotingButton.setText("Nicht mehr hervorheben");
    } else {
      promotingButton.setText("Angebot hervorheben");
    }
  }

  @FXML
  public void promotingAction() throws GenericServiceException {
    if (this.viewedOffer.isPromoted()) {
      offerController.degradeOffer(this.viewedOffer.getID());
    } else {
      offerController.promoteOffer(this.viewedOffer.getID());
    }
    backAction();
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
    if (
      startDatePicker.getValue() != null && endDatePicker.getValue() != null
    ) {
      // Liegt Startdatum nach Enddatum?
      // Startdatum == Enddatum?
      if (
        !ValidationHelper.checkRentingDates(
          startDatePicker.getValue(),
          endDatePicker.getValue()
        )
      ) {
        mainViewController.handleExceptionMessage(
          "Das Startdatum darf nicht nach oder am selben Tag wie das Enddatum liegen!"
        );
        // Gibt es gebuchte Tage zwischen Start- und Enddatum?
      } else if (
        !ValidationHelper.checkRentingDatesWithOffer(
          startDatePicker.getValue(),
          endDatePicker.getValue(),
          this.viewedOffer,
          bookingService,
          offerService,
          mainViewController
        )
      ) {
        mainViewController.handleExceptionMessage(
          "Zwischen Start- und Enddatum darf keine andere Buchung liegen!"
        );
        // Alles ok!
      } else {
        Alert confirmBooking = new Alert(
          Alert.AlertType.CONFIRMATION,
          "Willst du das Angebot wirklich von " +
          startDatePicker.getValue() +
          " bis " +
          endDatePicker.getValue() +
          " buchen?"
        );
        Optional<ButtonType> result = confirmBooking.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
          Offer offer = offerController.getOfferById(viewedOffer.getID());
          User user = userController.getLoggedInUser();
          try {
            BookingDTO bookingDTO = bookingController.create(
              user,
              offer,
              startDatePicker.getValue(),
              endDatePicker.getValue()
            );
          } catch (GenericServiceException e) {
            mainViewController.handleExceptionMessage(e.getMessage());
          }
          checkMode(true);
        }
      }
    } else {
      mainViewController.handleExceptionMessage(
        "Bitte wähle ein korrektes Datum aus!"
      );
    }
  }

  /**
   * Creates and sets a cellFactory for the given DatePicker, which makes all days before today un-clickable and
   * all blockedDays and bookedDays pink and un-clickable
   * @param datePicker
   * @param offerDTO
   */
  private void setCellFactory(DatePicker datePicker, OfferDTO offerDTO) {
    try {
      final List<LocalDate> bookedDays = bookingService.getBookedDays(
        offerDTO.getID()
      );
      final List<LocalDate> blockedDates = offerService.getBlockedDates(
        offerDTO.getID()
      );
      datePicker.setDayCellFactory(
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
                  if (
                    bookedDays.contains(date) || blockedDates.contains(date)
                  ) {
                    // Aussehen und Verhalten der Zellen setzen
                    this.setStyle("-fx-background-color: pink");
                    setDisable(true);
                  }
                }
              }
            };
          }
        }
      );
    } catch (GenericServiceException e) {
      mainViewController.handleExceptionMessage(e.getMessage());
    }
  }
}
