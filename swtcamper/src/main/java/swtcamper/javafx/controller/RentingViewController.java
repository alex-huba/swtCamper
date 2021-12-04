package swtcamper.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.contract.OfferDTO;

@Component
public class RentingViewController {

  private OfferDTO selectedOffer;

  @Autowired
  private MainViewController mainViewController;

  @FXML
  public ListView<OfferDTO> offersList;

  public void openFilterView(ActionEvent actionEvent) {
    private FXMLLoader fxmlLoader;
    fxmlLoader = new FXMLLoader();
    fxmlLoader.setControllerFactory(springContext::getBean);
    fxmlLoader.setLocation(getClass().getResource("/fxml/filterOptionsView.fxml"));
    Parent rootNode = fxmlLoader.load();

    primaryStage.setTitle("SWTCamper");
    primaryStage.setScene(new Scene(rootNode, 800, 600));
    primaryStage.show();
  }

  public void startSearch(ActionEvent actionEvent) {
  }
}


