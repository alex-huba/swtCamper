package swtcamper.backend.services;

import static org.mockito.Mockito.*;

import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import swtcamper.backend.entities.User;
import swtcamper.backend.entities.UserRole;
import swtcamper.backend.repositories.UserRepository;
import swtcamper.backend.services.exceptions.GenericServiceException;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceUnitTest {

  @Mock
  private UserRepository userRepository;

  @Spy
  @InjectMocks
  private UserService userServiceUnderTest;

  @Test
  public void whenSaveValidUserItShouldReturnUser() {
    User testUser = new User();
    testUser.setUsername("Musti");
    testUser.setPassword("password");
    testUser.setEmail("m.mustermann@t-online.de");
    testUser.setPhone("01764893743456");
    testUser.setName("Max");
    testUser.setSurname("Mustermann");
    testUser.setUserRole(UserRole.PROVIDER);
    testUser.setEnabled(false);

    Mockito.when(userRepository.save(any())).thenReturn(testUser);
    Mockito
      .when(userRepository.findByUsername(any()))
      .thenReturn(Optional.of(testUser));

    ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(
      User.class
    );

    userServiceUnderTest.create(
      "Musti",
      "password",
      "m.mustermann@t-online.de",
      "01764893743456",
      "Max",
      "Mustermann",
      UserRole.PROVIDER,
      false
    );

    verify(userRepository, times(1)).save(userArgumentCaptor.capture());
  }

  @Test
  public void deleteShouldInvokeRepositoryOnce() {
    userRepository.delete(any());
    verify(userRepository, times(1)).delete(any());
  }

  @Test
  public void updateUserShouldReturnUser() {
    User user = new User();
    when(userServiceUnderTest.update(any(User.class))).thenReturn(user);
  }

  @Test
  public void isThereAnyDisabledUserShouldReturnBoolean()
    throws GenericServiceException {
    userServiceUnderTest.create(
      "Musti",
      "password",
      "m.mustermann@t-online.de",
      "01764893743456",
      "Max",
      "Mustermann",
      UserRole.PROVIDER,
      false
    );

    userServiceUnderTest.isThereAnyDisabledUser();
    verify(userRepository, times(1)).findAll();
  }

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
