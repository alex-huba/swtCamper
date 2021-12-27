package swtcamper.javafx.controller;

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
import swtcamper.backend.services.exceptions.GenericServiceException;

import java.util.List;

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
        reloadData();
    }

    public void reloadData() {
        bookingsListVBox.getChildren().removeIf(node -> true);
        List<Booking> bookingList = bookingController.getBookingsForUser(userController.getLoggedInUser());

        if(bookingList.size() > 0) {
            for (Booking booking : bookingList) {
                Label bookingLabel = new Label(String.format("%s will dein %s %s %s von %s bis %s mieten.", booking.getRenter(), booking.getOffer().getOfferedObjectType(), booking.getOffer().getOfferedObject().getVehicleFeatures().getMake(), booking.getOffer().getOfferedObject().getVehicleFeatures().getModel(), booking.getStartDate(), booking.getEndDate()));

                Button acceptButton = new Button("Annehmen");
                acceptButton.setOnAction(event -> {
                    try {
                        bookingController.update(booking.getId(),booking.getStartDate(),booking.getEndDate(),true);
                    } catch (GenericServiceException ignore) {
                    }
                });

                Button rejectButton = new Button("Ablehnen");
                rejectButton.setOnAction(event -> {
                    try {
                        bookingController.delete(booking.getId());
                    } catch (GenericServiceException ignore) {
                    }
                });

                Button reportRenterButton = new Button("Nutzer melden");
                // TODO
                //reportRenterButton.setOnAction(event -> );

                HBox buttonHBox = new HBox(acceptButton, rejectButton, reportRenterButton);

                VBox bookingVBox = new VBox(bookingLabel, buttonHBox);
                bookingsListVBox.getChildren().add(bookingVBox);
            }
        } else {
            Label bookingLabel = new Label("Im Moment gibt es keine Buchungsanfragen zu deinen Anzeigen.");
            bookingsListVBox.getChildren().add(bookingLabel);
        }
    }
}
