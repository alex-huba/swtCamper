package swtcamper.backend.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import swtcamper.backend.entities.User;
import swtcamper.backend.entities.UserRole;
import swtcamper.backend.repositories.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceUnitTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userServiceUnderTest;

  @Test
  public void whenSaveValidUserItShouldReturnUser() {
    // when
    User testUser = userServiceUnderTest.create(
      "Musti",
      "password",
      "m.mustermann@t-online.de",
      "01764893743456",
      "Max",
      "Mustermann",
      UserRole.PROVIDER,
      false
    );

    // then
    ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(
      User.class
    );
    verify(userRepository, times(1)).save(userArgumentCaptor.capture());
    User capturedUser = userArgumentCaptor.getValue();

    assertEquals(testUser, capturedUser);
  }
}
