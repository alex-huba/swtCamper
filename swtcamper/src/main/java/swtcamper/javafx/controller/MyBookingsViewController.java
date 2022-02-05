package swtcamper.javafx.controller;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.interfaces.IBookingController;
import swtcamper.api.contract.interfaces.IUserController;
import swtcamper.api.controller.BookingController;
import swtcamper.api.controller.UserController;
import swtcamper.backend.entities.Booking;
import swtcamper.backend.entities.UserRole;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class MyBookingsViewController {

  @FXML
  private VBox bookingsListVBox;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private IBookingController bookingController;

  @Autowired
  private IUserController userController;

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private OfferViewController offerViewController;

  public void reloadData() {
    bookingsListVBox.getChildren().clear();
    if (
      !userController.getLoggedInUser().getUserRole().equals(UserRole.RENTER)
    ) {
      addRequestsForProvider();
      Separator separator = new Separator();
      separator.setOrientation(Orientation.HORIZONTAL);
      bookingsListVBox.getChildren().add(separator);
    }
    addRequestsFromRenter();
  }

  /**
   * get all booking requests for current user (as provider)
   */
  private void addRequestsForProvider() {
    Label renterLabel = new Label("Buchungsanfragen zu meinen Anzeigen:");
    renterLabel.setStyle("-fx-font-weight: bold");
    bookingsListVBox.getChildren().add(renterLabel);

    List<Booking> bookingList = bookingController
      .getBookingsForUser(userController.getLoggedInUser())
      .stream()
      .filter(booking -> !booking.isRejected())
      .collect(Collectors.toList());

    if (!bookingList.isEmpty()) {
      // create a card for each booking request
      for (Booking booking : bookingList) {
        String vehicleType = booking
          .getOffer()
          .getOfferedObject()
          .getVehicleType()
          .toString();
        String vehicle =
          vehicleType.charAt(0) + vehicleType.substring(1).toLowerCase();

        Label bookingLabel = new Label(
          !booking.isActive()
            ? String.format(
              "%s will deinen %s %s %s von %s bis %s mieten.",
              booking.getRenter().getUsername(),
              vehicle,
              booking.getOffer().getOfferedObject().getMake(),
              booking.getOffer().getOfferedObject().getModel(),
              booking
                .getStartDate()
                .format(DateTimeFormatter.ofPattern("dd.MM.YYYY")),
              booking
                .getEndDate()
                .format(DateTimeFormatter.ofPattern("dd.MM.YYYY"))
            )
            : String.format(
              "Dein %s %s %s ist von %s bis %s vermietet an %s.",
              vehicle,
              booking.getOffer().getOfferedObject().getMake(),
              booking.getOffer().getOfferedObject().getModel(),
              booking
                .getStartDate()
                .format(DateTimeFormatter.ofPattern("dd.MM.YYYY")),
              booking
                .getEndDate()
                .format(DateTimeFormatter.ofPattern("dd.MM.YYYY")),
              booking.getRenter().getUsername()
            )
        );

        // link to visit related offer
        Hyperlink linkToOffer = new Hyperlink("(zur Anzeige)");
        linkToOffer.setAlignment(Pos.TOP_CENTER);
        linkToOffer.setOnAction(event -> {
          try {
            mainViewController.changeView("viewOffer");
            offerViewController.initialize(
              modelMapper.offerToOfferDTO(booking.getOffer()),
              false
            );
          } catch (GenericServiceException ignore) {}
        });

        HBox bookingInfoHBox = new HBox(bookingLabel, linkToOffer);
        bookingInfoHBox.setSpacing(10);

        // Button for accepting the booking request
        Button acceptButton = new Button("Annehmen");
        acceptButton.getStyleClass().add("bg-primary");
        acceptButton.setDisable(
          booking.isActive() &&
          anotherBookingWithSameOfferIsActiveAndRentedAtTheSameTime(booking)
        );
        acceptButton.setOnAction(event -> {
          try {
            bookingController.activate(booking.getId());
            reloadData();
          } catch (GenericServiceException ignore) {}
        });

        // Button for rejecting the booking request
        Button rejectButton = new Button("Ablehnen");
        rejectButton.getStyleClass().add("bg-warning");
        rejectButton.setDisable(booking.isActive());
        rejectButton.setOnAction(event -> {
          try {
            //            bookingController.reject(booking.getId());
            bookingController.delete(booking.getId());
            reloadData();
          } catch (GenericServiceException ignore) {}
        });

        // Button for aborting the booking
        Button abortButton = new Button("Buchung beenden");
        abortButton.getStyleClass().add("bg-danger");
        abortButton.setDisable(!booking.isActive());
        abortButton.setOnAction(event -> {
          Alert confirmDelete = new Alert(
            Alert.AlertType.WARNING,
            "Willst du diese Buchung wirklich frühzeitig beenden?"
          );
          Optional<ButtonType> result = confirmDelete.showAndWait();

          if (result.isPresent() && result.get() == ButtonType.OK) {
            bookingController.reject(booking.getId());
            //            try {
            //                          bookingController.delete(booking.getId());
            //            } catch (GenericServiceException ignore) {}
          }
          reloadData();
        });

        // button box
        HBox buttonHBox = new HBox(acceptButton, rejectButton, abortButton);
        buttonHBox.setSpacing(5);

        // card
        VBox bookingVBox = new VBox(bookingInfoHBox, buttonHBox);
        bookingVBox.setFillWidth(true);
        bookingVBox.setSpacing(5);
        bookingVBox.setStyle(
          "-fx-background-color: #c9dfce; -fx-background-radius: 20; -fx-padding: 10;"
        );

        bookingsListVBox.getChildren().add(bookingVBox);
      }
    } else {
      Label bookingLabel = new Label(
        "\tIm Moment gibt es keine Buchungsanfragen zu einer deiner Anzeigen."
      );
      bookingLabel.setDisable(true);
      bookingsListVBox.getChildren().add(bookingLabel);
    }
  }

  /**
   * get all booking requests from current user (as renter)
   */
  private void addRequestsFromRenter() {
    Label renterLabel = new Label("Meine aktiven Buchungen:");
    bookingsListVBox.getChildren().add(renterLabel);

    List<Booking> rentingList = new ArrayList<>();
    for (Booking booking : bookingController.getAllBookings()) {
      if (
        booking
          .getRenter()
          .getId()
          .equals(userController.getLoggedInUser().getId()) &&
        !booking.isRejected()
      ) {
        rentingList.add(booking);
      }
    }

    if (!rentingList.isEmpty()) {
      // create a card for each booking request
      for (Booking booking : rentingList) {
        String vehicleType = booking
          .getOffer()
          .getOfferedObject()
          .getVehicleType()
          .toString();
        String vehicle =
          vehicleType.charAt(0) + vehicleType.substring(1).toLowerCase();

        Label bookingLabel = new Label(
          booking.isActive()
            ? String.format(
              "Du hast den %s %s %s von Nutzer %s von %s bis %s gemietet.",
              vehicle,
              booking.getOffer().getOfferedObject().getMake(),
              booking.getOffer().getOfferedObject().getModel(),
              booking.getOffer().getCreator().getUsername(),
              booking
                .getStartDate()
                .format(DateTimeFormatter.ofPattern("dd.MM.YYYY")),
              booking
                .getEndDate()
                .format(DateTimeFormatter.ofPattern("dd.MM.YYYY"))
            )
            : String.format(
              "Du hast angefragt, den %s %s %s von Nutzer %s von %s bis %s zu mieten.",
              vehicle,
              booking.getOffer().getOfferedObject().getMake(),
              booking.getOffer().getOfferedObject().getModel(),
              booking.getOffer().getCreator().getUsername(),
              booking
                .getStartDate()
                .format(DateTimeFormatter.ofPattern("dd.MM.YYYY")),
              booking
                .getEndDate()
                .format(DateTimeFormatter.ofPattern("dd.MM.YYYY"))
            )
        );
        bookingLabel.setStyle("-fx-text-fill: #FFFFFF");

        HBox bookingInfoHBox = new HBox(bookingLabel);
        bookingInfoHBox.setSpacing(10);

        // Button for aborting the booking
        Button abortButton = new Button(
          booking.isActive() ? "Buchung beenden" : "Anfrage löschen"
        );
        abortButton
          .getStyleClass()
          .add(booking.isActive() ? "bg-danger" : "bg-warning");
        abortButton.setOnAction(event -> {
          Alert confirmDelete = new Alert(
            Alert.AlertType.WARNING,
            booking.isActive()
              ? "Willst du diese Buchung wirklich frühzeitig beenden?\nDaduch haben andere Nutzer wieder die Möglichkeit, eine Buchungsanfrage zu dieser Anzeige zu starten."
              : "Willst du diese Buchung wirklich entgültig entfernen?"
          );
          Optional<ButtonType> result = confirmDelete.showAndWait();

          if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
              if (booking.isActive()) {
                // finish booking after it has already been accepted
                bookingController.reject(booking.getId());
                //                bookingController.deactivate(booking.getId());
              } else {
                //                bookingController.reject(booking.getId());
                // completely delete booking because it has never been accepted
                bookingController.delete(booking.getId());
              }
            } catch (GenericServiceException ignore) {}
          }
          reloadData();
        });

        // button box
        HBox buttonHBox = new HBox(abortButton);
        buttonHBox.setSpacing(5);

        // card
        VBox bookingVBox = new VBox(bookingInfoHBox, buttonHBox);
        bookingVBox.setFillWidth(true);
        bookingVBox.setSpacing(5);
        bookingVBox.setStyle(
          "-fx-background-color: #665fa0; -fx-background-radius: 20; -fx-padding: 10;"
        );

        bookingsListVBox.getChildren().add(bookingVBox);
      }
    } else {
      Label bookingLabel = new Label(
        "\tDu hast im Moment keine Anfrage zu einer Anzeige getätigt."
      );
      bookingLabel.setDisable(true);
      bookingsListVBox.getChildren().add(bookingLabel);
    }
  }

  /**
   * Checks if the same offer is also in another booking that is active already and has overlapping renting dates
   *
   * @param booking the {@link Booking} that shall be evaluated
   * @return true if the offer is already in another active booking, false if it is available
   */
  private boolean anotherBookingWithSameOfferIsActiveAndRentedAtTheSameTime(
    Booking booking
  ) {
    for (Booking bookingI : bookingController.getBookingsForUser(
      userController.getLoggedInUser()
    )) {
      if (
        bookingI.getOffer().getOfferID() == booking.getOffer().getOfferID() &&
        bookingI.isActive() &&
        (
          (booking.getStartDate().compareTo(bookingI.getEndDate()) <= 0) &&
          (bookingI.getStartDate().compareTo(booking.getEndDate()) <= 0)
        )
      ) {
        return true;
      }
    }
    return false;
  }
}
