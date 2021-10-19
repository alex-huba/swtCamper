package xtasks.backend.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import xtasks.backend.services.exceptions.GenericServiceException;

@RunWith(MockitoJUnitRunner.class)
public class TaskServiceUnitTest {

  @Spy
  private TaskService taskService;

  @Test(expected = GenericServiceException.class)
  public void createShouldFailIfTitleIsNull() throws GenericServiceException {
    taskService.create(null, "description");
  }

  @Test(expected = GenericServiceException.class)
  public void createShouldFailIfDescriptionIsNull()
    throws GenericServiceException {
    taskService.create("title", null);
  }

  @Test(expected = GenericServiceException.class)
  public void createShouldFailIfTitleIsEmpty() throws GenericServiceException {
    taskService.create("", "description");
  }

  @Test(expected = GenericServiceException.class)
  public void createShouldFailIfTitleContainsOnlyBlanks()
    throws GenericServiceException {
    taskService.create("   ", "description");
  }
}
