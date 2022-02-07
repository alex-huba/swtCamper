package swtcamper.backend.services;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import swtcamper.backend.entities.User;
import swtcamper.backend.entities.UserRole;
import swtcamper.backend.repositories.UserRepository;
import swtcamper.backend.services.exceptions.GenericServiceException;
import swtcamper.backend.services.exceptions.UserDoesNotExistException;
import swtcamper.backend.services.exceptions.WrongPasswordException;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserServiceIntegrationTest {

  @Mock
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserService userServiceUnderTest;

  public User saveCheckUser() {
    return userServiceUnderTest.create(
      "Musti",
      "password",
      "m.mustermann@t-online.de",
      "01769308213",
      "Max",
      "Mustermann",
      UserRole.PROVIDER,
      false
    );
  }

  @Test
  public void savingUserShouldReturnUser() {
    User checkUser = saveCheckUser();

    assertNotNull(checkUser.getId());
    assertEquals("Musti", checkUser.getUsername());
    assertEquals("Max", checkUser.getName());
    assertEquals("Mustermann", checkUser.getSurname());
    assertEquals("m.mustermann@t-online.de", checkUser.getEmail());
  }

  @Test
  public void deletedUserShouldNotExistAnymore()
    throws GenericServiceException {
    saveCheckUser();

    userServiceUnderTest.delete(
      userServiceUnderTest.getUserByUsername("Musti").getId()
    );

    assertThrows(
      GenericServiceException.class,
      () -> userServiceUnderTest.getUserByUsername("Musti")
    );
  }

  @Test
  public void updatedUserShouldBeUpdated() {
    User checkUser = saveCheckUser();
    checkUser.setUsername("UPDATED");

    assertEquals(
      "UPDATED",
      userServiceUnderTest.update(checkUser).getUsername()
    );
  }

  @Test
  public void isThereAnyDisabledUserShouldReturnTrue()
    throws GenericServiceException {
    saveCheckUser();

    assertTrue(userServiceUnderTest.isThereAnyDisabledUser());
  }

  @Test
  public void isThereAnyDisabledUserShouldReturnFalseAfterEnabling()
    throws GenericServiceException {
    User checkUser = saveCheckUser();
    userServiceUnderTest.enable(checkUser.getId(), "Operator");

    assertFalse(userServiceUnderTest.isThereAnyDisabledUser());
  }

  @Test
  public void getUserByIdShouldWorkForExistingUser()
    throws GenericServiceException {
    User checkUser = saveCheckUser();
    assertNotNull(userServiceUnderTest.getUserById(checkUser.getId()));
  }

  @Test
  public void getUserByIdShouldThrowForUnknownUser()
    throws GenericServiceException {
    assertThrows(
      GenericServiceException.class,
      () -> userServiceUnderTest.getUserById(5L)
    );
  }

  @Test
  public void getUserByUsernameShouldWorkForExistingUser()
    throws GenericServiceException {
    User checkUser = saveCheckUser();
    assertNotNull(
      userServiceUnderTest.getUserByUsername(checkUser.getUsername())
    );
  }

  @Test
  public void getUserByUsernameShouldThrowForUnknownUser()
    throws GenericServiceException {
    assertThrows(
      GenericServiceException.class,
      () -> userServiceUnderTest.getUserByUsername("Hans")
    );
  }

  @Test
  public void getUsersSizeShouldBeOneAfterCreatedUser()
    throws GenericServiceException {
    saveCheckUser();
    assertEquals(1, userServiceUnderTest.user().size());
  }

  @Test
  public void getUsersShouldThrowIfThereIsNoUser() {
    assertThrows(
      GenericServiceException.class,
      () -> userServiceUnderTest.user()
    );
  }

  @Test
  public void loggedInUserShouldReturnNullIfNoOneLoggedIn() {
    assertNull(userServiceUnderTest.getLoggedInUser());
  }

  @Test
  public void loggedInUserShouldReturnCorrectUserAfterLogin()
    throws UserDoesNotExistException, WrongPasswordException {
    User checkUser = saveCheckUser();
    userServiceUnderTest.login("Musti", "password");
    assertNotNull(userServiceUnderTest.getLoggedInUser());
    assertEquals(
      checkUser.getId(),
      userServiceUnderTest.getLoggedInUser().getId()
    );
  }

  @Test
  public void loggedInUserShouldReturnNullAfterLogout()
    throws UserDoesNotExistException, WrongPasswordException {
    User checkUser = saveCheckUser();
    userServiceUnderTest.login("Musti", "password");
    assertNotNull(userServiceUnderTest.getLoggedInUser());
    assertEquals(
      checkUser.getId(),
      userServiceUnderTest.getLoggedInUser().getId()
    );

    userServiceUnderTest.logout();
    assertNull(userServiceUnderTest.getLoggedInUser());
  }

  @Test
  public void exludingUsersShouldMakeThemAppearInExcludedRentersList()
    throws UserDoesNotExistException, WrongPasswordException, GenericServiceException {
    User user1 = saveCheckUser();
    User user2 = userServiceUnderTest.create(
      "Annaiii",
      "password",
      "a.mustermann@t-online.de",
      "017693034534",
      "Anna",
      "Mustermann",
      UserRole.PROVIDER,
      false
    );

    userServiceUnderTest.login("Musti", "password");
    userServiceUnderTest.excludeRenterForCurrentlyLoggedInUser(user2.getId());

    assertTrue(
      userServiceUnderTest
        .getLoggedInUser()
        .getExcludedRenters()
        .contains(user2.getId())
    );
  }

  @Test
  public void removingAnExludedUsersShouldMakeThemNotAppearInExcludedRentersListAnymore()
    throws UserDoesNotExistException, WrongPasswordException, GenericServiceException {
    User user1 = saveCheckUser();
    User user2 = userServiceUnderTest.create(
      "Annaiii",
      "password",
      "a.mustermann@t-online.de",
      "017693034534",
      "Anna",
      "Mustermann",
      UserRole.PROVIDER,
      false
    );

    userServiceUnderTest.login("Musti", "password");
    userServiceUnderTest.excludeRenterForCurrentlyLoggedInUser(user2.getId());
    assertTrue(
      userServiceUnderTest
        .getLoggedInUser()
        .getExcludedRenters()
        .contains(user2.getId())
    );

    userServiceUnderTest.removeExcludedRenterForCurrentlyLoggedInUser(
      user2.getId()
    );
    assertFalse(
      userServiceUnderTest
        .getLoggedInUser()
        .getExcludedRenters()
        .contains(user2.getId())
    );
  }

  @Test
  public void loggingInWithWrongUsernameShouldThrowUserDoesNotExistException() {
    User checkUser = saveCheckUser();
    assertThrows(
      UserDoesNotExistException.class,
      () -> userServiceUnderTest.login("Anna", "password")
    );
  }

  @Test
  public void loggingInWithWrongPasswordShouldThrowWrongPasswordException() {
    User checkUser = saveCheckUser();
    assertThrows(
      WrongPasswordException.class,
      () -> userServiceUnderTest.login("Musti", "wortpass")
    );
  }

  @Test
  public void isUserNameFreeShouldReturnFalseForUsedUsername() {
    User checkUser = saveCheckUser();
    assertFalse(userServiceUnderTest.isUsernameFree("Musti"));
  }

  @Test
  public void isUserNameFreeShouldReturnTrueForFreeUsername() {
    User checkUser = saveCheckUser();
    assertTrue(userServiceUnderTest.isUsernameFree("Anna"));
  }

  @Test
  public void isEmailFreeShouldReturnFalseForUsedEmail() {
    User checkUser = saveCheckUser();
    assertFalse(userServiceUnderTest.isEmailFree("m.mustermann@t-online.de"));
  }

  @Test
  public void isEmailFreeShouldReturnTrueForFreeEmail() {
    User checkUser = saveCheckUser();
    assertTrue(userServiceUnderTest.isEmailFree("a.mustermann@t-online.de"));
  }

  @Test
  public void userShouldBeLockedAfterLockingHim()
    throws GenericServiceException {
    User checkUser = saveCheckUser();
    userServiceUnderTest.lock(checkUser.getId(), "Operator");
    assertTrue(
      userServiceUnderTest.getUserByUsername(checkUser.getUsername()).isLocked()
    );
  }

  @Test
  public void userShouldBeUnlockedAfterUnlockingHim()
    throws GenericServiceException {
    User checkUser = saveCheckUser();
    userServiceUnderTest.lock(checkUser.getId(), "Operator");
    assertTrue(
      userServiceUnderTest.getUserByUsername(checkUser.getUsername()).isLocked()
    );

    userServiceUnderTest.unlock(checkUser.getId(), "Operator");
    assertFalse(
      userServiceUnderTest.getUserByUsername(checkUser.getUsername()).isLocked()
    );
  }

  @Test
  public void userShouldBeEnabledAfterEnablingHim()
    throws UserDoesNotExistException {
    User checkUser = saveCheckUser();
    userServiceUnderTest.enable(checkUser.getId(), "Operator");
    assertTrue(userServiceUnderTest.isEnabled(checkUser.getUsername()));
  }

  @Test
  public void gettingEnabledStatusShouldThrowForUnknownUser()
    throws UserDoesNotExistException {
    User checkUser = saveCheckUser();
    userServiceUnderTest.enable(checkUser.getId(), "Operator");
    assertThrows(
      UserDoesNotExistException.class,
      () -> userServiceUnderTest.isEnabled("Anna")
    );
  }

  @Test
  public void promotedRenterShouldBeProvider() throws GenericServiceException {
    User checkUser = saveCheckUser();
    checkUser.setUserRole(UserRole.RENTER);
    userServiceUnderTest.update(checkUser);
    assertEquals(
      UserRole.RENTER,
      userServiceUnderTest.getUserById(checkUser.getId()).getUserRole()
    );

    userServiceUnderTest.promoteUser(checkUser.getId(), "Operator");
    assertEquals(
      UserRole.PROVIDER,
      userServiceUnderTest.getUserById(checkUser.getId()).getUserRole()
    );
  }

  @Test
  public void promotedProviderShouldBeOperator()
    throws GenericServiceException {
    User checkUser = saveCheckUser();
    checkUser.setUserRole(UserRole.PROVIDER);
    userServiceUnderTest.update(checkUser);
    assertEquals(
      UserRole.PROVIDER,
      userServiceUnderTest.getUserById(checkUser.getId()).getUserRole()
    );

    userServiceUnderTest.promoteUser(checkUser.getId(), "Operator");
    assertEquals(
      UserRole.OPERATOR,
      userServiceUnderTest.getUserById(checkUser.getId()).getUserRole()
    );
  }

  @Test
  public void degradedProviderShouldBeRenter() throws GenericServiceException {
    User checkUser = saveCheckUser();
    checkUser.setUserRole(UserRole.PROVIDER);
    userServiceUnderTest.update(checkUser);
    assertEquals(
      UserRole.PROVIDER,
      userServiceUnderTest.getUserById(checkUser.getId()).getUserRole()
    );

    userServiceUnderTest.degradeUser(checkUser.getId(), "Operator");
    assertEquals(
      UserRole.RENTER,
      userServiceUnderTest.getUserById(checkUser.getId()).getUserRole()
    );
  }

  @Test
  public void degradedOperatorShouldBeProvider()
    throws GenericServiceException {
    User checkUser = saveCheckUser();
    checkUser.setUserRole(UserRole.OPERATOR);
    userServiceUnderTest.update(checkUser);
    assertEquals(
      UserRole.OPERATOR,
      userServiceUnderTest.getUserById(checkUser.getId()).getUserRole()
    );

    userServiceUnderTest.degradeUser(checkUser.getId(), "Operator");
    assertEquals(
      UserRole.PROVIDER,
      userServiceUnderTest.getUserById(checkUser.getId()).getUserRole()
    );
  }

  @Test
  public void resetPasswordShouldWorkForCorrectCredentials()
    throws GenericServiceException, UserDoesNotExistException, WrongPasswordException {
    User checkUser = saveCheckUser();
    userServiceUnderTest.resetPassword(
      checkUser.getUsername(),
      checkUser.getEmail(),
      "NEUES-Password"
    );

    assertNotNull(userServiceUnderTest.login("Musti", "NEUES-Password"));
  }

  @Test
  public void resetPasswordShouldThrowForIncorrectUsername() {
    User checkUser = saveCheckUser();
    assertThrows(
      GenericServiceException.class,
      () ->
        userServiceUnderTest.resetPassword(
          "Anna",
          checkUser.getEmail(),
          "NEUES-Password"
        )
    );
  }

  @Test
  public void resetPasswordShouldThrowForIncorrectEmail() {
    User checkUser = saveCheckUser();
    assertThrows(
      GenericServiceException.class,
      () ->
        userServiceUnderTest.resetPassword(
          checkUser.getUsername(),
          "a.mustermann@t-online.de",
          "NEUES-Password"
        )
    );
  }

  @Test
  public void countUserShouldBeZeroIfNoUserWasAdded() {
    assertEquals(0L, userServiceUnderTest.countUser());
  }

  @Test
  public void countUserShouldBeZOneAfterFirstRegistration() {
    User checkUser = saveCheckUser();
    assertEquals(1L, userServiceUnderTest.countUser());
  }
}
