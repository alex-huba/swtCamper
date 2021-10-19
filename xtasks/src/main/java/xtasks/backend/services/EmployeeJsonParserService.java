package xtasks.backend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.stereotype.Service;
import xtasks.backend.services.exceptions.GenericServiceException;
import xtasks.backend.services.jsonimport.Employee;

/**
 * Parses the specified JSON file to retrieve a list of employees.
 *
 */
@Service
public class EmployeeJsonParserService {

  /**
   * Parses a JSON String containing a list of employees.
   *
   * @param json
   * @return
   * @throws GenericServiceException
   */
  public List<Employee> parseJsonEmployeeList(String json)
    throws GenericServiceException {
    try {
      return new ObjectMapper().readValue(json, new TypeReference<>() {});
    } catch (JsonProcessingException e) {
      throw new GenericServiceException(
        String.format(
          "Failed to parse the JSON string \"%s\" with exception message \"%s\"",
          json,
          e.getMessage()
        )
      );
    }
  }
}
