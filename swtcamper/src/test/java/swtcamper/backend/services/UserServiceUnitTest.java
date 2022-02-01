package swtcamper.backend.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

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
    when(userRepository.save(any())).thenReturn(testUser);
    verify(userRepository, times(1)).save(userArgumentCaptor.capture());
    User capturedUser = userArgumentCaptor.getValue();

    assertEquals(testUser.getId(), capturedUser.getId());
  }

  @Test
  public void deleteShouldLog() {
    userRepository.delete(any());
    verify(userRepository, times(1)).delete(any());
  }

  @Test
  public void updateUserShouldReturnUser() {
    User user = new User();
    when(userServiceUnderTest.update(any(User.class))).thenReturn(user);
  }

  @Test
  public void isThereAnyDisabledUserShouldReturnBoolean() {}

  @Test
  public void getUserByIdShouldReturnUserOrThrow() {}

  @Test
  public void getUserByUsernameShouldReturnUserOrThrow() {}

  @Test
  public void getUsersShouldReturnListOfUsersOrThrow() {}

  @Test
  public void getLoggedInUserShouldReturnUserOrNull() {}

  @Test
  public void excludeRenterForCurrentlyLoggedInUserShouldRevokeRepositoryOnce() {}

  @Test
  public void removeExcludedRenterForCurrentlyLoggedInUserShouldRevokeRepositoryOnce() {}

  @Test
  public void loginShouldReturnListOfUsersOrThrow() {}

  @Test
  public void isUsernameFreeShouldReturnBoolean() {}

  @Test
  public void isEmailFreeShouldReturnBoolean() {}

//  lock
//  unlock
//  enable
//  isEnabled
//  promoteUser
//  degradeUser
//  resetPassword
//  countUser
}
