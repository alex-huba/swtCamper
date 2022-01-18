package swtcamper.javafx.controller;

import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.ModelMapper;
import swtcamper.api.controller.BookingController;
import swtcamper.api.controller.UserController;
import swtcamper.backend.entities.Booking;
import swtcamper.backend.services.exceptions.GenericServiceException;

import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DealHistoryViewController {

    @Autowired
    private OfferViewController offerViewController;

    @Autowired
    private MainViewController mainViewController;

    @Autowired
    private BookingController bookingController;

    @Autowired
    private UserController userController;

    @Autowired
    private ModelMapper modelMapper;

    @FXML
    public VBox bookingsListVBox;

    @FXML
    private void initialize(){
        reloadData();
    }

    public void reloadData() {
        if(userController.getLoggedInUser() == null){
            return;
        }
        List<Booking> renterBookingList = bookingController.getAllBookings().stream().filter(Booking::isActive).
                filter(booking -> booking.getRenter().getId() == userController.getLoggedInUser().getId()
       || booking.getOffer().getCreator().getId() == userController.getLoggedInUser().getId()).collect(Collectors.toList());


        bookingsListVBox.getChildren().clear();

        for (Booking booking : renterBookingList){
            int totalDays = Period.between(booking.getStartDate(), booking.getEndDate()).getDays() +1;
            int totalPrice = (int) (booking.getOffer().getPrice() * totalDays);
            Label timeLineLabel = new Label(String.format("Zeitraum: %s - %s", booking.getStartDate().format(DateTimeFormatter.ofPattern("dd.MM.YYYY")), booking.getEndDate().format(DateTimeFormatter.ofPattern("dd.MM.YYYY"))));
            Label renterLabel = new Label(String.format("Mieter: %s", booking.getRenter().getUsername()));
            Label providerLabel = new Label(String.format("Vermieter: %s", booking.getOffer().getCreator().getUsername()));
            Label totalLabel = new Label(String.format("Gesamt: %s", totalPrice));
            Button detailsButton = new Button("Details");
            detailsButton.getStyleClass().add("bg-primary");

            detailsButton.setOnAction(event -> {
                try {
                    mainViewController.changeView("viewOffer");
                    offerViewController.initialize(modelMapper.offerToOfferDTO(booking.getOffer()), true);
                } catch (GenericServiceException e) {
                    e.printStackTrace();
                }
            });
            Label descriptionLabel = new Label();
            descriptionLabel.getStyleClass().add("descriptionHeading");
            VBox bookingVBox = new VBox(descriptionLabel, new Separator(Orientation.HORIZONTAL), timeLineLabel, providerLabel, renterLabel, totalLabel, detailsButton);

            // check if booking is rented or provided
            if(booking.getRenter().getId() == userController.getLoggedInUser().getId()){
                bookingVBox.setStyle(
                        "-fx-background-radius: 20; -fx-padding: 10;"
                );
                bookingVBox.getStyleClass().add("bg-purple");
                descriptionLabel.setText("Ich miete");
            } else {
                bookingVBox.setStyle(
                        "-fx-background-radius: 20; -fx-padding: 10;"
                );
                bookingVBox.getStyleClass().add("bg-lightGreen");
                descriptionLabel.setText("Ich vermiete");
            }


            bookingVBox.setSpacing(5);
            bookingsListVBox.getChildren().add(bookingVBox);




        }
    }

    public void showDetails(){

    }

}
