package xtasks.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xtasks.backend.services.exceptions.GenericServiceException;

@Component
public class MainViewController {

  @Autowired
  private StatisticsViewController statisticsViewController;

  @Autowired
  private TaskAssignmentViewController taskAssignmentViewController;

  @Autowired
  private TodoListViewController todoListViewController;

  @FXML
  private void initialize() {
    reloadData();
  }

  public void reloadData() {
    try {
      todoListViewController.reloadData();
      statisticsViewController.reloadData();
      taskAssignmentViewController.reloadData();
    } catch (GenericServiceException e) {
      handleException(e);
    }
  }

  public void handleExceptionMessage(String message) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Exception");
    alert.setHeaderText("There has been an error processing your request");
    alert.setContentText(String.format("Message: %s", message));
    alert.showAndWait();
  }

  public void handleInformationMessage(String message) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Information");
    alert.setHeaderText("Note the following");
    alert.setContentText(String.format("Message: %s", message));
    alert.showAndWait();
  }

  public void handleException(Exception e) {
    handleExceptionMessage(e.getMessage());
  }
}
