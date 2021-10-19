package xtasks.backend.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import xtasks.backend.entities.Person;
import xtasks.backend.services.exceptions.GenericServiceException;
import xtasks.backend.services.jsonimport.Employee;

@RunWith(MockitoJUnitRunner.class)
public class PersonServiceUnitTest {

  @Spy
  @InjectMocks
  private PersonService personService;

  @Spy
  private EmployeeJsonParserService employeeJsonParserService;

  private Employee johnActive() {
    Employee john = new Employee();
    john.name = "John";
    john.working_years = 3;
    john.active = true;
    return john;
  }

  private Employee janeInactive() {
    Employee jane = new Employee();
    jane.name = "Jane";
    jane.working_years = 20;
    jane.active = false;
    return jane;
  }

  private Employee invalidEmptyName() {
    Employee invalidName = new Employee();
    invalidName.name = "";
    invalidName.working_years = 20;
    invalidName.active = true;
    return invalidName;
  }

  private Employee invalidNameWithOnlyBlanks() {
    Employee invalidName = new Employee();
    invalidName.name = "             ";
    invalidName.working_years = 20;
    invalidName.active = true;
    return invalidName;
  }

  private Employee negativeTaskLimit() {
    Employee invalidTaskLimit = new Employee();
    invalidTaskLimit.name = "Name";
    invalidTaskLimit.working_years = -1;
    invalidTaskLimit.active = true;
    return invalidTaskLimit;
  }

  private void mockParseJsonEmployeeList(List<Employee> mockEmployees)
    throws GenericServiceException {
    doReturn(mockEmployees)
      .when(employeeJsonParserService)
      .parseJsonEmployeeList(any());
  }

  @Test(expected = GenericServiceException.class)
  public void importPeopleFromJSONShouldFailIfTaskLimitIsNegative()
    throws GenericServiceException {
    mockParseJsonEmployeeList(Arrays.asList(negativeTaskLimit()));
    personService.importPeopleFromJSON("");
  }

  @Test(expected = GenericServiceException.class)
  public void importPeopleFromJSONShouldFailIfNameIsEmpty()
    throws GenericServiceException {
    mockParseJsonEmployeeList(Arrays.asList(invalidEmptyName()));
    personService.importPeopleFromJSON("");
  }

  @Test(expected = GenericServiceException.class)
  public void importPeopleFromJSONShouldFailIfNameContainsOnlyBlanks()
    throws GenericServiceException {
    mockParseJsonEmployeeList(Arrays.asList(invalidNameWithOnlyBlanks()));
    personService.importPeopleFromJSON("");
  }

  @Test
  public void importPeopleFromJSONShouldCreateOnlyActiveEmployees()
    throws GenericServiceException {
    mockParseJsonEmployeeList(Arrays.asList(johnActive(), janeInactive()));

    doReturn(new Person()).when(personService).create(any(), anyInt());

    ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Integer> integerCaptor = ArgumentCaptor.forClass(
      Integer.class
    );

    personService.importPeopleFromJSON("");
    verify(personService, times(1))
      .create(stringCaptor.capture(), integerCaptor.capture());
    assertEquals(johnActive().name, stringCaptor.getValue());
    assertEquals(
      Integer.valueOf(johnActive().working_years),
      integerCaptor.getValue()
    );
  }

  @Test(expected = GenericServiceException.class)
  public void createShouldFailIfNameIsNull() throws GenericServiceException {
    personService.create(null, 0);
  }

  @Test(expected = GenericServiceException.class)
  public void createShouldFailIfNameIsEmpty() throws GenericServiceException {
    personService.create("", 0);
  }

  @Test(expected = GenericServiceException.class)
  public void createShouldFailIfNameContainsOnlyBlanks()
    throws GenericServiceException {
    personService.create("   ", 0);
  }

  @Test(expected = GenericServiceException.class)
  public void createShouldFailIfTaskLimitIsNegative()
    throws GenericServiceException {
    personService.create("name", -1);
  }
}
