package swtcamper.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.controller.UserController;
import swtcamper.backend.entities.User;
import swtcamper.backend.services.exceptions.GenericServiceException;

import java.util.stream.Collectors;

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
    public void initialize() throws GenericServiceException {
            reloadExcludedRenters();
    }

    private void reloadExcludedRenters() throws GenericServiceException {
        if(userController.getLoggedInUser()==null) return;
        excludedRentersVBox.getChildren().clear();
        if(userController.getLoggedInUser().getExcludedRenters().isEmpty()) {
            Label infoLabel = new Label("Du hast keine Nutzer von deinen Anzeigen ausgeschlossen");
            infoLabel.setDisable(true);
            excludedRentersVBox.getChildren().add(infoLabel);
            return;
        }

        for(Long excludedUserId : userController.getLoggedInUser().getExcludedRenters()) {
            User user = userController.getUserById(excludedUserId);

            Label usernameLabel = new Label(
                    String.format("Benutzername: %s", user.getUsername())
            );
            Label nameLabel = new Label(
                    String.format("Name: %s. %s", user.getName().toCharArray()[0], user.getSurname())
            );
            Label emailLabel = new Label(
                    String.format("E-mail: %s", user.getEmail())
            );

            // block button
            Button includeButton = new Button("Ausschließen beenden");
            includeButton.getStyleClass().add("bg-primary");
            includeButton.setOnAction(event -> {
                userController.removeExcludedRenterForCurrentlyLoggedInUser(user.getId());
                try {
                    reloadExcludedRenters();
                } catch (GenericServiceException ignore) {
                }
            });

            // card
            VBox bookingVBox = new VBox(
                    usernameLabel,
                    nameLabel,
                    emailLabel,
                    includeButton
            );
            bookingVBox.setFillWidth(true);
            bookingVBox.setSpacing(5);
            bookingVBox.setStyle(
                    "-fx-background-color: #c9dfce; -fx-background-radius: 20; -fx-padding: 10;"
            );

            // add card to view
            excludedRentersVBox.getChildren().add(bookingVBox);
        }
    }

    @FXML
    private void findUsers() throws GenericServiceException {
        findUsers(findUsersTextField.getText());
    }

    private void findUsers(String searchText) throws GenericServiceException {
        userResultsVBox.getChildren().clear();
        if(searchText.isEmpty())return;
        for(User user : userController.getAllUsers().parallelStream().filter(user -> user.getUsername().equals(searchText)).collect(Collectors.toList())) {
            if (userController.getLoggedInUser().getExcludedRenters().contains(user.getId())) {
                Label infoLabel = new Label("Dieser Nutzer wurde bereits von deinen Anzeigen ausgeschlossen.");
                infoLabel.setDisable(true);
                userResultsVBox.getChildren().add(infoLabel);
                return;
            }

            Label usernameLabel = new Label(
                    String.format("Benutzername: %s", user.getUsername())
            );
            Label nameLabel = new Label(
                    String.format("Name: %s. %s", user.getName().toCharArray()[0], user.getSurname())
            );
            Label emailLabel = new Label(
                    String.format("E-mail: %s", user.getEmail())
            );

            // block button
            Button excludeButton = new Button("Ausschließen");
            excludeButton.getStyleClass().add("bg-warning");
            excludeButton.setOnAction(event -> {
                userController.excludeRenterForCurrentlyLoggedInUser(user.getId());
                try {
                    findUsersTextField.setText("");
                    findUsers("");
                    reloadExcludedRenters();
                } catch (GenericServiceException ignore) {
                }
            });
            Tooltip t1 = new Tooltip(
                    String.format(
                            "Dadurch wird %s von deinen Anzeigen ausgeschlossen",
                            user.getUsername()
                    )
            );
            Tooltip.install(excludeButton, t1);

            // card
            VBox bookingVBox = new VBox(
                    usernameLabel,
                    nameLabel,
                    emailLabel,
                    excludeButton
            );
            bookingVBox.setFillWidth(true);
            bookingVBox.setSpacing(5);
            bookingVBox.setStyle(
                    "-fx-background-color: #c9dfce; -fx-background-radius: 20; -fx-padding: 10;"
            );

            // add card to view
            userResultsVBox.getChildren().add(bookingVBox);
        }
    }
}
