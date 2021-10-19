package xtasks.api.contract;

import xtasks.backend.services.AssignmentService;
import xtasks.backend.services.exceptions.GenericServiceException;

public interface IAssignmentController {
  /**
   * see {@link AssignmentService#setAssociatedAssignmentToDone}
   */
  void setAssociatedAssignmentToDone(PersonDTO personDTO, TaskDTO taskDTO)
    throws GenericServiceException;

  /**
   * see {@link AssignmentService#assignTaskToPerson}
   */
  void assignTaskToPerson(TaskDTO taskDTO, PersonDTO personDTO)
    throws GenericServiceException;

  /**
   * see {@link AssignmentService#unassignTaskFromPerson}
   */
  void unassignTaskFromPerson(TaskDTO taskDTO, PersonDTO personDTO)
    throws GenericServiceException;
}
