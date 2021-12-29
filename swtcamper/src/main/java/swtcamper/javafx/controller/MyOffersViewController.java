package swtcamper.javafx.controller;

import java.util.Optional;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.OfferDTO;
import swtcamper.api.controller.OfferController;
import swtcamper.api.controller.UserController;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class MyOffersViewController {

  @Autowired
  private MainViewController mainViewController;

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

  @FXML
  public void initialize() {
  }

  public void reloadData() throws GenericServiceException {
    offerListRoot.getChildren().clear();

    for (OfferDTO offer : offerController.getOffersCreatedByUser(userController.getLoggedInUser())) {
      VBox root = new VBox();
      root.setStyle(
              "-fx-background-color: #c9dfce; -fx-background-radius: 20px"
      );

      Image image;
      //TODO: after finishing stuff with image realize logic with image
      if (false) {
        image = new Image(offer.getOfferedObject().getPictureURLs()[0]);
      } else {
        image = new Image("/pictures/noImg.png");
      }

      HBox offerDetails = new HBox();

      ImageView thumbnail = new ImageView(image);
      thumbnail.setFitHeight(80);
      thumbnail.setFitWidth(90);
      offerDetails.getChildren().add(thumbnail);

      Label titleLabel = new Label(offer.getTitle());
      titleLabel.setStyle(
              "-fx-font-size: 35; -fx-font-family: \"Arial Rounded MT Bold\"; -fx-text-fill: #040759"
      );

      Label locationLabel = new Label("Abholort: " + offer.getLocation());
      locationLabel.setStyle(
              "-fx-font-size: 20; -fx-font-family: \"Arial Rounded MT Bold\";"
      );

      Label priceLabel = new Label(
              "Preis pro Tag: € " + Long.toString(offer.getPrice())
      );
      priceLabel.setStyle(
              "-fx-font-size: 20; -fx-font-family: \"Arial Rounded MT Bold\";"
      );

      Label brandLabel = new Label(
              "Marke: " + offer.getOfferedObject().getVehicleFeatures().getMake()
      );
      brandLabel.setStyle(
              "-fx-font-size: 20; -fx-font-family: \"Arial Rounded MT Bold\";"
      );

      Label modelLabel = new Label(
              "Modell: " + offer.getOfferedObject().getVehicleFeatures().getModel()
      );
      modelLabel.setStyle(
              "-fx-font-size: 20; -fx-font-family: \"Arial Rounded MT Bold\";"
      );

      VBox detailsVBox = new VBox();
      detailsVBox.setAlignment(Pos.TOP_CENTER);
      detailsVBox.getChildren().add(titleLabel);

      VBox locationPriceBrandModelBox = new VBox();
      locationPriceBrandModelBox.setStyle("-fx-padding: 0 0 0 30");
      locationPriceBrandModelBox.getChildren().add(locationLabel);
      locationPriceBrandModelBox.getChildren().add(priceLabel);
      locationPriceBrandModelBox.getChildren().add(brandLabel);
      locationPriceBrandModelBox.getChildren().add(modelLabel);
      detailsVBox.getChildren().add(locationPriceBrandModelBox);

      Button moreBtn = new Button("Mehr Information");
      moreBtn.getStyleClass().add("bg-primary");
      moreBtn.setOnAction(event -> {
        try {
          mainViewController.changeView("viewOffer");
          offerViewController.initialize(offer, false);
        } catch (GenericServiceException ignore) {}
      });

      Button updateBtn = new Button("Anzeige bearbeiten");
      updateBtn.getStyleClass().add("bg-secondary");
      updateBtn.setOnAction(event -> {
        try {
          mainViewController.changeView("placeOffer");
          modifyOfferViewController.initialize(offer);
        } catch (GenericServiceException ignore) {}
      });

      Button removeBtn = new Button("Löschen");
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
          } catch (GenericServiceException ignore) {
          }
          try {
            reloadData();
          } catch (GenericServiceException ignore) {
          }
        }
      });

      HBox btnBox = new HBox(moreBtn,updateBtn,removeBtn);
      btnBox.setAlignment(Pos.TOP_RIGHT);
      btnBox.setStyle("-fx-padding: 0 30 30 0");
      btnBox.setSpacing(5);
      detailsVBox.getChildren().add(btnBox);

      offerDetails.setHgrow(detailsVBox, Priority.ALWAYS);
      offerDetails.getChildren().add(detailsVBox);

      root.getChildren().add(offerDetails);
      offerListRoot.getChildren().add(root);
    }
  }
}
