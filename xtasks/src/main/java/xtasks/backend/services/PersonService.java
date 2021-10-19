package xtasks.backend.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xtasks.backend.entities.Assignment;
import xtasks.backend.entities.AssignmentStatus;
import xtasks.backend.entities.Person;
import xtasks.backend.entities.Task;
import xtasks.backend.repositories.PersonRepository;
import xtasks.backend.services.exceptions.GenericServiceException;
import xtasks.backend.services.jsonimport.Employee;

@Service
public class PersonService {

  @Autowired
  private AssignmentService assignmentService;

  @Autowired
  private PersonRepository personRepository;

  @Autowired
  private EmployeeJsonParserService employeeJsonParserService;

  /**
   * Imports the people specified in the passed JSON String. Fails if at least one entry is invalid.
   * In case of a failure, the database is not modified.
   *
   * @param json parsed via {@link EmployeeJsonParserService}
   * @throws GenericServiceException if the JSON cannot be parsed or contained entries are invalid
   */
  public void importPeopleFromJSON(String json) throws GenericServiceException {
    List<Employee> employees = employeeJsonParserService
      .parseJsonEmployeeList(json)
      .stream()
      .filter(employee -> employee.active)
      .collect(Collectors.toList());

    // validate all employees
    for (Employee employee : employees) {
      try {
        validateName(employee.name);
        validateTaskLimit(employee.working_years);
      } catch (GenericServiceException e) {
        throw new GenericServiceException(
          String.format(
            "The provided data for employee \"%s\" is invalid.",
            employee.toString()
          ),
          e
        );
      }
    }

    for (Employee employee : employees) {
      try {
        create(employee.name, employee.working_years);
      } catch (GenericServiceException e) {
        // this should never happen, because we have validated all entries before
        throw new GenericServiceException(
          "An error occurred. This should not have happened. Please contact your system administrator.",
          e
        );
      }
    }
  }

  /**
   * @param person the respective person
   * @return if the person's task limit has been reached
   */
  public boolean taskLimitReached(Person person) {
    return (
      assignmentService
        .findByPerson(person)
        .stream()
        .filter(a -> AssignmentStatus.TODO.equals(a.getAssignmentStatus()))
        .count() >=
      person.getTaskLimit()
    );
  }

  /**
   * Finds a person using its unique id.
   *
   * @param personId
   * @return
   * @throws GenericServiceException if no person with this id exists.
   */
  public Person findById(Long personId) throws GenericServiceException {
    Optional<Person> optPerson = personRepository.findById(personId);
    if (optPerson.isEmpty()) {
      throw new GenericServiceException(
        String.format("No person with id \"%s\" exists", personId)
      );
    }
    return optPerson.get();
  }

  /**
   * Validates the task limit of a person.
   *
   * @param task_limit
   * @throws GenericServiceException if the task limit is less than 0
   */
  private void validateTaskLimit(int task_limit)
    throws GenericServiceException {
    if (task_limit < 0) {
      throw new GenericServiceException(
        String.format(
          "The task limit of a person shall not be less than 0, but was %d.",
          task_limit
        )
      );
    }
  }

  /**
   * Validates the name of a person.
   *
   * @param name
   * @throws GenericServiceException if the passed name is null, empty or blank.
   */
  private void validateName(String name) throws GenericServiceException {
    if (name == null) {
      throw new GenericServiceException("The passed name is null.");
    }
    if (name.isEmpty()) {
      throw new GenericServiceException("The passed name is empty.");
    }

    if (name.isBlank()) {
      throw new GenericServiceException("The passed name is blank.");
    }
  }

  /**
   * Creates and stores a new person in the database with the provided name and task limit.
   *
   * @param name
   * @param taskLimit
   * @return
   * @throws GenericServiceException if the name or task limit is invalid
   */
  public Person create(String name, int taskLimit)
    throws GenericServiceException {
    validateName(name);
    validateTaskLimit(taskLimit);

    Person person = new Person();
    person.setName(name);
    person.setTaskLimit(taskLimit);
    return personRepository.save(person);
  }

  /**
   * Finds all people.
   *
   * @return
   */
  public List<Person> people() {
    return personRepository.findAll();
  }

  /**
   * Finds all people not assigned to a particular task.
   *
   * @param task
   * @return
   */
  public List<Person> notAssignedToTask(Task task) {
    List<Person> assignedToTask = assignedToTask(task);
    return people()
      .stream()
      .filter(person -> !assignedToTask.contains(person))
      .collect(Collectors.toList());
  }

  /**
   * Finds all people assigned to a particular task.
   *
   * @param task
   * @return
   */
  public List<Person> assignedToTask(Task task) {
    return assignmentService
      .findByTask(task)
      .stream()
      .map(Assignment::getPerson)
      .collect(Collectors.toList());
  }
}
