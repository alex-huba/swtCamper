package swtcamper.javafx.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.LongStringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.BookingDTO;
import swtcamper.api.contract.OfferDTO;
import swtcamper.api.contract.PictureDTO;
import swtcamper.api.contract.interfaces.IBookingController;
import swtcamper.api.contract.interfaces.IOfferController;
import swtcamper.api.contract.interfaces.IPictureController;
import swtcamper.api.contract.interfaces.IUserController;
import swtcamper.api.controller.ValidationHelper;
import swtcamper.backend.entities.Booking;
import swtcamper.backend.entities.User;
import swtcamper.backend.entities.UserRole;
import swtcamper.backend.entities.Vehicle;
import swtcamper.backend.services.BookingService;
import swtcamper.backend.services.OfferService;
import swtcamper.backend.services.exceptions.GenericServiceException;
import swtcamper.backend.services.exceptions.UserDoesNotExistException;

@Component
public class OfferViewController {

  private final SimpleBooleanProperty isRentingMode = new SimpleBooleanProperty();

  @FXML
  private HBox pictureHorizontHBox;

  @FXML
  private Label vehicleTypeLabel;

  @FXML
  private Label brandLabel;

  @FXML
  private Label modelLabel;

  @FXML
  private Label constructionLabel;

  @FXML
  private Label priceLabel;

  @FXML
  private Label widthLabel;

  @FXML
  private Label lengthLabel;

  @FXML
  private Label heightLabel;

  @FXML
  private Label engineLabel;

  @FXML
  private Label transmissionLabel;

  @FXML
  private Label roofTentLabel;

  @FXML
  private Label roofRackLabel;

  @FXML
  private Label bikeRackLabel;

  @FXML
  private Label showerLabel;

  @FXML
  private Label toiletLabel;

  @FXML
  private Label kitchenUnitLabel;

  @FXML
  private Label fridgeLabel;

  @FXML
  private Label contactLabel;

  @FXML
  private Label locationLabel;

  @FXML
  private Label particularitiesLabel;

  @FXML
  private VBox rentalConditionsVBox;

  @FXML
  private Label seatsLabel;

  @FXML
  private Label bedsLabel;

  @FXML
  private Label titleLabel;

  @FXML
  private Button modifyButton;

  @FXML
  private Button bookingButton;

  @FXML
  private Button promotingButton;

  @FXML
  private Label dateLabel;

  @FXML
  private DatePicker startDatePicker;

  @FXML
  private DatePicker endDatePicker;

  @FXML
  private HBox rentHBox;

  @FXML
  private Label rentLabel;

  @FXML
  private Button abortBookingRequestBtn;

  private OfferDTO viewedOffer;
  private final LongStringConverter longStringConverter = new LongStringConverter();
  private final DoubleStringConverter doubleStringConverter = new DoubleStringConverter();

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private IBookingController bookingController;

  @Autowired
  private IOfferController offerController;

  @Autowired
  private IPictureController pictureController;

  @Autowired
  private ValidationHelper validationHelper;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private ModifyOfferViewController modifyOfferViewController;

  @Autowired
  private IUserController userController;

  @Autowired
  private BookingService bookingService;

  @Autowired
  private OfferService offerService;

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
      if (
        pictureDTO.getPath().startsWith("file:///") &&
        Files.notExists(Path.of(pictureDTO.getPath().substring(8)))
      ) {
        continue;
      }

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
      !offer.getRentalConditions().isEmpty()
    ) {
      for (String rentalCondition : offer.getRentalConditions()) {
        rentalConditionsVBox
          .getChildren()
          .add(new Label("- " + rentalCondition));
      }
    } else {
      rentalConditionsVBox.getChildren().add(new Label(" / "));
    }

    vehicleTypeLabel.setText(String.valueOf(offeredObject.getVehicleType()));
    brandLabel.setText(offeredObject.getMake());
    modelLabel.setText(offeredObject.getModel());
    transmissionLabel.setText(offeredObject.getTransmission());
    seatsLabel.setText(Integer.toString(offeredObject.getSeats()));
    bedsLabel.setText(Integer.toString(offeredObject.getBeds()));
    constructionLabel.setText(offeredObject.getYear());
    engineLabel.setText(String.valueOf(offeredObject.getFuelType()));
    widthLabel.setText(
      doubleStringConverter.toString(offeredObject.getWidth())
    );
    lengthLabel.setText(
      doubleStringConverter.toString(offeredObject.getLength())
    );
    heightLabel.setText(
      doubleStringConverter.toString(offeredObject.getHeight())
    );

    roofTentLabel.setOpacity(labelOpacity(offeredObject.isRoofTent()));
    roofRackLabel.setOpacity(labelOpacity(offeredObject.isRoofRack()));
    bikeRackLabel.setOpacity(labelOpacity(offeredObject.isBikeRack()));
    showerLabel.setOpacity(labelOpacity(offeredObject.isShower()));
    toiletLabel.setOpacity(labelOpacity(offeredObject.isToilet()));
    kitchenUnitLabel.setOpacity(labelOpacity(offeredObject.isKitchenUnit()));
    fridgeLabel.setOpacity(labelOpacity(offeredObject.isFridge()));
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
        // blocked user cannot book an offer
        bookingButton.setDisable(userController.getLoggedInUser().isLocked());

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
    promotingButton.setVisible(
      userController.getLoggedInUser() != null &&
      userController.getLoggedInUser().getUserRole().equals(UserRole.OPERATOR)
    );
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

  /**
   * calculates the number of days for a booking, multiplies it with the price and
   * returns a String with the result
   */
  public String calculateTotalPrice() {
    long daysBetween = ChronoUnit.DAYS.between(
      startDatePicker.getValue(),
      endDatePicker.getValue()
    );
    String totalPrice =
      "Das Angebot kostet " +
      daysBetween *
      viewedOffer.getPrice() +
      "€ für " +
      daysBetween +
      " Tage.";
    return totalPrice;
  }

  @FXML
  public void promotingAction() throws GenericServiceException {
    if (this.viewedOffer.isPromoted()) {
      offerController.degradeOffer(this.viewedOffer.getID());
    } else {
      offerController.promoteOffer(this.viewedOffer.getID());
    }

    // go back to overview
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
          " buchen? " +
          calculateTotalPrice()
        );
        Optional<ButtonType> result = confirmBooking.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
          OfferDTO offerDTO = offerController.getOfferById(viewedOffer.getID());
          User user = userController.getLoggedInUser();
          try {
            BookingDTO bookingDTO = bookingController.create(
              user,
              modelMapper.offerDTOToOffer(offerDTO),
              startDatePicker.getValue(),
              endDatePicker.getValue()
            );
          } catch (GenericServiceException | UserDoesNotExistException e) {
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
   *
   * @param datePicker
   * @param offerDTO
   */
  private void setCellFactory(DatePicker datePicker, OfferDTO offerDTO) {
    try {
      final List<LocalDate> bookedDays = bookingService.getBookedAndBlockedDays(
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
