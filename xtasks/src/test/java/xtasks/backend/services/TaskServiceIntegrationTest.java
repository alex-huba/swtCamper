package xtasks.backend.services;

import static org.junit.Assert.assertEquals;
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
import xtasks.backend.entities.Person;
import xtasks.backend.entities.Task;
import xtasks.backend.entities.TaskStatus;
import xtasks.backend.repositories.TaskRepository;
import xtasks.backend.services.exceptions.GenericServiceException;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class TaskServiceIntegrationTest {

  @Autowired
  private TaskService taskService;

  @Autowired
  private TaskRepository taskRepository;

  @Autowired
  private AssignmentService assignmentService;

  @Autowired
  private PersonService personService;

  @Test
  public void tasksShouldReturnAnEmptyListIfTheDatabaseIsEmpty() {
    List<Task> tasks = taskService.tasks();
    assertTrue(tasks.isEmpty());
  }

  @Test
  public void tasksShouldReturnCorrectTask() throws GenericServiceException {
    Task task = taskService.create("title", "description");

    List<Task> tasks = taskService.tasks();
    assertEquals(1, tasks.size());
    Task foundTask = tasks.get(0);

    assertEquals(task, foundTask);
  }

  @Test
  public void getTaskStatusShouldReturnUnassignedForNewTask()
    throws GenericServiceException {
    Task task = taskService.create("title", "description");
    TaskStatus taskStatus = taskService.getTaskStatus(task);
    assertEquals(TaskStatus.UNASSIGNED, taskStatus);
  }

  @Test
  public void getTaskStatusShouldReturnInProgressForTaskWithOneAssignment()
    throws GenericServiceException {
    Task task = taskService.create("title", "description");
    Person person = personService.create("name", 1);
    assignmentService.assignTaskToPerson(task, person);

    TaskStatus taskStatus = taskService.getTaskStatus(task);
    assertEquals(TaskStatus.IN_PROGRESS, taskStatus);
  }

  @Test
  public void getTaskStatusShouldReturnInProgressIfNotAllAssignmentsDone()
    throws GenericServiceException {
    Task task = taskService.create("title", "description");
    Person person1 = personService.create("name", 1);
    assignmentService.assignTaskToPerson(task, person1);

    Person person2 = personService.create("name", 1);
    Assignment assignment2 = assignmentService.assignTaskToPerson(
      task,
      person2
    );
    assignmentService.setToDone(assignment2);

    TaskStatus taskStatus = taskService.getTaskStatus(task);
    assertEquals(TaskStatus.IN_PROGRESS, taskStatus);
  }

  @Test
  public void getTaskStatusShouldReturnCompletedIfAllAssignmentsDone()
    throws GenericServiceException {
    Task task = taskService.create("title", "description");
    Person person = personService.create("name", 1);
    Assignment assignment = assignmentService.assignTaskToPerson(task, person);
    assignmentService.setToDone(assignment);

    TaskStatus taskStatus = taskService.getTaskStatus(task);
    assertEquals(TaskStatus.COMPLETED, taskStatus);
  }

  @Test
  public void createShouldStoreTaskInDatabase() throws GenericServiceException {
    Task task = taskService.create("title", "description");

    Task foundTask = taskRepository.findById(task.getId()).get();
    assertEquals(task, foundTask);
  }

  @Test(expected = GenericServiceException.class)
  public void findByIdShouldFailIfIDDoesNotExist()
    throws GenericServiceException {
    taskService.findById(Long.valueOf(101));
  }

  @Test
  public void findByIdShouldReturnCorrectTask() throws GenericServiceException {
    Task task = taskService.create("title", "description");
    Task foundTask = taskService.findById(task.getId());
    assertEquals(task, foundTask);
  }

  @Test
  public void findTasksAssociatedWithTodoAssignmentShouldReturnCorrectTask()
    throws GenericServiceException {
    Person person = personService.create("name", 1);
    Task task = taskService.create("title", "description");
    assignmentService.assignTaskToPerson(task, person);
    List<Task> tasks = taskService.findTasksAssociatedWithTodoAssignment(
      person
    );
    assertEquals(1, tasks.size());
    assertEquals(task, tasks.get(0));
  }

  @Test
  public void findTasksAssociatedWithTodoAssignmentShouldReturnNoTaskIfAllDone()
    throws GenericServiceException {
    Task task = taskService.create("title", "description");
    Person person = personService.create("name", 1);
    Assignment assignment = assignmentService.assignTaskToPerson(task, person);
    assignmentService.setToDone(assignment);
    List<Task> tasks = taskService.findTasksAssociatedWithTodoAssignment(
      person
    );
    assertTrue(tasks.isEmpty());
  }
}
