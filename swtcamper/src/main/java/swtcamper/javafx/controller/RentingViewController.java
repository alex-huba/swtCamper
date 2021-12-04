package swtcamper.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.OfferDTO;

import java.io.IOException;

@Component
public class RentingViewController {

    @FXML
    public TextField locationTextField;
    @FXML
    public DatePicker pickUpDatePicker;
    @FXML
    public DatePicker returnDatePicker;
    @FXML
    public ComboBox vehicleTypeComboBox;
    @FXML
    public TextField vehicleBrandTextField;
    @FXML
    public TextField constructionYearTextField;
    @FXML
    public TextField maxPricePerDayTextField;
    @FXML
    public TextField engineTextField;
    @FXML
    public ComboBox transmissionComboBox;
    @FXML
    public TextField seatAmountTextField;
    @FXML
    public TextField bedAmountTextField;
    @FXML
    public CheckBox roofTentCheckBox;
    @FXML
    public CheckBox roofRackCheckBox;
    @FXML
    public CheckBox bikeRackCheckBox;
    @FXML
    public CheckBox showerCheckBox;
    @FXML
    public CheckBox toiletCheckBox;
    @FXML
    public CheckBox kitchenCheckBox;
    @FXML
    public CheckBox fridgeCheckBox;


    private OfferDTO selectedOffer;

    @Autowired
    private MainViewController mainViewController;

    @FXML
    public ListView<OfferDTO> offersList;

    public void openFilterView(ActionEvent actionEvent) throws IOException {
        mainViewController.changeView("filterOptions");
    }

    public void startSearch(ActionEvent actionEvent) {
    }

    public void applyFilters(ActionEvent actionEvent) {
        mainViewController.changeView("home");
    }
}


