package swtcamper.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.controller.BookingController;

@Component
public class DealHistoryViewController {

    @Autowired
    private BookingController bookingController;

    @FXML
    public VBox bookingsListVBox;

}
