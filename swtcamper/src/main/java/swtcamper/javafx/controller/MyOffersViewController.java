package swtcamper.javafx.controller;

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
import swtcamper.api.contract.interfaces.IBookingController;
import swtcamper.api.contract.interfaces.IOfferController;
import swtcamper.api.contract.interfaces.IPictureController;
import swtcamper.api.contract.interfaces.IUserController;
import swtcamper.backend.entities.Booking;
import swtcamper.backend.entities.User;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class MyOffersViewController {

  @FXML
  private ScrollPane offerListScroll;

  @FXML
  private VBox offerListRoot;

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private IBookingController bookingController;

  @Autowired
  private IOfferController offerController;

  @Autowired
  private IUserController userController;

  @Autowired
  private IPictureController pictureController;

  @Autowired
  private ModifyOfferViewController modifyOfferViewController;

  @Autowired
  private OfferViewController offerViewController;

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
      if (
        pictureController
          .getPicturesForVehicle(offer.getOfferedObject().getVehicleID())
          .size() >
        0
      ) {
        image =
          new Image(
            pictureController
              .getPicturesForVehicle(offer.getOfferedObject().getVehicleID())
              .get(0)
              .getPath()
          );
      } else {
        image = new Image("/pictures/noImg.png");
      }

      // thumbnail
      ImageView thumbnail = new ImageView(image);
      thumbnail.setFitHeight(150);
      thumbnail.setPreserveRatio(true);

      HBox thumbnailHbox = new HBox(thumbnail);
      thumbnailHbox.setAlignment(Pos.TOP_CENTER);
      thumbnailHbox.setStyle("-fx-padding: 20 20 20 20");

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
      Label priceLabel = new Label("Preis pro Tag: ??? " + offer.getPrice());
      priceLabel.setStyle(
        "-fx-font-size: 20; -fx-font-family: \"Arial Rounded MT Bold\";"
      );

      // brand
      Label brandLabel = new Label(
        "Marke: " + offer.getOfferedObject().getMake()
      );
      brandLabel.setStyle(
        "-fx-font-size: 20; -fx-font-family: \"Arial Rounded MT Bold\";"
      );

      // model
      Label modelLabel = new Label(
        "Modell: " + offer.getOfferedObject().getModel()
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
          offerViewController.initialize(offer, true);
        } catch (GenericServiceException ignore) {}
      });

      // check if offer is rented right now, in order to prevent the provider from updating or deleting it
      boolean isOfferRentedRightNow = false;
      for (Booking booking : bookingController.getBookingsForUser(
        userController.getLoggedInUser()
      )) {
        if (
          booking.getOffer().getOfferID() == offer.getID() &&
          booking.isActive() &&
          !booking.isRejected()
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

      Button removeBtn = new Button("L??schen");
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

      // card
      HBox offerDetails = new HBox(thumbnailHbox, detailsVBox);
      HBox.setHgrow(detailsVBox, Priority.ALWAYS);
      offerDetails.setStyle(
        "-fx-background-color: #c9dfce; -fx-background-radius: 20px"
      );

      // add to view
      offerListRoot.getChildren().add(offerDetails);
    }
  }
}
