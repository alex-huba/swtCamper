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
public class TaskAssignmentViewController {

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private AssignmentController assignmentController;

  @Autowired
  private TaskController taskController;

  @Autowired
  private PersonController personController;

  @FXML
  public ComboBox<TaskDTO> taskList;

  @FXML
  public ListView<PersonDTO> unassignedPersonList;

  @FXML
  public ListView<PersonDTO> assignedPersonList;

  @FXML
  public void assignButtonAction() {
    if (
      unassignedPersonList.getSelectionModel().isEmpty() ||
      taskList.getSelectionModel().isEmpty()
    ) {
      return;
    }
    PersonDTO personDTO = unassignedPersonList
      .getSelectionModel()
      .getSelectedItem();
    TaskDTO taskDTO = taskList.getSelectionModel().getSelectedItem();
    try {
      assignmentController.assignTaskToPerson(taskDTO, personDTO);
    } catch (GenericServiceException e) {
      mainViewController.handleException(e);
    }
    mainViewController.reloadData();
  }

  @FXML
  public void unassignButtonAction() throws GenericServiceException {
    if (
      assignedPersonList.getSelectionModel().isEmpty() ||
      taskList.getSelectionModel().isEmpty()
    ) {
      return;
    }
    PersonDTO personDTO = assignedPersonList
      .getSelectionModel()
      .getSelectedItem();
    TaskDTO taskDTO = taskList.getSelectionModel().getSelectedItem();
    try {
      assignmentController.unassignTaskFromPerson(taskDTO, personDTO);
    } catch (GenericServiceException e) {
      mainViewController.handleException(e);
    }
    reloadData();
  }

  @FXML
  public void taskListAction() {
    List<PersonDTO> notAssigned = new ArrayList<>();
    List<PersonDTO> assigned = new ArrayList<>();
    if (!taskList.getSelectionModel().isEmpty()) {
      try {
        TaskDTO taskDTO = taskList.getSelectionModel().getSelectedItem();
        notAssigned = personController.notAssignedToTask(taskDTO);
        assigned = personController.assignedToTask(taskDTO);
      } catch (GenericServiceException e) {
        mainViewController.handleException(e);
      }
    }
    unassignedPersonList.setItems(
      FXCollections.observableArrayList(notAssigned)
    );

    assignedPersonList.setItems(FXCollections.observableArrayList(assigned));
  }

  public void reloadData() throws GenericServiceException {
    taskList.setItems(
      FXCollections.observableArrayList(taskController.tasks())
    );
  }
}
