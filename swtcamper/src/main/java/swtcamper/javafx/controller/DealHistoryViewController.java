package swtcamper.javafx.controller;

import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.ModelMapper;
import swtcamper.api.contract.interfaces.IBookingController;
import swtcamper.api.contract.interfaces.IUserController;
import swtcamper.api.controller.BookingController;
import swtcamper.api.controller.UserController;
import swtcamper.backend.entities.Booking;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class DealHistoryViewController {

  @FXML
  public VBox bookingsListVBox;

  @Autowired
  private OfferViewController offerViewController;

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private IBookingController bookingController;

  @Autowired
  private IUserController userController;

  @Autowired
  private ModelMapper modelMapper;

  @FXML
  private void initialize() {
    reloadData();
  }

  public void reloadData() {
    if (userController.getLoggedInUser() == null) {
      return;
    }
    List<Booking> bookingList = bookingController
      .getAllBookings()
      .stream()
      .filter(booking -> booking.isActive() || booking.isRejected())
      .filter(booking ->
        booking
          .getRenter()
          .getId()
          .equals(userController.getLoggedInUser().getId()) ||
        booking
          .getOffer()
          .getCreator()
          .getId()
          .equals(userController.getLoggedInUser().getId())
      )
      .collect(Collectors.toList());

    bookingsListVBox.getChildren().clear();

    if (bookingList.isEmpty()) {
      Label emptyListLabel = new Label(
        "Du hast noch keine Deals abgeschlossen."
      );
      emptyListLabel.setDisable(true);
      bookingsListVBox.getChildren().add(emptyListLabel);
      return;
    }

    for (Booking booking : bookingList) {
      int totalDays =
        Period.between(booking.getStartDate(), booking.getEndDate()).getDays() +
        1;
      int totalPrice = (int) (booking.getOffer().getPrice() * totalDays);
      Label timeLineLabel = new Label(
        String.format(
          "Zeitraum: %s - %s",
          booking
            .getStartDate()
            .format(DateTimeFormatter.ofPattern("dd.MM.YYYY")),
          booking.getEndDate().format(DateTimeFormatter.ofPattern("dd.MM.YYYY"))
        )
      );
      Label renterLabel = new Label(
        String.format("Mieter: %s", booking.getRenter().getUsername())
      );
      Label providerLabel = new Label(
        String.format(
          "Vermieter: %s",
          booking.getOffer().getCreator().getUsername()
        )
      );
      Label totalLabel = new Label(String.format("Gesamt: %s€ ", totalPrice));
      Button detailsButton = new Button("Details");
      detailsButton.getStyleClass().add("bg-primary");

      detailsButton.setOnAction(event -> {
        try {
          mainViewController.changeView("viewOffer");
          offerViewController.initialize(
            modelMapper.offerToOfferDTO(booking.getOffer()),
            true
          );
        } catch (GenericServiceException e) {
          e.printStackTrace();
        }
      });
      Label descriptionLabel = new Label();
      descriptionLabel.getStyleClass().add("descriptionHeading");
      VBox bookingVBox = new VBox(
        descriptionLabel,
        new Separator(Orientation.HORIZONTAL),
        timeLineLabel,
        providerLabel,
        renterLabel,
        totalLabel,
        detailsButton
      );

      // check if booking is rented or provided
      if (
        booking
          .getRenter()
          .getId()
          .equals(userController.getLoggedInUser().getId())
      ) {
        bookingVBox.setStyle("-fx-background-radius: 20; -fx-padding: 10;");
        bookingVBox.getStyleClass().add("bg-purple");
        descriptionLabel.setText(
          "Ich miete" + (booking.isRejected() ? "te" : "")
        );
      } else {
        bookingVBox.setStyle("-fx-background-radius: 20; -fx-padding: 10;");
        bookingVBox.getStyleClass().add("bg-lightGreen");
        descriptionLabel.setText(
          "Ich vermiete" + (booking.isRejected() ? "te" : "")
        );
      }

      bookingVBox.setSpacing(5);
      bookingsListVBox.getChildren().add(bookingVBox);
    }
  }

  public void showDetails() {}
}
