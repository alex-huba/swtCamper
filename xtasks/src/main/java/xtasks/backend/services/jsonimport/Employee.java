package xtasks.backend.services.jsonimport;

import xtasks.backend.services.EmployeeJsonParserService;

/**
 * Models the attributes of an employee in order to parse the provided
 * JSON file, which contains a list of employees.
 *
 * See {@link EmployeeJsonParserService}
 */
public class Employee {

  public String name;
  public int working_years;
  public boolean active;

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Employee other = (Employee) obj;
    if (active != other.active) return false;
    if (name == null) {
      if (other.name != null) return false;
    } else if (!name.equals(other.name)) return false;
    return working_years == other.working_years;
  }
}
