package swtcamper.javafx.controller;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.controller.BookingController;
import swtcamper.api.controller.UserController;
import swtcamper.backend.entities.Booking;
import swtcamper.backend.entities.VehicleType;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class MyBookingsViewController {

  @Autowired
  private BookingController bookingController;

  @Autowired
  private UserController userController;

  @FXML
  public VBox bookingsListVBox;

  @FXML
  public void initialize() {
  }

  public void reloadData() {
    bookingsListVBox.getChildren().clear();
    List<Booking> bookingList = bookingController.getBookingsForUser(
      userController.getLoggedInUser()
    );

    if (bookingList.size() > 0) {
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
          String.format(
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
        );

        Button acceptButton = new Button("Annehmen");
        acceptButton.setOnAction(event -> {
          try {
            // TODO: was soll passieren?
            bookingController.update(
              booking.getId(),
              booking.getStartDate(),
              booking.getEndDate(),
              true
            );
          } catch (GenericServiceException ignore) {}
        });

        Button rejectButton = new Button("Ablehnen");
        rejectButton.setOnAction(event -> {
          try {
            bookingController.delete(booking.getId());
            reloadData();
          } catch (GenericServiceException ignore) {}
        });

        Button reportRenterButton = new Button("Nutzer melden");
        // TODO
        //reportRenterButton.setOnAction(event -> );

        HBox buttonHBox = new HBox(
          acceptButton,
          rejectButton,
          reportRenterButton
        );

        VBox bookingVBox = new VBox(bookingLabel, buttonHBox);
        bookingsListVBox.getChildren().add(bookingVBox);
      }
    } else {
      Label bookingLabel = new Label(
        "Im Moment gibt es keine Buchungsanfragen zu deinen Anzeigen."
      );
      bookingsListVBox.getChildren().add(bookingLabel);
    }
  }
}
