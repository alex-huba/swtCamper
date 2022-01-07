package swtcamper.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.controller.UserController;
import swtcamper.backend.entities.User;
import swtcamper.backend.entities.UserRole;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class ApproveNewProvidersViewController {

    @Autowired
    private UserController userController;

    @FXML
    public VBox toApproveListView;

    @FXML
    public void initialize() throws GenericServiceException {
        reloadData();
    }

    private void reloadData() throws GenericServiceException {
        toApproveListView.getChildren().clear();
        for(User user : userController.getAllUsers()) {
            if(user.getUserRole().equals(UserRole.PROVIDER) && !user.isEnabled()) {
                Label descriptionLabel = new Label(String.format("Der neue Nutzer %s %s (%s) will Provider sein.", user.getName(), user.getSurname(),user.getUsername()));
                Button acceptButton = new Button("Akzeptieren");
                acceptButton.getStyleClass().add("bg-primary");
                acceptButton.setOnAction(event -> {
                    userController.enableUserById(user.getId());
                    try {
                        reloadData();
                    } catch (GenericServiceException ignored) {
                    }
                });
                Tooltip t1 = new Tooltip(String.format("Dadurch wird %s Anzeigen erstellen können", user.getUsername()));
                Tooltip.install(acceptButton, t1);

                Button rejectButton = new Button("Ablehnen");
                rejectButton.getStyleClass().add("bg-warning");
                rejectButton.setOnAction(event -> {
                    try {
                        userController.degradeUserById(user.getId());
                    } catch (GenericServiceException ignored) {
                    }
                    userController.enableUserById(user.getId());
                    try {
                        reloadData();
                    } catch (GenericServiceException ignored) {
                    }
                });
                Tooltip t2 = new Tooltip(String.format("Dadurch bekommt %s die Rolle 'Renter'", user.getUsername()));
                Tooltip.install(rejectButton, t2);


                HBox providerToApproveHBox = new HBox(descriptionLabel,
                        acceptButton,
                        rejectButton);

                toApproveListView.getChildren().add(providerToApproveHBox);
            }
        }
    }
}
