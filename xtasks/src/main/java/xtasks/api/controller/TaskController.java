package xtasks.api.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xtasks.api.ModelMapper;
import xtasks.api.contract.ITaskController;
import xtasks.api.contract.PersonDTO;
import xtasks.api.contract.TaskDTO;
import xtasks.backend.services.PersonService;
import xtasks.backend.services.TaskService;
import xtasks.backend.services.exceptions.GenericServiceException;

@Component
public class TaskController implements ITaskController {

  @Autowired
  TaskService taskService;

  @Autowired
  PersonService personService;

  @Autowired
  ModelMapper modelMapper;

  @Override
  public TaskDTO create(String title, String description)
    throws GenericServiceException {
    return modelMapper.taskToTaskDTO(taskService.create(title, description));
  }

  @Override
  public List<TaskDTO> tasks() throws GenericServiceException {
    return modelMapper.tasksToTaskDTOs(taskService.tasks());
  }

  @Override
  public List<TaskDTO> findTasksAssociatedWithTodoAssignment(
    PersonDTO personDTO
  )
    throws GenericServiceException {
    return modelMapper.tasksToTaskDTOs(
      taskService.findTasksAssociatedWithTodoAssignment(
        personService.findById(personDTO.getId())
      )
    );
  }
}
