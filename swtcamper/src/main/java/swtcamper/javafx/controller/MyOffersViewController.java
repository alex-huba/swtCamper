package swtcamper.javafx.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.OfferDTO;
import swtcamper.api.controller.OfferController;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class MyOffersViewController {
  private OfferDTO selectedOffer;

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private OfferController offerController;

  @Autowired
  private ModifyOfferViewController modifyOfferViewController;

  @Autowired
  private OfferViewController offerViewController;

  @FXML
  public ScrollPane offersList;

  @FXML
  public VBox offerListRoot;

  @FXML
  public void initialize() throws GenericServiceException {
    reloadData();
  }

  private void setData(List<OfferDTO> offers) throws GenericServiceException {
    offerListRoot.getChildren().clear();

    Label header = new Label("Deine Anzeigen");
    header.setStyle("-fx-font-size: 30");
    offerListRoot.getChildren().add(header);

    for (OfferDTO offer : offers) {
      VBox root = new VBox();
      root.setStyle(
              "-fx-background-color: #c9dfce; -fx-background-radius: 20px"
      );
      root.setEffect(new DropShadow(4d, 0d, +6d, Color.BLACK));

      Image image = offer.getOfferedObject().getPictureURLs().length > 0 ? new Image(offer.getOfferedObject().getPictureURLs()[0]) : new Image("/pictures/noImg.png");

      HBox offerDetails = new HBox();

      ImageView thumbnail = new ImageView(image);
      thumbnail.setFitHeight(80);
      thumbnail.setFitWidth(90);
      offerDetails.getChildren().add(thumbnail);

      Label titleLabel = new Label(offer.getTitle() + " " + (offer.isActive() ? "(aktiv)" : "(deaktiviert)"));
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
      moreBtn.setStyle(
              "-fx-background-radius: 8; -fx-background-color: #e79e69"
      );
      moreBtn.setOnAction(
              new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                  try {
                    mainViewController.changeView("viewOffer");
                    offerViewController.initialize(offer, false);
                  } catch (GenericServiceException e) {
                    e.printStackTrace();
                  }
                }
              }
      );

      HBox btnBox = new HBox();
      btnBox.setAlignment(Pos.TOP_RIGHT);
      btnBox.setStyle("-fx-padding: 0 30 30 0");
      btnBox.getChildren().add(moreBtn);
      detailsVBox.getChildren().add(btnBox);

      offerDetails.setHgrow(detailsVBox, Priority.ALWAYS);
      offerDetails.getChildren().add(detailsVBox);

      root.getChildren().add(offerDetails);
      offerListRoot.getChildren().add(root);
    }
  }

  @FXML
  public void placeOfferAction() throws GenericServiceException {
    mainViewController.changeView("placeOffer");
  }

  public void reloadData() throws GenericServiceException {
    setData(offerController.offers());
  }

  private void showInfoAlert(OfferDTO offerItem) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION, offerItem.toString());
    alert.showAndWait();
  }
}
