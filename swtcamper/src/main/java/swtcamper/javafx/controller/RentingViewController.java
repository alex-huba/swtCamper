package swtcamper.javafx.controller;

import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.OfferDTO;
import swtcamper.api.controller.OfferController;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class RentingViewController {

  private OfferDTO selectedOffer;

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private OfferController offerController;

  @FXML
  public ListView<OfferDTO> offersList;
}
