package xtasks.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xtasks.api.contract.TaskDTO;
import xtasks.api.controller.TaskController;
import xtasks.backend.services.exceptions.GenericServiceException;

@Component
public class TaskCreationViewController {

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private TaskController taskController;

  @FXML
  public TextArea newTaskDescription;

  @FXML
  public TextField newTaskTitle;

  @FXML
  public void createNewTask() {
    try {
      TaskDTO taskDTO = taskController.create(
        newTaskTitle.getText(),
        newTaskDescription.getText()
      );
      mainViewController.handleInformationMessage(
        String.format("The new task \"%s\" has been created.", taskDTO)
      );
    } catch (GenericServiceException e) {
      mainViewController.handleException(e);
    }
    mainViewController.reloadData();
  }
}
