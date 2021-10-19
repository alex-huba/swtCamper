package xtasks.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import xtasks.backend.services.exceptions.GenericServiceException;
import xtasks.backend.services.jsonimport.Employee;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeJsonParserUnitTest {

  @Spy
  private EmployeeJsonParserService employeeJsonParser;

  private String readJsonTestData(String name) throws IOException {
    return new String(
      getClass()
        .getClassLoader()
        .getResourceAsStream("jsonimporter/" + name + ".json")
        .readAllBytes()
    );
  }

  @Test
  public void parseJsonEmployeeListShouldReturnEmptyList()
    throws GenericServiceException, IOException {
    String json = readJsonTestData("no-employees");

    List<Employee> actual = new EmployeeJsonParserService()
    .parseJsonEmployeeList(json);

    assertEquals(new ArrayList<>(), actual);
  }

  @Test
  public void parseJsonEmployeeListShouldReturnCorrectPersons()
    throws GenericServiceException, IOException {
    String json = readJsonTestData("spec-example");
    List<Employee> employees = new EmployeeJsonParserService()
    .parseJsonEmployeeList(json);

    Employee john = new Employee();
    john.name = "John";
    john.working_years = 3;
    john.active = true;

    Employee jane = new Employee();
    jane.name = "Jane";
    jane.working_years = 20;
    jane.active = false;

    assertEquals(2, employees.size());

    assertEquals(john, employees.get(0));
    assertEquals(jane, employees.get(1));
  }

  @Test(expected = GenericServiceException.class)
  public void parseJsonEmployeeListShouldFailIfJSONIsCorrupt()
    throws GenericServiceException, IOException {
    String json = readJsonTestData("corrupt");
    new EmployeeJsonParserService().parseJsonEmployeeList(json);
  }
}
