package swtcamper.backend.services;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import swtcamper.api.controller.LoggingController;
import swtcamper.backend.entities.LoggingLevel;
import swtcamper.backend.entities.LoggingMessage;
import swtcamper.backend.entities.User;
import swtcamper.backend.entities.UserRole;
import swtcamper.backend.repositories.LoggingRepository;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoggingServiceIntegrationTest {

  @Mock
  @Autowired
  private LoggingRepository loggingRepository;

  @Autowired
  private LoggingService loggingServiceUnterTest;

  @Autowired
  private LoggingController loggingControllerUnterTest;

  public User getExampleUser() {
    User user = new User();
    user.setUsername("Musti");
    user.setPassword("password");
    user.setEmail("m.mustermann@t-online.de");
    user.setPhone("01769308213");
    user.setName("Max");
    user.setSurname("Mustermann");
    user.setUserRole(UserRole.PROVIDER);
    user.setEnabled(false);

    return user;
  }

  @Test
  public void databaseShouldContainCorrectLogMessages() {
    // INFO Log
    loggingControllerUnterTest.log(
      new LoggingMessage(LoggingLevel.INFO, "Das ist ein Test von Musti")
    );

    assertEquals(1, loggingServiceUnterTest.getAllLogMessages().size());
    assertNotNull(loggingServiceUnterTest.getAllLogMessages().get(0).getTime());
    assertEquals(
      LoggingLevel.INFO,
      loggingServiceUnterTest.getAllLogMessages().get(0).getLogLevel()
    );
    assertEquals(
      "Das ist ein Test von Musti",
      loggingServiceUnterTest.getAllLogMessages().get(0).getLoggingMessage()
    );

    // WARNING Log
    loggingControllerUnterTest.log(
      new LoggingMessage(
        LoggingLevel.WARNING,
        "Das ist der zweite Test von Musti"
      )
    );

    assertEquals(2, loggingServiceUnterTest.getAllLogMessages().size());
    assertNotNull(loggingServiceUnterTest.getAllLogMessages().get(1).getTime());
    assertEquals(
      LoggingLevel.WARNING,
      loggingServiceUnterTest.getAllLogMessages().get(1).getLogLevel()
    );
    assertEquals(
      "Das ist der zweite Test von Musti",
      loggingServiceUnterTest.getAllLogMessages().get(1).getLoggingMessage()
    );

    // ERROR Log
    loggingControllerUnterTest.log(
      new LoggingMessage(LoggingLevel.ERROR, "Obacht, ein Misuse von Anna!")
    );

    assertEquals(3, loggingServiceUnterTest.getAllLogMessages().size());
    assertNotNull(loggingServiceUnterTest.getAllLogMessages().get(2).getTime());
    assertEquals(
      LoggingLevel.ERROR,
      loggingServiceUnterTest.getAllLogMessages().get(2).getLogLevel()
    );
    assertEquals(
      "Obacht, ein Misuse von Anna!",
      loggingServiceUnterTest.getAllLogMessages().get(2).getLoggingMessage()
    );
  }

  @Test
  public void getLogforSpecificUserShouldBeFoundCorrectly() {
    List<LoggingMessage> logList;

    // first Log from 'Musti'
    loggingControllerUnterTest.log(
      new LoggingMessage(LoggingLevel.INFO, "Das ist ein Test von Musti")
    );
    logList = loggingServiceUnterTest.getLogForUser(getExampleUser());

    assertEquals(1, logList.size());

    // second Log from 'Musti'
    loggingControllerUnterTest.log(
      new LoggingMessage(
        LoggingLevel.WARNING,
        "Das ist der zweite Test von Musti"
      )
    );
    logList = loggingServiceUnterTest.getLogForUser(getExampleUser());

    assertEquals(2, logList.size());

    // first log from 'Anna'
    loggingControllerUnterTest.log(
      new LoggingMessage(LoggingLevel.ERROR, "Obacht, ein Misuse von Anna!")
    );
    logList = loggingServiceUnterTest.getLogForUser(getExampleUser());

    assertEquals(2, logList.size());
  }
}
