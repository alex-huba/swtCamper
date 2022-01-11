package swtcamper.javafx.controller;

import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swtcamper.api.controller.UserController;
import swtcamper.backend.entities.User;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class ExcludeRenterViewController {

  @Autowired
  private UserController userController;

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private ReportUserViewController reportUserViewController;

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
    // do not continue if no one is logged in right now
    if (userController.getLoggedInUser() == null) return;

    excludedRentersVBox.getChildren().clear();

    // give info message if no one is excluded
    if (
      userController.getLoggedInUser().getExcludedRenters() == null ||
      userController.getLoggedInUser().getExcludedRenters().isEmpty()
    ) {
      Label infoLabel = new Label(
        "Du hast keine Nutzer von deinen Anzeigen ausgeschlossen"
      );
      infoLabel.setDisable(true);
      excludedRentersVBox.getChildren().add(infoLabel);
      return;
    }

    // load excluded renters
    for (Long excludedUserId : userController
      .getLoggedInUser()
      .getExcludedRenters()) {
      User user = userController.getUserById(excludedUserId);

      Label usernameLabel = new Label(
        String.format("Benutzername: %s", user.getUsername())
      );
      Label nameLabel = new Label(
        String.format(
          "Name: %s. %s",
          user.getName().toCharArray()[0],
          user.getSurname()
        )
      );
      Label emailLabel = new Label(
        String.format("E-mail: %s", user.getEmail())
      );

      // include button
      Button includeButton = new Button("Ausschließen beenden");
      includeButton.getStyleClass().add("bg-primary");
      includeButton.setOnAction(event -> {
        try {
          userController.removeExcludedRenterForCurrentlyLoggedInUser(
            user.getId()
          );
          reloadExcludedRenters();
        } catch (GenericServiceException ignore) {}
      });

      // card
      VBox excludedUserVBox = new VBox(
        usernameLabel,
        nameLabel,
        emailLabel,
        includeButton
      );
      excludedUserVBox.setFillWidth(true);
      excludedUserVBox.setSpacing(5);
      excludedUserVBox.setStyle(
        "-fx-background-color: #c9dfce; -fx-background-radius: 20; -fx-padding: 10;"
      );

      // add card to view
      excludedRentersVBox.getChildren().add(excludedUserVBox);
    }
  }

  @FXML
  private void findUsers() throws GenericServiceException {
    findUsers(findUsersTextField.getText());
  }

  private void findUsers(String searchText) throws GenericServiceException {
    userResultsVBox.getChildren().clear();
    if (searchText.isEmpty()) return;

    // only show results if searchText fully equals the needed username (privacy reasons)
    for (User user : userController
      .getAllUsers()
      .parallelStream()
      .filter(user -> user.getUsername().equals(searchText))
      .collect(Collectors.toList())) {
      // info message in case this user is already excluded
      if (
        userController.getLoggedInUser().getExcludedRenters() != null &&
        userController
          .getLoggedInUser()
          .getExcludedRenters()
          .contains(user.getId())
      ) {
        Label infoLabel = new Label(
          "Dieser Nutzer wurde bereits von deinen Anzeigen ausgeschlossen."
        );
        infoLabel.setDisable(true);
        userResultsVBox.getChildren().add(infoLabel);
        return;
      }

      // short info about user
      Label usernameLabel = new Label(
        String.format("Benutzername: %s", user.getUsername())
      );
      Label nameLabel = new Label(
        String.format(
          "Name: %s. %s",
          user.getName().toCharArray()[0],
          user.getSurname()
        )
      );
      Label emailLabel = new Label(
        String.format("E-mail: %s", user.getEmail())
      );

      // exclude button
      Button excludeButton = new Button(
        userController.getLoggedInUser().getId().equals(user.getId())
          ? "Das bist du ;)"
          : "Ausschließen"
      );
      excludeButton.setDisable(
        userController.getLoggedInUser().getId().equals(user.getId())
      );
      excludeButton.getStyleClass().add("bg-warning");
      excludeButton.setOnAction(event -> {
        try {
          userController.excludeRenterForCurrentlyLoggedInUser(user.getId());
          // reset search
          findUsersTextField.setText("");
          findUsers("");

          reloadExcludedRenters();
        } catch (GenericServiceException ignore) {}
      });
      Tooltip t1 = new Tooltip(
        String.format(
          "Dadurch wird %s von deinen Anzeigen ausgeschlossen",
          user.getUsername()
        )
      );
      Tooltip.install(excludeButton, t1);

      // report button
      Button reportButton = new Button("Diesen Nutzer melden");
      reportButton.setOnAction(event -> {
        try {
          mainViewController.changeView("reportUser");
          reportUserViewController.initialize(user);
        } catch (GenericServiceException ignore) {
        }
      });

      // card
      VBox excludeUserVBox = new VBox(
        usernameLabel,
        nameLabel,
        emailLabel,
        new HBox(excludeButton, reportButton)
      );
      excludeUserVBox.setFillWidth(true);
      excludeUserVBox.setSpacing(5);
      excludeUserVBox.setStyle(
        "-fx-background-color: #c9dfce; -fx-background-radius: 20; -fx-padding: 10;"
      );

      // add card to view
      userResultsVBox.getChildren().add(excludeUserVBox);
    }
  }
}
