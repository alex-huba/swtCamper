package xtasks.api;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xtasks.api.contract.PersonDTO;
import xtasks.api.contract.TaskDTO;
import xtasks.api.contract.TaskStatusDTO;
import xtasks.backend.entities.Person;
import xtasks.backend.entities.Task;
import xtasks.backend.entities.TaskStatus;
import xtasks.backend.services.TaskService;
import xtasks.backend.services.exceptions.GenericServiceException;

/**
 * Maps entities to DTOs.
 */
@Component
public class ModelMapper {

  @Autowired
  TaskService taskService;

  public PersonDTO personToPersonDTO(Person person) {
    return new PersonDTO(
      person.getId(),
      person.getName(),
      person.getTaskLimit()
    );
  }

  public List<PersonDTO> personsToPersonDTOs(List<Person> persons) {
    return persons
      .stream()
      .map(person -> personToPersonDTO(person))
      .collect(Collectors.toList());
  }

  public TaskStatusDTO toTaskStatusDTO(TaskStatus taskStatus)
    throws GenericServiceException {
    switch (taskStatus) {
      case COMPLETED:
        return TaskStatusDTO.COMPLETED;
      case IN_PROGRESS:
        return TaskStatusDTO.IN_PROGRESS;
      case UNASSIGNED:
        return TaskStatusDTO.UNASSIGNED;
      default:
        throw new GenericServiceException("TaskStatus is invalid.");
    }
  }

  public TaskDTO taskToTaskDTO(Task task) throws GenericServiceException {
    return new TaskDTO(
      task.getId(),
      task.getTitle(),
      task.getDescription(),
      toTaskStatusDTO(taskService.getTaskStatus(task))
    );
  }

  public List<TaskDTO> tasksToTaskDTOs(List<Task> tasks)
    throws GenericServiceException {
    List<TaskDTO> taskDTOs = new ArrayList<>();
    for (Task task : tasks) {
      taskDTOs.add(taskToTaskDTO(task));
    }
    return taskDTOs;
  }
}
