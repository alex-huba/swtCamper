package swtcamper.javafx.controller;

import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.OfferDTO;
import swtcamper.api.controller.OfferController;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class MyOffersViewController {

    @Autowired
    private MainViewController mainViewController;

    @Autowired
    private OfferController offerController;

    @Autowired
    private ModifyOfferViewController modifyOfferViewController;

    @FXML
    public VBox offersList;

    @FXML
    public void initialize() throws GenericServiceException {
        reloadData();
    }

    @FXML
    public void placeOfferAction(ActionEvent event)
            throws GenericServiceException {
        mainViewController.changeView("placeOffer");
    }

    @FXML
    public void viewOfferAction(long id) throws GenericServiceException {
        OfferDTO selectedOffer = offerController.getOffer(id);
        if (selectedOffer != null) {
            showInfoAlert(selectedOffer);
        }
    }

    @FXML
    public void updateOfferAction(long id) throws GenericServiceException {
        OfferDTO selectedOffer = offerController.getOffer(id);
        if (selectedOffer != null) {
            mainViewController.changeView("placeOffer");
            modifyOfferViewController.initialize(selectedOffer);
        }
    }

    @FXML
    public void removeOfferAction(long id) throws GenericServiceException {
        OfferDTO selectedOffer = offerController.getOffer(id);
        if (selectedOffer != null) {
            Alert confirmDelete = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Willst du dieses Angebot wirklich entfernen?"
            );
            Optional<ButtonType> result = confirmDelete.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                offerController.delete(selectedOffer.getID());
                reloadData();
            }
        }
    }

    public void reloadData() throws GenericServiceException {
        offersList.getChildren().clear();

        for (OfferDTO offer : offerController.offers()) {
            VBox root = new VBox();
            root.setStyle("-fx-background-color: #36334f; -fx-background-radius: 20");
            HBox imageSeperated = new HBox();

            Image image = new Image("pictures/campervan-wallpaper.jpg");
            ImageView thumbnail = new ImageView(image);
            thumbnail.setFitWidth(120);
            thumbnail.setFitHeight(80);
            imageSeperated.getChildren().add(thumbnail);

            VBox rightSide = new VBox();
            HBox vehicleName = new HBox();
            vehicleName.setSpacing(5);

            Label titleLabel = new Label(offer.getTitle());
            Label brandLabel = new Label(offer.getOfferedObject().getVehicleFeatures().getMake());
            Label modelLabel = new Label(offer.getOfferedObject().getVehicleFeatures().getModel());
            Label yearLabel = new Label("(" + offer.getOfferedObject().getVehicleFeatures().getYear() + ")");
            Label priceLabel = new Label(String.valueOf(offer.getPrice()) + " €");


            titleLabel.setStyle("-fx-text-fill: #FFFFFF");
            brandLabel.setStyle("-fx-text-fill: #FFFFFF");
            modelLabel.setStyle("-fx-text-fill: #FFFFFF");
            yearLabel.setStyle("-fx-text-fill: #FFFFFF");
            priceLabel.setStyle("-fx-text-fill: #FFFFFF");

            vehicleName.getChildren().add(brandLabel);
            vehicleName.getChildren().add(modelLabel);
            vehicleName.getChildren().add(yearLabel);

            rightSide.getChildren().add(titleLabel);
            rightSide.getChildren().add(vehicleName);
            rightSide.getChildren().add(priceLabel);
            imageSeperated.getChildren().add(rightSide);

            HBox buttonBar = new HBox();
            Button viewButton = new Button("View");
            Button updateButton = new Button("Update");
            Button deleteButton = new Button("Remove");

            viewButton.setOnAction(event -> {
                try {
                    viewOfferAction(offer.getID());
                } catch (GenericServiceException e) {
                    e.printStackTrace();
                }
            });
            updateButton.setOnAction(event -> {
                try {
                    updateOfferAction(offer.getID());
                } catch (GenericServiceException ignore) {
                }
            });
            deleteButton.setOnAction(event -> {
                try {
                    removeOfferAction(offer.getID());
                } catch (GenericServiceException ignore) {
                }
            });

            buttonBar.getChildren().add(viewButton);
            buttonBar.getChildren().add(updateButton);
            buttonBar.getChildren().add(deleteButton);

            root.getChildren().add(imageSeperated);
            root.getChildren().add(buttonBar);

            offersList.getChildren().add(root);
        }

    }

    private void showInfoAlert(OfferDTO offerItem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, offerItem.toString());
        alert.showAndWait();
    }
}
