package xtasks.javafx.controller;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xtasks.api.contract.PersonDTO;
import xtasks.api.contract.TaskDTO;
import xtasks.api.controller.AssignmentController;
import xtasks.api.controller.PersonController;
import xtasks.api.controller.TaskController;
import xtasks.backend.services.exceptions.GenericServiceException;

@Component
public class TodoListViewController {

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private AssignmentController assignmentController;

  @Autowired
  private PersonController personController;

  @Autowired
  private TaskController taskController;

  @FXML
  public ListView<TaskDTO> openList;

  @FXML
  public ComboBox<PersonDTO> userSelection;

  @FXML
  public void taskDoneAction() {
    if (userSelection.getSelectionModel().isEmpty()) {
      return;
    }
    if (openList.getSelectionModel().isEmpty()) {
      return;
    }

    try {
      PersonDTO personDTO = userSelection.getSelectionModel().getSelectedItem();
      TaskDTO taskDTO = openList.getSelectionModel().getSelectedItem();
      assignmentController.setAssociatedAssignmentToDone(personDTO, taskDTO);
    } catch (GenericServiceException e) {
      mainViewController.handleException(e);
    }
    mainViewController.reloadData();
  }

  @FXML
  public void userSelectionAction() {
    List<TaskDTO> todoTasks = new ArrayList<>();
    if (!userSelection.getSelectionModel().isEmpty()) {
      PersonDTO personDTO = userSelection.getSelectionModel().getSelectedItem();
      try {
        todoTasks =
          taskController.findTasksAssociatedWithTodoAssignment(personDTO);
      } catch (GenericServiceException e) {
        mainViewController.handleException(e);
      }
    }

    openList.setItems(FXCollections.observableArrayList(todoTasks));
  }

  public void reloadData() {
    userSelection.setItems(
      FXCollections.observableArrayList(personController.people())
    );
    userSelectionAction();
  }
}
