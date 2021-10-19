package xtasks.backend.services;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import xtasks.backend.entities.Person;
import xtasks.backend.entities.Task;
import xtasks.backend.repositories.PersonRepository;
import xtasks.backend.services.exceptions.GenericServiceException;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PersonServiceIntegrationTest {

  @Autowired
  private PersonService personService;

  @Autowired
  PersonRepository personRepository;

  @Autowired
  private TaskService taskService;

  @Autowired
  private AssignmentService assignmentService;

  @Test
  public void taskLimitReachedShouldReturnTrueForPersonWithZeroTaskLimit()
    throws GenericServiceException {
    taskService.create("title", "description");
    Person person = personService.create("name", 0);

    assertTrue(personService.taskLimitReached(person));
  }

  @Test
  public void taskLimitReachedShouldReturnFalseIfMoreTasksCanBeAssigned()
    throws GenericServiceException {
    taskService.create("title", "description");
    Person person = personService.create("name", 1);
    assertFalse(personService.taskLimitReached(person));
  }

  @Test(expected = GenericServiceException.class)
  public void findByIdShouldFailIfIdDoesNotExist()
    throws GenericServiceException {
    personService.findById(Long.valueOf(1));
  }

  @Test
  public void findByIdShouldReturnCorrectPerson()
    throws GenericServiceException {
    Person person = personService.create("name", 0);
    Person foundPerson = personService.findById(Long.valueOf(1));
    assertEquals(person, foundPerson);
  }

  @Test
  public void createShouldStorePersonInDatabase()
    throws GenericServiceException {
    Person person = personService.create("name", 0);
    Optional<Person> optFoundPerson = personRepository.findById(person.getId());
    assertTrue(optFoundPerson.isPresent());
    Person foundPerson = optFoundPerson.get();
    assertEquals(person, foundPerson);
  }

  @Test
  public void assignedToTaskShouldBeEmptyIfNoAssignmentsExist()
    throws GenericServiceException {
    Task task = taskService.create("title", "description");
    List<Person> people = personService.assignedToTask(task);
    assertTrue(people.isEmpty());
  }

  @Test
  public void assignedToTaskShouldReturnCorrectPerson()
    throws GenericServiceException {
    Task task = taskService.create("title", "description");
    Person person = personService.create("name", 1);
    assignmentService.assignTaskToPerson(task, person);
    List<Person> people = personService.assignedToTask(task);
    assertEquals(1, people.size());
    assertEquals(person, people.get(0));
  }

  @Test
  public void notAssignedToTaskShouldBeEmptyIfNoPersonIsAssigned()
    throws GenericServiceException {
    Task task = taskService.create("title", "description");
    List<Person> people = personService.notAssignedToTask(task);
    assertTrue(people.isEmpty());
  }

  @Test
  public void notAssignedToTaskShouldReturnCorrectPerson()
    throws GenericServiceException {
    Task task = taskService.create("title", "description");
    Person person = personService.create("name", 1);
    List<Person> people = personService.notAssignedToTask(task);
    assertEquals(1, people.size());
    assertEquals(person, people.get(0));
  }
}
