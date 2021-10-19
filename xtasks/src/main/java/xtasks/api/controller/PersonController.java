package xtasks.api.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xtasks.api.ModelMapper;
import xtasks.api.contract.IPersonController;
import xtasks.api.contract.PersonDTO;
import xtasks.api.contract.TaskDTO;
import xtasks.backend.services.PersonService;
import xtasks.backend.services.TaskService;
import xtasks.backend.services.exceptions.GenericServiceException;

@Component
public class PersonController implements IPersonController {

  @Autowired
  PersonService personService;

  @Autowired
  TaskService taskService;

  @Autowired
  ModelMapper modelMapper;

  @Override
  public List<PersonDTO> people() {
    return modelMapper.personsToPersonDTOs(personService.people());
  }

  @Override
  public List<PersonDTO> notAssignedToTask(TaskDTO taskDTO)
    throws GenericServiceException {
    return modelMapper.personsToPersonDTOs(
      personService.notAssignedToTask(taskService.findById(taskDTO.getId()))
    );
  }

  @Override
  public List<PersonDTO> assignedToTask(TaskDTO taskDTO)
    throws GenericServiceException {
    return modelMapper.personsToPersonDTOs(
      personService.assignedToTask(taskService.findById(taskDTO.getId()))
    );
  }

  @Override
  public void importPeopleFromJSON(String json) throws GenericServiceException {
    personService.importPeopleFromJSON(json);
  }
}
