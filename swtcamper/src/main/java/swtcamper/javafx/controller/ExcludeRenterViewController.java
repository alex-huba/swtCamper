package swtcamper.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.controller.UserController;
import swtcamper.backend.entities.User;

@Component
public class ExcludeRenterViewController {

    @Autowired
    private UserController userController;

    @FXML
    public VBox excludedRentersVBox;

    @FXML
    public TextField findUsersTextField;

    @FXML
    public VBox userResultsVBox;

    @FXML
    public void initialize() {

    }

    private void reloadExcludedRenters() {
        excludedRentersVBox.getChildren().clear();
        for(User excludedUser : userController.getLoggedInUser().getExcludedRenters()) {

        }
    }

    public void findUsers(ActionEvent actionEvent) {
        for(User user : userController.)
    }
}
