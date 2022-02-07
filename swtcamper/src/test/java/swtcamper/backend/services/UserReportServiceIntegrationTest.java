package swtcamper.backend.services;

import static org.junit.Assert.*;

import java.util.stream.Collectors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import swtcamper.backend.entities.User;
import swtcamper.backend.entities.UserReport;
import swtcamper.backend.entities.UserRole;
import swtcamper.backend.repositories.UserReportRepository;
import swtcamper.backend.services.exceptions.GenericServiceException;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserReportServiceIntegrationTest {

  @Autowired
  private UserReportRepository userReportRepository;

  @Autowired
  private UserReportService userReportServiceUnderTest;

  @Autowired
  private UserService userServiceUnderTest;

  private UserReport createUserReport() {
    User operator = userServiceUnderTest.create(
      "Operator",
      "password",
      "op.erator@t-online.de",
      "017673987433",
      "Op",
      "Erator",
      UserRole.OPERATOR,
      true
    );
    userServiceUnderTest.setLoggedInUser(operator);

    User reporter = userServiceUnderTest.create(
      "Max",
      "password",
      "m.mustermann@t-online.de",
      "0176473654333",
      "Max",
      "Mustermann",
      UserRole.PROVIDER,
      true
    );
    User reportee = userServiceUnderTest.create(
      "Anna",
      "password",
      "a.mustermann@t-online.de",
      "0176439874390",
      "Anna",
      "Mustermann",
      UserRole.PROVIDER,
      true
    );

    return userReportServiceUnderTest.create(
      new UserReport(reporter, reportee, "Das ist ein Test")
    );
  }

  @Test
  public void createUserReportShouldReturnActiveUserReport() {
    UserReport userReport = createUserReport();

    assertTrue(userReport.isActive());
    assertEquals("Max", userReport.getReporter().getUsername());
    assertEquals("Anna", userReport.getReportee().getUsername());
    assertEquals("Das ist ein Test", userReport.getReportReason());
  }

  @Test
  public void getUserReportByIdShouldReturnOrThrow()
    throws GenericServiceException {
    UserReport userReport = createUserReport();

    assertNotNull(
      userReportServiceUnderTest.getUserReportById(userReport.getId())
    );
    assertThrows(
      GenericServiceException.class,
      () -> userReportServiceUnderTest.getUserReportById(99L)
    );
  }

  @Test
  public void getAllUserReportsShouldReturnCorrectList() {
    UserReport userReport = createUserReport();

    userReportServiceUnderTest.create(
      new UserReport(
        userReport.getReportee(),
        userReport.getReporter(),
        "Das ist noch ein Test"
      )
    );

    assertEquals(2, userReportServiceUnderTest.getAllUserReports().size());
    assertEquals(
      "Max",
      userReportServiceUnderTest
        .getAllUserReports()
        .stream()
        .filter(userReportI ->
          userReportI.getReportReason().equals("Das ist noch ein Test")
        )
        .collect(Collectors.toList())
        .get(0)
        .getReportee()
        .getUsername()
    );
  }

  @Test
  public void acceptUserReportShouldBlockUserAndReturnInactiveUserReport()
    throws GenericServiceException {
    UserReport userReport = createUserReport();
    // does not block reportee...
    UserReport updatedUserReport = userReportServiceUnderTest.acceptUserReport(
      userReport.getId()
    );

    assertFalse(updatedUserReport.isActive());
    assertTrue(userServiceUnderTest.getUserByUsername("Anna").isLocked());
  }

  @Test
  public void rejectUserReportShouldReturnInactiveUserReport()
    throws GenericServiceException {
    UserReport userReport = createUserReport();
    UserReport updatedUserReport = userReportServiceUnderTest.rejectUserReport(
      userReport.getId()
    );

    assertFalse(updatedUserReport.isActive());
    assertFalse(userServiceUnderTest.getUserByUsername("Anna").isLocked());
  }

  @Test
  public void afterDeleteUserReportShouldNotBeAvailableAnymore() {
    UserReport userReport = createUserReport();

    assertEquals(1, userReportServiceUnderTest.getAllUserReports().size());

    userReportServiceUnderTest.delete(userReport.getId());

    assertTrue(userReportServiceUnderTest.getAllUserReports().isEmpty());
  }
}
