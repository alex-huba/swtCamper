package xtasks.backend.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xtasks.backend.entities.Assignment;
import xtasks.backend.entities.AssignmentStatus;
import xtasks.backend.entities.Person;
import xtasks.backend.entities.Task;
import xtasks.backend.repositories.AssignmentRepository;
import xtasks.backend.services.exceptions.GenericServiceException;

@Service
public class AssignmentService {

  @Autowired
  private PersonService personService;

  @Autowired
  private AssignmentRepository assignmentRepository;

  /**
   * @param person the considered person
   * @return all associated assignments of that person
   */
  public List<Assignment> findByPerson(Person person) {
    return assignmentRepository.findByPerson(person);
  }

  /**
   * Finds all Assignments associated with the given task.
   *
   * @param task
   * @return
   */
  public List<Assignment> findByTask(Task task) {
    return assignmentRepository.findByTask(task);
  }

  /**
   * Checks if a person is assigned to a task.
   *
   * @param person
   * @param task
   * @return
   */
  public boolean isPersonAssignedToTask(Person person, Task task) {
    return !assignmentRepository.findByPersonAndTask(person, task).isEmpty();
  }

  /**
   * Removes the passed assignment.
   *
   * @param assignment
   * @throws GenericServiceException if the assignment has status DONE
   */
  public void removeAssignment(Assignment assignment)
    throws GenericServiceException {
    if (AssignmentStatus.DONE.equals(assignment.getAssignmentStatus())) {
      throw new GenericServiceException(
        "An assignment with status DONE cannot be removed."
      );
    }
    assignmentRepository.delete(assignment);
  }

  /**
   * Assigns a task to a person.
   *
   * @param task
   * @param person
   * @return
   * @throws GenericServiceException if the person is already assigned to this task
   *                                 or if an assignment would exceed the person's task limit
   */
  public Assignment assignTaskToPerson(Task task, Person person)
    throws GenericServiceException {
    if (isPersonAssignedToTask(person, task)) {
      throw new GenericServiceException(
        "This person is already assigned to this task. The same task can be assigned to the same person at most once."
      );
    }

    if (personService.taskLimitReached(person)) {
      throw new GenericServiceException(
        "The assignment exceeds the person's task limit."
      );
    }

    Assignment assignment = new Assignment();
    assignment.setPerson(person);
    assignment.setTask(task);
    assignment.setAssignmentStatus(AssignmentStatus.TODO);
    return assignmentRepository.save(assignment);
  }

  /**
   * Set assignment status to done.
   *
   * @param assignment
   * @return
   */
  public Assignment setToDone(Assignment assignment) {
    assignment.setAssignmentStatus(AssignmentStatus.DONE);
    return assignmentRepository.save(assignment);
  }

  /**
   * Sets the assignment associated with a particular person and task to done.
   *
   * @param person
   * @param task
   * @return
   * @throws GenericServiceException if the person is not assigned to this task
   */
  public Assignment setAssociatedAssignmentToDone(Person person, Task task)
    throws GenericServiceException {
    if (!isPersonAssignedToTask(person, task)) {
      throw new GenericServiceException("Person is not assigned to this task");
    }

    Assignment assignment = assignmentRepository
      .findByPersonAndTask(person, task)
      .get(0);
    return setToDone(assignment);
  }

  /**
   * Unassigns a person from a task.
   *
   * @param task
   * @param person
   * @throws GenericServiceException if the person is not assigned to this task in the first place.
   */
  public void unassignTaskFromPerson(Task task, Person person)
    throws GenericServiceException {
    if (!isPersonAssignedToTask(person, task)) {
      throw new GenericServiceException(
        String.format(
          "The person with id \"%s\" cannot be unassigned from the task with id \"%s\", because it is not assigned to the task in the first place.",
          person.getId(),
          task.getId()
        )
      );
    }
    List<Assignment> assignments = assignmentRepository.findByPersonAndTask(
      person,
      task
    );
    removeAssignment(assignments.get(0));
  }
}
