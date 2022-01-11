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

import java.util.stream.Collectors;

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

  public void reloadData() throws GenericServiceException {
    toApproveListView.getChildren().clear();

    if (userController.countUser() > 0) {
      if (userController.getAllUsers().stream().anyMatch(user -> !user.isEnabled())) {
        for (User user : userController.getAllUsers()) {
          if (user.getUserRole().equals(UserRole.PROVIDER) && !user.isEnabled()) {
            Label headingLabel = new Label(
                    String.format(
                            "Neue Provider-Anfrage von Nutzer '%s'",
                            user.getUsername()
                    )
            );
            headingLabel.setStyle("-fx-font-size: 20;");

            Label usernameLabel = new Label(
                    String.format("Benutzername: %s", user.getUsername())
            );
            Label nameLabel = new Label(
                    String.format("Name: %s %s", user.getName(), user.getSurname())
            );
            Label emailLabel = new Label(
                    String.format("E-mail: %s", user.getEmail())
            );
            Label phoneLabel = new Label(
                    String.format("Telefon: %s", user.getPhone())
            );

            // accept button
            Button acceptButton = new Button("Akzeptieren");
            acceptButton.getStyleClass().add("bg-primary");
            acceptButton.setOnAction(event -> {
              userController.enableUserById(user.getId());
              try {
                reloadData();
              } catch (GenericServiceException ignored) {
              }
            });
            Tooltip t1 = new Tooltip(
                    String.format(
                            "Dadurch wird %s Anzeigen erstellen können",
                            user.getUsername()
                    )
            );
            Tooltip.install(acceptButton, t1);

            // reject button
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
            Tooltip t2 = new Tooltip(
                    String.format(
                            "Dadurch bekommt %s die Rolle 'Renter'",
                            user.getUsername()
                    )
            );
            Tooltip.install(rejectButton, t2);

            HBox buttonHBox = new HBox(acceptButton, rejectButton);
            buttonHBox.setSpacing(10);

            // card
            VBox bookingVBox = new VBox(
                    headingLabel,
                    usernameLabel,
                    nameLabel,
                    emailLabel,
                    phoneLabel,
                    buttonHBox
            );
            bookingVBox.setFillWidth(true);
            bookingVBox.setSpacing(5);
            bookingVBox.setStyle(
                    "-fx-background-color: #c9dfce; -fx-background-radius: 20; -fx-padding: 10;"
            );

            // add card to view
            toApproveListView.getChildren().add(bookingVBox);
          }
        }
      } else {
        Label noNewProvidersLabel = new Label(
                "\tIm Moment gibt es keine neuen Provider die akzeptiert werden müssen."
        );
        noNewProvidersLabel.setDisable(true);
        toApproveListView.getChildren().add(noNewProvidersLabel);
      }
    }
  }
}
