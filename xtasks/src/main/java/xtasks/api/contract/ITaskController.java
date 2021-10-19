package xtasks.api.contract;

import java.util.List;
import xtasks.backend.services.TaskService;
import xtasks.backend.services.exceptions.GenericServiceException;

public interface ITaskController {
  /**
   * see {@link TaskService#create}
   */
  TaskDTO create(String title, String description)
    throws GenericServiceException;

  /**
   * see {@link TaskService#tasks}
   */
  List<TaskDTO> tasks() throws GenericServiceException;

  /**
   * see {@link TaskService#findTasksAssociatedWithTodoAssignment}
   */
  List<TaskDTO> findTasksAssociatedWithTodoAssignment(PersonDTO personDTO)
    throws GenericServiceException;
}
