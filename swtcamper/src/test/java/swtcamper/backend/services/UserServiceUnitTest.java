package swtcamper.backend.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import swtcamper.backend.entities.User;
import swtcamper.backend.entities.UserRole;
import swtcamper.backend.repositories.UserRepository;
import swtcamper.backend.services.exceptions.GenericServiceException;
import swtcamper.backend.services.exceptions.UserDoesNotExistException;
import swtcamper.backend.services.exceptions.WrongPasswordException;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceUnitTest {

  @Spy
  UserRepository userRepository;

  @Mock
  UserService userServiceUnderTest;

  private User testUser = new User(
    "ThomasK96",
    "password",
    "t.kretschmann@t-online.de",
    "1829309182308213",
    "Thomas",
    "Kretschmann",
    UserRole.PROVIDER,
    false
  );

  @BeforeEach
  public void saveTestUser() {
    verify(userServiceUnderTest).create(testUser);
  }

  @Test
  public void whenSaveValidUserItShouldReturnUser() {
    // then
    ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(
      User.class
    );
    verify(userRepository).save(userArgumentCaptor.capture());
    User capturedUser = userArgumentCaptor.getValue();
    assertThat(capturedUser).isNotNull().isEqualTo(testUser);
  }

  @Test
  public void userCountShouldBeOne() throws GenericServiceException {
//    assertThat(userServiceUnderTest.countUser()).isEqualTo(1);
    assertThat(userServiceUnderTest.user().size()).isEqualTo(1);
  }

  @Test
  public void listOfAllUsersShouldContainUser() throws GenericServiceException {
    assertThat(userServiceUnderTest.user()).contains(testUser);
  }

  @Test
  public void loginWithRegisteredCredentialsShouldReturnCorrectUserRole()
    throws UserDoesNotExistException, WrongPasswordException {
    assertThat(userServiceUnderTest.login("ThomasK96", "password"))
      .isEqualTo(UserRole.PROVIDER);
  }

  @Test
  public void loginWithWrongCredentialsShouldThrowWrongPasswordException()
    throws UserDoesNotExistException, WrongPasswordException {
    when(userServiceUnderTest.login("ThomasK96", "passwort"))
      .thenThrow(WrongPasswordException.class);
  }

  @Test
  public void usedUserNameShouldNotBeFree() {
    assertThat(userServiceUnderTest.isUsernameFree("ThomasK96")).isFalse();
  }

  @Test
  public void unusedUserNameShouldBeFree() {
    assertThat(userServiceUnderTest.isUsernameFree("paulmitp")).isFalse();
  }

  @Test
  public void usedEmailShouldNotBeFree() {
    assertThat(userServiceUnderTest.isEmailFree("t.kretschmann@t-online.de"))
      .isFalse();
  }

  @Test
  public void unusedEmailShouldBeFree() {
    assertThat(userServiceUnderTest.isEmailFree("paulmitp@gmail.com"))
      .isFalse();
  }

  @Test
  public void savedUserShouldBeNotEnabled() throws UserDoesNotExistException {
    assertThat(userServiceUnderTest.isEnabled("ThomasK96")).isFalse();
  }

  @Test
  public void savedUserShouldBeEnabledAfterEnabling()
    throws UserDoesNotExistException {
    // when
    userServiceUnderTest.create(testUser);

    // then
    ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(
      User.class
    );
    verify(userRepository).save(userArgumentCaptor.capture());
    User capturedUser = userArgumentCaptor.getValue();

    verify(userServiceUnderTest).enable(capturedUser.getId());

    assertThat(userServiceUnderTest.isEnabled("ThomasK96")).isTrue();
  }

  @Test
  public void resetPasswordShouldApply()
    throws GenericServiceException, UserDoesNotExistException, WrongPasswordException {
    verify(userServiceUnderTest).resetPassword(
      "ThomasK96",
      "t.kretschmann@t-online.de",
      "passworT"
    );
    assertThat(userServiceUnderTest.login("ThomasK96", "passworT"))
      .isEqualTo(UserRole.PROVIDER);
  }
}
