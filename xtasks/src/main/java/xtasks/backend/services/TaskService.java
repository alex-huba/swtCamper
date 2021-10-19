package xtasks.backend.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xtasks.backend.entities.*;
import xtasks.backend.repositories.TaskRepository;
import xtasks.backend.services.exceptions.GenericServiceException;

@Service
public class TaskService {

  @Autowired
  private TaskRepository taskRepository;

  @Autowired
  private AssignmentService assignmentService;

  /**
   * @return a list of all tasks
   */
  public List<Task> tasks() {
    return taskRepository.findAll();
  }

  /**
   * Computes the task status for a given task depending on its associated assignments.
   *
   * @param task
   * @return
   */
  public TaskStatus getTaskStatus(Task task) {
    List<Assignment> assignments = assignmentService.findByTask(task);
    if (assignments.isEmpty()) {
      return TaskStatus.UNASSIGNED;
    }

    boolean allAssignmentsDone = assignments
      .stream()
      .allMatch(
        assignment ->
          AssignmentStatus.DONE.equals(assignment.getAssignmentStatus())
      );

    if (allAssignmentsDone) {
      return TaskStatus.COMPLETED;
    } else {
      return TaskStatus.IN_PROGRESS;
    }
  }

  /**
   * Validates the title of a task
   *
   * @param title
   * @throws GenericServiceException if the title is null, empty, or blank.
   */
  private void validateTitle(String title) throws GenericServiceException {
    if (title == null) {
      throw new GenericServiceException("The passed title shall not be null");
    }

    if (title.isEmpty()) {
      throw new GenericServiceException("The passed title shall not be empty.");
    }

    if (title.isBlank()) {
      throw new GenericServiceException("The passed title shall not be blank.");
    }
  }

  /**
   * Validates the description of a task.
   *
   * @param description
   * @throws GenericServiceException if the description is null.
   */
  private void validateDescription(String description)
    throws GenericServiceException {
    if (description == null) {
      throw new GenericServiceException("The passed description shall not be.");
    }
  }

  /**
   * Creates a new Task object and persists it in the database.
   *
   * @param title
   * @param description
   * @return the created and persisted Task object
   * @throws GenericServiceException if the provided title or description is not valid
   */
  public Task create(String title, String description)
    throws GenericServiceException {
    validateTitle(title);
    validateDescription(description);

    Task task = new Task();
    task.setTitle(title);
    task.setDescription(description);
    return taskRepository.save(task);
  }

  /**
   * Finds the task with the passed id.
   *
   * @param id
   * @return
   * @throws GenericServiceException if no task with this id exists.
   */
  public Task findById(Long id) throws GenericServiceException {
    Optional<Task> optTask = taskRepository.findById(id);
    if (optTask.isEmpty()) {
      throw new GenericServiceException(
        String.format("No task with id \"%s\" exists.", id)
      );
    }
    return optTask.get();
  }

  /**
   * Finds all tasks for a person that are associated with an assignment having status to-do.
   *
   * @param person
   * @return
   * @throws GenericServiceException
   */
  public List<Task> findTasksAssociatedWithTodoAssignment(Person person) {
    return assignmentService
      .findByPerson(person)
      .stream()
      .filter(
        assignment ->
          AssignmentStatus.TODO.equals(assignment.getAssignmentStatus())
      )
      .map(Assignment::getTask)
      .collect(Collectors.toList());
  }
}
