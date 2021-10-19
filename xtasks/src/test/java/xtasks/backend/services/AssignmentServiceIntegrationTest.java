package xtasks.backend.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;
import xtasks.backend.entities.Assignment;
import xtasks.backend.entities.AssignmentStatus;
import xtasks.backend.entities.Person;
import xtasks.backend.entities.Task;
import xtasks.backend.repositories.AssignmentRepository;
import xtasks.backend.services.exceptions.GenericServiceException;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class AssignmentServiceIntegrationTest {

  @Autowired
  private TaskService taskService;

  @Autowired
  private AssignmentService assignmentService;

  @Autowired
  private AssignmentRepository assignmentRepository;

  @Autowired
  private PersonService personService;

  @Test
  public void assignTaskToPersonShouldStoreAssignmentInDatabase()
    throws GenericServiceException {
    Task task = taskService.create("title", "description");
    Person person = personService.create("name", 1);
    Assignment assignment = assignmentService.assignTaskToPerson(task, person);

    List<Assignment> assignments = assignmentRepository.findAll();
    assertEquals(1, assignments.size());
    Assignment foundAssignment = assignments.get(0);

    assertEquals(assignment, foundAssignment);
  }

  @Test
  public void findByPersonShouldReturnEmptyListIfNoAssociatedAssignmentExists()
    throws GenericServiceException {
    Person person = personService.create("name", 1);
    List<Assignment> assignments = assignmentService.findByPerson(person);
    assertTrue(assignments.isEmpty());
  }

  @Test
  public void findByPersonShouldReturnCorrectAssignment()
    throws GenericServiceException {
    Task task = taskService.create("title", "description");
    Person person = personService.create("name", 1);
    Assignment assignment = assignmentService.assignTaskToPerson(task, person);
    List<Assignment> assignments = assignmentService.findByPerson(person);
    assertEquals(1, assignments.size());
    Assignment foundAssignment = assignments.get(0);

    assertEquals(assignment, foundAssignment);
  }

  @Test
  public void findByTaskShouldReturnEmptyListIfNoAssociatedAssignmentExists()
    throws GenericServiceException {
    Task task = taskService.create("title", "description");

    List<Assignment> assignments = assignmentService.findByTask(task);
    assertTrue(assignments.isEmpty());
  }

  @Test
  public void findByTaskShouldReturnCorrectAssignment()
    throws GenericServiceException {
    Task task = taskService.create("title", "description");
    Person person = personService.create("name", 1);
    Assignment assignment = assignmentService.assignTaskToPerson(task, person);
    List<Assignment> assignments = assignmentService.findByTask(task);
    assertEquals(1, assignments.size());
    Assignment foundAssignment = assignments.get(0);

    assertEquals(assignment, foundAssignment);
  }

  @Test
  public void assignTaskToPersonCreatesMatchingAssignment()
    throws GenericServiceException {
    Task task = taskService.create("title", "description");
    Person person = personService.create("name", 1);

    assignmentService.assignTaskToPerson(task, person);

    List<Assignment> assignments = assignmentRepository.findAll();
    assertEquals(1, assignments.size());
    Assignment assignment = assignments.get(0);
    assertEquals(task, assignment.getTask());
    assertEquals(person, assignment.getPerson());
  }

  @Test
  public void isPersonAssignedToTaskShouldReturnTrueIfAssignmentExists()
    throws GenericServiceException {
    Task task = taskService.create("title", "description");
    Person person = personService.create("name", 1);
    Assignment assignment = new Assignment();
    assignment.setPerson(person);
    assignment.setTask(task);
    assignmentRepository.save(assignment);

    assertTrue(assignmentService.isPersonAssignedToTask(person, task));
  }

  @Test
  public void isPersonAssignedToTaskShouldReturnFalseIfPersonIsNotAssociated()
    throws GenericServiceException {
    Task task = taskService.create("title", "description");
    Task otherTask = taskService.create("title", "description");
    Person person = personService.create("name", 1);
    Person otherPerson = personService.create("name", 1);

    Assignment assignment = new Assignment();
    assignment.setPerson(person);
    assignment.setTask(task);
    assignmentRepository.save(assignment);

    Assignment otherAssignment = new Assignment();
    assignment.setPerson(otherPerson);
    assignment.setTask(otherTask);
    assignmentRepository.save(otherAssignment);

    assertFalse(assignmentService.isPersonAssignedToTask(person, otherTask));

    assertFalse(assignmentService.isPersonAssignedToTask(otherPerson, task));
  }

  @Test(expected = GenericServiceException.class)
  public void removeAssignmentShouldFailIfAssignmentIsDone()
    throws GenericServiceException {
    Task task = taskService.create("title", "description");
    Person person = personService.create("name", 1);
    Assignment assignment = assignmentService.assignTaskToPerson(task, person);
    assignment = assignmentService.setToDone(assignment);

    assignmentService.removeAssignment(assignment);
  }

  @Test
  public void removeAssignmentShouldRemoveFromDatabase()
    throws GenericServiceException {
    Task task = taskService.create("title", "description");
    Person person = personService.create("name", 1);
    Assignment assignment = assignmentService.assignTaskToPerson(task, person);

    assignmentService.removeAssignment(assignment);

    assertTrue(assignmentRepository.findAll().isEmpty());
  }

  @Test(expected = GenericServiceException.class)
  public void assignTaskToPersonShouldFailIfItExceedsThePersonsTaskLimit()
    throws GenericServiceException {
    Task task = taskService.create("title", "description");
    Person person = personService.create("name", 0);
    assignmentService.assignTaskToPerson(task, person);
  }

  @Test(expected = GenericServiceException.class)
  public void assignTaskToPersonShouldFailIfThePersonIsAlreadyAssignedToThisTask()
    throws GenericServiceException {
    Task task = taskService.create("title", "description");
    Person person = personService.create("name", 2);
    assignmentService.assignTaskToPerson(task, person);
    assignmentService.assignTaskToPerson(task, person);
  }

  @Test
  public void assignTaskToPersonShouldStoreTheCorrectAssignmentInTheDatabase()
    throws GenericServiceException {
    Task task = taskService.create("title", "description");
    Person person = personService.create("name", 1);
    Assignment assignment = assignmentService.assignTaskToPerson(task, person);

    List<Assignment> foundAssignments = assignmentRepository.findAll();
    Assignment foundAssignment = foundAssignments.get(0);
    assertEquals(assignment, foundAssignment);
  }

  @Test
  public void setToDoneShouldUpdateAssignmentStatusInDatabaseToDone()
    throws GenericServiceException {
    Task task = taskService.create("title", "description");
    Person person = personService.create("name", 1);
    Assignment assignment = assignmentService.assignTaskToPerson(task, person);

    assignmentService.setToDone(assignment);

    List<Assignment> foundAssignments = assignmentRepository.findAll();
    Assignment foundAssignment = foundAssignments.get(0);
    assertEquals(AssignmentStatus.DONE, foundAssignment.getAssignmentStatus());
  }

  @Test
  public void setAssociatedAssignmentToDoneShouldUpdateAssignmentStatusInDatabaseToDone()
    throws GenericServiceException {
    Task task = taskService.create("title", "description");
    Person person = personService.create("name", 1);
    assignmentService.assignTaskToPerson(task, person);

    assignmentService.setAssociatedAssignmentToDone(person, task);

    List<Assignment> foundAssignments = assignmentRepository.findAll();
    Assignment foundAssignment = foundAssignments.get(0);
    assertEquals(AssignmentStatus.DONE, foundAssignment.getAssignmentStatus());
  }

  @Test(expected = GenericServiceException.class)
  public void setAssociatedAssignmentToDoneShouldFailIfNoAssignmentExists()
    throws GenericServiceException {
    Task task = taskService.create("title", "description");
    Person person = personService.create("name", 1);

    assignmentService.setAssociatedAssignmentToDone(person, task);
  }

  @Test(expected = GenericServiceException.class)
  public void unassignTaskFromPersonShouldFailIfNoAssignmentExists()
    throws GenericServiceException {
    Person person = personService.create("name", 1);
    Task task = taskService.create("title", "description");

    assignmentService.unassignTaskFromPerson(task, person);
  }

  @Test
  public void unassignTaskFromPersonShouldRemoveAssignmentFromDatabase()
    throws GenericServiceException {
    Person person = personService.create("name", 1);
    Task task = taskService.create("title", "description");
    assignmentService.assignTaskToPerson(task, person);

    assignmentService.unassignTaskFromPerson(task, person);

    assertTrue(assignmentRepository.findAll().isEmpty());
  }
}
