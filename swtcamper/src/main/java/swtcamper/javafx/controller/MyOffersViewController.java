package swtcamper.javafx.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.OfferDTO;
import swtcamper.api.controller.BookingController;
import swtcamper.api.controller.OfferController;
import swtcamper.api.controller.UserController;
import swtcamper.backend.entities.Booking;
import swtcamper.backend.entities.User;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class MyOffersViewController {

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private BookingController bookingController;

  @Autowired
  private OfferController offerController;

  @Autowired
  private UserController userController;

  @Autowired
  private ModifyOfferViewController modifyOfferViewController;

  @Autowired
  private OfferViewController offerViewController;

  @FXML
  public ScrollPane offerListScroll;

  @FXML
  public VBox offerListRoot;

  public void reloadData() throws GenericServiceException {
    // create the "cards" that has been created by the logged-in user
    createCards(userController.getLoggedInUser());
  }

  /**
   * creates a new "card" for each offer that has been created by the given user
   */
  private void createCards(User user) throws GenericServiceException {
    offerListRoot.getChildren().clear();

    for (OfferDTO offer : offerController.getOffersCreatedByUser(user)) {
      Image image;
      //TODO: after finishing stuff with image realize logic with image
      if (false) {
        image = new Image(offer.getOfferedObject().getPictureURLs()[0]);
      } else {
        image = new Image("/pictures/noImg.png");
      }

      HBox offerDetails = new HBox();

      // thumbnail
      ImageView thumbnail = new ImageView(image);
      thumbnail.setFitHeight(80);
      thumbnail.setFitWidth(90);
      offerDetails.getChildren().add(thumbnail);

      // title
      Label titleLabel = new Label(offer.getTitle());
      titleLabel.setStyle(
        "-fx-font-size: 35; -fx-font-family: \"Arial Rounded MT Bold\"; -fx-text-fill: #040759"
      );

      // location
      Label locationLabel = new Label("Abholort: " + offer.getLocation());
      locationLabel.setStyle(
        "-fx-font-size: 20; -fx-font-family: \"Arial Rounded MT Bold\";"
      );

      // price
      Label priceLabel = new Label(
        "Preis pro Tag: € " + Long.toString(offer.getPrice())
      );
      priceLabel.setStyle(
        "-fx-font-size: 20; -fx-font-family: \"Arial Rounded MT Bold\";"
      );

      // brand
      Label brandLabel = new Label(
        "Marke: " + offer.getOfferedObject().getVehicleFeatures().getMake()
      );
      brandLabel.setStyle(
        "-fx-font-size: 20; -fx-font-family: \"Arial Rounded MT Bold\";"
      );

      // model
      Label modelLabel = new Label(
        "Modell: " + offer.getOfferedObject().getVehicleFeatures().getModel()
      );
      modelLabel.setStyle(
        "-fx-font-size: 20; -fx-font-family: \"Arial Rounded MT Bold\";"
      );

      VBox locationPriceBrandModelBox = new VBox(
        locationLabel,
        priceLabel,
        brandLabel,
        modelLabel
      );
      locationPriceBrandModelBox.setStyle("-fx-padding: 0 0 0 30");

      Button moreBtn = new Button("Mehr Information");
      moreBtn.getStyleClass().add("bg-primary");
      moreBtn.setOnAction(event -> {
        try {
          mainViewController.changeView("viewOffer");
          offerViewController.initialize(offer, false);
        } catch (GenericServiceException ignore) {}
      });

      // check if offer is rented right now, in order to prevent the provider from updating or deleting it
      // TODO: auch im {@link swtcamper.backend.services.BookingService} abchecken!
      boolean isOfferRentedRightNow = false;
      for (Booking booking : bookingController.getAllBookings()) {
        if (
          booking.getOffer().getOfferID() == offer.getID() && booking.isActive()
        ) {
          isOfferRentedRightNow = true;
          break;
        }
      }

      Button updateBtn = new Button("Anzeige bearbeiten");
      if (isOfferRentedRightNow) updateBtn.setDisable(true);
      updateBtn.getStyleClass().add("bg-secondary");
      updateBtn.setOnAction(event -> {
        try {
          mainViewController.changeView("placeOffer");
          modifyOfferViewController.initialize(offer);
        } catch (GenericServiceException ignore) {}
      });

      Button removeBtn = new Button("Löschen");
      if (isOfferRentedRightNow) removeBtn.setDisable(true);
      removeBtn.getStyleClass().add("bg-danger");
      removeBtn.setOnAction(event -> {
        Alert confirmDelete = new Alert(
          Alert.AlertType.CONFIRMATION,
          "Willst du dieses Angebot wirklich entfernen?"
        );
        Optional<ButtonType> result = confirmDelete.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
          try {
            offerController.delete(offer.getID());
          } catch (GenericServiceException ignore) {}
          try {
            reloadData();
          } catch (GenericServiceException ignore) {}
        }
      });

      // control buttons
      HBox btnBox = new HBox(moreBtn, updateBtn, removeBtn);
      btnBox.setAlignment(Pos.TOP_RIGHT);
      btnBox.setStyle("-fx-padding: 0 30 30 0");
      btnBox.setSpacing(5);

      // card details
      VBox detailsVBox = new VBox(
        titleLabel,
        locationPriceBrandModelBox,
        btnBox
      );
      detailsVBox.setAlignment(Pos.TOP_CENTER);

      offerDetails.setHgrow(detailsVBox, Priority.ALWAYS);
      offerDetails.getChildren().add(detailsVBox);

      // card
      VBox root = new VBox(offerDetails);
      root.setStyle(
        "-fx-background-color: #c9dfce; -fx-background-radius: 20px"
      );

      // add to view
      offerListRoot.getChildren().add(root);
    }
  }
}
