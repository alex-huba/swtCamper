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
import swtcamper.api.contract.UserReportDTO;
import swtcamper.api.contract.interfaces.IUserController;
import swtcamper.api.contract.interfaces.IUserReportController;
import swtcamper.backend.entities.User;
import swtcamper.backend.services.exceptions.GenericServiceException;

@Component
public class ExcludeRenterViewController {

  @FXML
  private VBox excludedRentersVBox;

  @FXML
  private TextField findUsersTextField;

  @FXML
  private VBox userResultsVBox;

  @Autowired
  private IUserController userController;

  @Autowired
  private IUserReportController userReportController;

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private ReportUserViewController reportUserViewController;

  @FXML
  public void initialize() throws GenericServiceException {
    reloadExcludedRenters();
    findUsers(findUsersTextField.getText());
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
      Button includeButton = new Button("Ausschlie??en beenden");
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

  /**
   * Searches for users that fit to the given searchText and displays the result in userResultsVBox
   *
   * @param searchText String that shall be used to filter available usernames
   * @throws GenericServiceException
   */
  private void findUsers(String searchText) throws GenericServiceException {
    userResultsVBox.getChildren().clear();
    if (searchText.isEmpty()) return;

    // only show results if searchText fully equals the needed username (privacy reasons)
    // note: it uses a for loop, even if the result can actually only consist of one User,
    //       but equals() could be replaced by startsWith() or similar...
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
          : "Ausschlie??en"
      );
      excludeButton.setDisable(
        userController.getLoggedInUser().getId().equals(user.getId())
      );
      excludeButton.getStyleClass().add("bg-warning");
      excludeButton.setOnAction(event -> {
        try {
          userController.excludeRenterForCurrentlyLoggedInUser(user.getId());
          // reset search
          findUsersTextField.clear();
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
      // determine whether there is another active report from this user about that user already
      boolean isThisUserAlreadyReportedByLoggedInUser = false;
      for (UserReportDTO userReportDTO : userReportController.getAllUserReports()) {
        if (
          userReportDTO.getReportee().getId().equals(user.getId()) &&
          userReportDTO
            .getReporter()
            .getId()
            .equals(userController.getLoggedInUser().getId()) &&
          userReportDTO.isActive()
        ) {
          isThisUserAlreadyReportedByLoggedInUser = true;
          break;
        }
      }
      // disable report button if a similar report is already active or if it concerns the user him-/herself
      reportButton.setDisable(
        isThisUserAlreadyReportedByLoggedInUser ||
        userController.getLoggedInUser().getId().equals(user.getId())
      );
      reportButton.getStyleClass().add("bg-warning");
      reportButton.setOnAction(event -> {
        try {
          // reset textField and search
          findUsersTextField.clear();
          findUsers("");

          // redirect to report form
          mainViewController.changeView("reportUser");
          reportUserViewController.initialize(user);
        } catch (GenericServiceException ignore) {}
      });

      HBox buttonHBox = new HBox(excludeButton, reportButton);
      buttonHBox.setSpacing(5);

      // card
      VBox excludeUserVBox = new VBox(
        usernameLabel,
        nameLabel,
        emailLabel,
        buttonHBox
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
