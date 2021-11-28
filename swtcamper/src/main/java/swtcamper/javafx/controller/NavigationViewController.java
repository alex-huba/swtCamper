package swtcamper.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NavigationViewController {

    @Autowired
    private MainViewController mainViewController;

    @FXML
    public Button myOfferButton;

    @FXML
    public Button rentCamperButton;

    @FXML
    public Button placeOfferButton;

    @FXML
    public Button loginButton;

    private String activeButtonString = "-fx-background-color:#333; -fx-text-fill:#FFF";

    private void resetStyle() {
        myOfferButton.setStyle("");
        rentCamperButton.setStyle("");
        placeOfferButton.setStyle("");
        loginButton.setStyle("");
    }

    public void showMyOffers() {
        resetStyle();
        myOfferButton.setStyle(activeButtonString);
        mainViewController.changeView("myOffers");
    }

    public void showRentCamper() {
        resetStyle();
        rentCamperButton.setStyle(activeButtonString);
        mainViewController.changeView("rentVan");
    }

    public void showPlaceOffer() {
        resetStyle();
        placeOfferButton.setStyle(activeButtonString);
        mainViewController.changeView("placeOffer");
    }

    public void showLogin() {
        resetStyle();
        loginButton.setStyle(activeButtonString);
        mainViewController.changeView("login");
    }
}
