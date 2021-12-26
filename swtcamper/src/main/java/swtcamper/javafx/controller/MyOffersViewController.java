package swtcamper.javafx.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.OfferDTO;
import swtcamper.api.controller.OfferController;
import swtcamper.api.controller.UserController;
import swtcamper.backend.entities.UserRole;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class MyOffersViewController {

  private OfferDTO selectedOffer;

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private OfferController offerController;

  @Autowired
  private UserController userController;

  @Autowired
  private ModifyOfferViewController modifyOfferViewController;

  @FXML
  public BorderPane myOffersRootPane;

  @FXML
  public Button viewOfferButton;

  @FXML
  public Button updateOfferButton;

  @FXML
  public Button removeButton;

  @FXML
  public ListView<OfferDTO> offersList;

  @FXML
  public VBox foreignOffersVBox;

  @FXML
  public ListView<OfferDTO> foreignOffersList;

  @FXML
  public void initialize() {
    viewOfferButton
      .disableProperty()
      .bind(
        offersList
          .getSelectionModel()
          .selectedItemProperty()
          .isNull()
          .and(
            foreignOffersList
              .getSelectionModel()
              .selectedItemProperty()
              .isNull()
          )
      );
    updateOfferButton
      .disableProperty()
      .bind(
        offersList
          .getSelectionModel()
          .selectedItemProperty()
          .isNull()
          .and(
            foreignOffersList
              .getSelectionModel()
              .selectedItemProperty()
              .isNull()
          )
      );
    removeButton
      .disableProperty()
      .bind(
        offersList
          .getSelectionModel()
          .selectedItemProperty()
          .isNull()
          .and(
            foreignOffersList
              .getSelectionModel()
              .selectedItemProperty()
              .isNull()
          )
      );

    offersList.setOnMouseClicked(click -> {
      foreignOffersList.getSelectionModel().select(null);
      selectedOffer = offersList.getSelectionModel().getSelectedItem();
      //Listener for double click
      if (click.getClickCount() == 2) {
        showInfoAlert();
      }
    });

    foreignOffersList.setOnMouseClicked(click -> {
      offersList.getSelectionModel().select(null);
      selectedOffer = foreignOffersList.getSelectionModel().getSelectedItem();
      //Listener for double click
      if (click.getClickCount() == 2) {
        showInfoAlert();
      }
    });
  }

  @FXML
  public void placeOfferAction(ActionEvent event)
    throws GenericServiceException {
    mainViewController.changeView("placeOffer");
  }

  @FXML
  public void viewOfferAction() {
    if (selectedOffer != null) {
      showInfoAlert();
    }
  }

  private void showSelectOfferFirstInfo() {
    Alert alert = new Alert(
      Alert.AlertType.WARNING,
      "Bitte wähle erst ein Angebot aus der Liste"
    );
    alert.showAndWait();
  }

  @FXML
  public void updateOfferAction() throws GenericServiceException {
    if (selectedOffer != null) {
      mainViewController.changeView("placeOffer");
      modifyOfferViewController.initialize(selectedOffer);
    }
  }

  @FXML
  public void removeOfferAction() throws GenericServiceException {
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
    offersList.setItems(
      FXCollections.observableArrayList(
        offerController.getOffersCreatedByUser(userController.getLoggedInUser())
      )
    );

    if (
      userController.getLoggedInUser().getUserRole().equals(UserRole.OPERATOR)
    ) {
      if (
        !myOffersRootPane.getChildren().contains(foreignOffersVBox)
      ) myOffersRootPane.getChildren().add(foreignOffersVBox);

      foreignOffersList.setItems(
        FXCollections.observableArrayList(
          offerController.getForeignOffers(userController.getLoggedInUser())
        )
      );
    } else {
      if (
        myOffersRootPane.getChildren().contains(foreignOffersVBox)
      ) myOffersRootPane.getChildren().remove(foreignOffersVBox);
    }
  }

  private void showInfoAlert() {
    Alert alert = new Alert(
      Alert.AlertType.INFORMATION,
      selectedOffer.toString()
    );
    alert.showAndWait();
  }
}
