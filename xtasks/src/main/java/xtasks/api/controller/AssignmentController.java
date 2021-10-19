package xtasks.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xtasks.api.contract.IAssignmentController;
import xtasks.api.contract.PersonDTO;
import xtasks.api.contract.TaskDTO;
import xtasks.backend.entities.Person;
import xtasks.backend.entities.Task;
import xtasks.backend.services.AssignmentService;
import xtasks.backend.services.PersonService;
import xtasks.backend.services.TaskService;
import xtasks.backend.services.exceptions.GenericServiceException;

@Component
public class AssignmentController implements IAssignmentController {

  @Autowired
  private AssignmentService assignmentService;

  @Autowired
  private PersonService personService;

  @Autowired
  private TaskService taskService;

  @Override
  public void setAssociatedAssignmentToDone(
    PersonDTO personDTO,
    TaskDTO taskDTO
  )
    throws GenericServiceException {
    Person person = personService.findById(personDTO.getId());
    Task task = taskService.findById(taskDTO.getId());
    assignmentService.setAssociatedAssignmentToDone(person, task);
  }

  @Override
  public void assignTaskToPerson(TaskDTO taskDTO, PersonDTO personDTO)
    throws GenericServiceException {
    assignmentService.assignTaskToPerson(
      taskService.findById(taskDTO.getId()),
      personService.findById(personDTO.getId())
    );
  }

  @Override
  public void unassignTaskFromPerson(TaskDTO taskDTO, PersonDTO personDTO)
    throws GenericServiceException {
    assignmentService.unassignTaskFromPerson(
      taskService.findById(taskDTO.getId()),
      personService.findById(personDTO.getId())
    );
  }
}
