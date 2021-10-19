package xtasks.backend.services;

import static org.junit.Assert.assertEquals;

import java.util.Map;
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
import xtasks.backend.services.exceptions.GenericServiceException;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class StatisticsServiceIntegrationTest {

  @Autowired
  private StatisticsService statisticsService;

  @Autowired
  private TaskService taskService;

  @Autowired
  private AssignmentService assignmentService;

  @Autowired
  private PersonService personService;

  @Test(expected = GenericServiceException.class)
  public void taskDistributionShouldFailIfNoTasksExist()
    throws GenericServiceException {
    statisticsService.taskDistribution();
  }

  @Test
  public void taskDistributionShouldBeCorrectIfOnlyUnassignedTaskExist()
    throws GenericServiceException {
    createTaskWithStatusUnassigned(1);

    Map<TaskStatus, Double> distribution = statisticsService.taskDistribution();

    assertEquals(1.0, distribution.get(TaskStatus.UNASSIGNED), 0.01);
    assertEquals(0.0, distribution.get(TaskStatus.IN_PROGRESS), 0.01);
    assertEquals(0.0, distribution.get(TaskStatus.COMPLETED), 0.01);
  }

  @Test
  public void taskDistributionShouldBeCorrectIfOnlyInProgressTaskExist()
    throws GenericServiceException {
    createTaskWithStatusInProgress(1);

    Map<TaskStatus, Double> distribution = statisticsService.taskDistribution();

    assertEquals(0.0, distribution.get(TaskStatus.UNASSIGNED), 0.01);
    assertEquals(1.0, distribution.get(TaskStatus.IN_PROGRESS), 0.01);
    assertEquals(0.0, distribution.get(TaskStatus.COMPLETED), 0.01);
  }

  @Test
  public void taskDistributionShouldBeCorrectIfOnlyCompletedTaskExist()
    throws GenericServiceException {
    createTaskWithStatusCompleted(1);

    Map<TaskStatus, Double> distribution = statisticsService.taskDistribution();

    assertEquals(0.0, distribution.get(TaskStatus.UNASSIGNED), 0.01);
    assertEquals(0.0, distribution.get(TaskStatus.IN_PROGRESS), 0.01);
    assertEquals(1.0, distribution.get(TaskStatus.COMPLETED), 0.01);
  }

  @Test
  public void taskDistributionShouldBeCorrectForTasksWithDifferentStatus()
    throws GenericServiceException {
    createTaskWithStatusUnassigned(6);
    createTaskWithStatusInProgress(3);
    createTaskWithStatusCompleted(1);

    Map<TaskStatus, Double> distribution = statisticsService.taskDistribution();

    assertEquals(0.6, distribution.get(TaskStatus.UNASSIGNED), 0.01);
    assertEquals(0.3, distribution.get(TaskStatus.IN_PROGRESS), 0.01);
    assertEquals(0.1, distribution.get(TaskStatus.COMPLETED), 0.01);
  }

  private void createTaskWithStatusUnassigned(int number)
    throws GenericServiceException {
    for (int i = 0; i < number; i++) {
      taskService.create("title", "description");
    }
  }

  private void createTaskWithStatusInProgress(int number)
    throws GenericServiceException {
    for (int i = 0; i < number; i++) {
      Task task = taskService.create("title", "description");
      Person person = personService.create("name", 1);
      assignmentService.assignTaskToPerson(task, person);
    }
  }

  private void createTaskWithStatusCompleted(int number)
    throws GenericServiceException {
    for (int i = 0; i < number; i++) {
      Task task = taskService.create("title", "description");
      Person person = personService.create("name", 1);
      Assignment assignment = assignmentService.assignTaskToPerson(
        task,
        person
      );
      assignmentService.setToDone(assignment);
    }
  }
}
