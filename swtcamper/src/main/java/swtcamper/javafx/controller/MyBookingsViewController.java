package swtcamper.javafx.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.ModelMapper;
import swtcamper.api.controller.BookingController;
import swtcamper.api.controller.UserController;
import swtcamper.backend.entities.Booking;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class MyBookingsViewController {

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private OfferViewController offerViewController;

  @Autowired
  private BookingController bookingController;

  @Autowired
  private UserController userController;

  @FXML
  public VBox bookingsListVBox;

  @FXML
  public void initialize() {}

  public void reloadData() {
    bookingsListVBox.getChildren().clear();
    // get all booking requests for current user
    List<Booking> bookingList = bookingController.getBookingsForUser(
      userController.getLoggedInUser()
    );

    if (bookingList.size() > 0) {
      // create a card for each booking request
      for (Booking booking : bookingList) {
        String vehicleType = booking
          .getOffer()
          .getOfferedObject()
          .getVehicleFeatures()
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
              booking
                .getOffer()
                .getOfferedObject()
                .getVehicleFeatures()
                .getMake(),
              booking
                .getOffer()
                .getOfferedObject()
                .getVehicleFeatures()
                .getModel(),
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
              booking
                .getOffer()
                .getOfferedObject()
                .getVehicleFeatures()
                .getMake(),
              booking
                .getOffer()
                .getOfferedObject()
                .getVehicleFeatures()
                .getModel(),
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
        acceptButton.setDisable(booking.isActive());
        acceptButton.setOnAction(event -> {
          try {
            bookingController.update(
              booking.getId(),
              booking.getStartDate(),
              booking.getEndDate(),
              true
            );
            reloadData();
          } catch (GenericServiceException ignore) {}
        });

        // Button for rejecting the booking request
        Button rejectButton = new Button("Ablehnen");
        rejectButton.getStyleClass().add("bg-warning");
        rejectButton.setDisable(booking.isActive());
        rejectButton.setOnAction(event -> {
          try {
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
            try {
              bookingController.update(
                booking.getId(),
                booking.getStartDate(),
                booking.getEndDate(),
                false
              );
              //            bookingController.delete(booking.getId());
            } catch (GenericServiceException ignore) {}
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
        "Im Moment gibt es keine Buchungsanfragen zu einer deiner Anzeigen."
      );
      bookingsListVBox.getChildren().add(bookingLabel);
    }
  }
}
