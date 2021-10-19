package xtasks.backend.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xtasks.backend.entities.Task;
import xtasks.backend.entities.TaskStatus;
import xtasks.backend.services.exceptions.GenericServiceException;

@Service
public class StatisticsService {

  @Autowired
  private TaskService taskService;

  /**
   * @param tasks tasks considered
   * @param taskStatus considered task status
   * @return the number of tasks having a particular task status.
   */
  private Long numberOfTasksWithStatus(
    List<Task> tasks,
    TaskStatus taskStatus
  ) {
    return tasks
      .stream()
      .filter(task -> taskStatus.equals(taskService.getTaskStatus(task)))
      .count();
  }

  /**
   * Computes a {@link Map} that maps each possible task status to the percentage of tasks having this status among all tasks.
   * The percentage ranges from 0.00 to 1.00, i.e., you have to multiply by 100 to get the percentage value.
   * <br><br>
   *  Example: given the following tasks present in the system ...
   *  <ul>
   *  <li>a task with status {@link TaskStatus#UNASSIGNED}</li>
   *  <li>a task with status {@link TaskStatus#IN_PROGRESS}</li>
   *  <li>a task with status {@link TaskStatus#COMPLETED}</li>
   *  </ul>
   *  the derived {@link Map} looks as follows:
   *  <br><br>
   *  <ul>
   *  <li>{@link TaskStatus#UNASSIGNED} --> 0.33333</li>
   *  <li>{@link TaskStatus#IN_PROGRESS} --> 0.33333</li>
   *  <li>{@link TaskStatus#COMPLETED} --> 0.33333</li>
   *  </ul>
   * @return
   * @throws GenericServiceException if no tasks are present at all
   */
  public Map<TaskStatus, Double> taskDistribution()
    throws GenericServiceException {
    List<Task> tasks = taskService.tasks();

    if (tasks.isEmpty()) {
      throw new GenericServiceException(
        "Failed to compute task distribution, because no tasks exist."
      );
    }

    Double numberOfTasks = (double) tasks.size();
    Map<TaskStatus, Double> distribution = new HashMap<>();

    for (TaskStatus status : TaskStatus.values()) {
      distribution.put(
        status,
        numberOfTasksWithStatus(tasks, status) / numberOfTasks
      );
    }

    return distribution;
  }
}
