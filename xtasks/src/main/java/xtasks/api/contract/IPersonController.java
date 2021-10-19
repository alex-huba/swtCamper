package xtasks.api.contract;

import java.util.List;
import xtasks.backend.services.PersonService;
import xtasks.backend.services.exceptions.GenericServiceException;

public interface IPersonController {
  /**
   * see {@link PersonService#people}
   */
  List<PersonDTO> people();

  /**
   * see {@link PersonService#notAssignedToTask}
   */
  List<PersonDTO> notAssignedToTask(TaskDTO taskDTO)
    throws GenericServiceException;

  /**
   * see {@link PersonService#assignedToTask}
   */
  List<PersonDTO> assignedToTask(TaskDTO taskDTO)
    throws GenericServiceException;

  /**
   * see {@link PersonService#importPeopleFromJSON}
   */
  void importPeopleFromJSON(String json) throws GenericServiceException;
}
