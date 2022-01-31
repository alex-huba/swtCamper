package swtcamper.backend.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import swtcamper.backend.entities.User;
import swtcamper.backend.entities.UserRole;
import swtcamper.backend.services.exceptions.GenericServiceException;
import swtcamper.backend.services.exceptions.UserDoesNotExistException;
import swtcamper.backend.services.exceptions.WrongPasswordException;

import java.util.stream.Collectors;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userServiceUnderTest;

    public User saveTestUser() {
        return userServiceUnderTest.create("Musti",
                "password",
                "m.mustermann@t-online.de",
                "01769308213",
                "Max",
                "Mustermann",
                UserRole.PROVIDER,
                false);
    }

    @Test
    public void creatingNewUserShouldReturnThisUser() {
        assertEquals("Musti", saveTestUser().getUsername());
        assertEquals("Max", saveTestUser().getName());
        assertEquals("Mustermann", saveTestUser().getSurname());
        assertEquals("m.mustermann@t-online.de", saveTestUser().getEmail());
    }

    @Test
    public void userShouldNotExistAnymoreAfterDeleting() {
        User testUser = saveTestUser();
        userServiceUnderTest.delete(testUser.getId());
        assertThrows(GenericServiceException.class, () -> userServiceUnderTest.getUserById(testUser.getId()));
    }

    @Test
    public void userShouldBeUpdatedAfterUpdating() throws GenericServiceException {
        User testUser = saveTestUser();

        testUser.setUsername("Mustus");

        userServiceUnderTest.update(testUser);
        assertEquals(testUser, userServiceUnderTest.getUserById(testUser.getId()));
    }

    @Test
    public void databaseShouldBeEmptyBeforeSavingAnyUser() {
        assertEquals(0, userServiceUnderTest.countUser());
    }

    @Test
    public void databaseShouldThrowIfRequestingListOfUsers() {
        assertThrows(GenericServiceException.class, () -> userServiceUnderTest.user());
    }

    @Test
    public void databaseShouldCountOneAfterInsertingOneUser() {
        saveTestUser();

        assertEquals(1, userServiceUnderTest.countUser());
    }

    @Test
    public void loggedInUserShouldBeSetAfterLogin() throws UserDoesNotExistException, WrongPasswordException {
        saveTestUser();
        userServiceUnderTest.login("Musti", "password");
        assertEquals("Musti", userServiceUnderTest.getLoggedInUser().getUsername());
    }

    @Test
    public void listOfAllUsersShouldContainUser() throws GenericServiceException {
        saveTestUser();

        assertEquals("Musti", userServiceUnderTest.user().get(0).getUsername());
        assertEquals("Max", userServiceUnderTest.user().get(0).getName());
        assertEquals("Mustermann", userServiceUnderTest.user().get(0).getSurname());
        assertEquals("m.mustermann@t-online.de", userServiceUnderTest.user().get(0).getEmail());
    }

    @Test
    public void loginWithRegisteredCredentialsShouldReturnCorrectUserRole()
            throws UserDoesNotExistException, WrongPasswordException {
        saveTestUser();

        assertEquals(UserRole.PROVIDER, userServiceUnderTest.login("Musti", "password"));
    }

    @Test
    public void loginWithWrongUsernameShouldThrowUserDoesNotExistException() {
        saveTestUser();

        assertThrows(UserDoesNotExistException.class, () -> userServiceUnderTest.login("Mustus", "password"));
    }

    @Test
    public void loginWithWrongPasswordShouldThrowWrongPasswordException() {
        saveTestUser();

        assertThrows(WrongPasswordException.class, () -> userServiceUnderTest.login("Musti", "12345"));
    }

    @Test
    public void lockingUserShouldSetHimLocked() throws GenericServiceException {
        User testUser = saveTestUser();
        userServiceUnderTest.lock(testUser.getId(), "Operator");
        assertTrue(userServiceUnderTest.user().stream().filter(user -> user.getId().equals(testUser.getId())).collect(Collectors.toList()).get(0).isLocked());
    }

    @Test
    public void unlockingLockedUserShouldSetHimUnlocked() throws GenericServiceException {
        User testUser = saveTestUser();
        userServiceUnderTest.lock(testUser.getId(), "Operator");
        userServiceUnderTest.unlock(testUser.getId(), "Operator");
        assertFalse(userServiceUnderTest.user().stream().filter(user -> user.getId().equals(testUser.getId())).collect(Collectors.toList()).get(0).isLocked());
    }

    @Test
    public void usedUserNameShouldNotBeFree() {
        saveTestUser();

        assertFalse(userServiceUnderTest.isUsernameFree("Musti"));
    }

    @Test
    public void unusedUserNameShouldBeFree() {
        saveTestUser();

        assertTrue(userServiceUnderTest.isUsernameFree("PaulmitP"));
    }

    @Test
    public void usedEmailShouldNotBeFree() {
        saveTestUser();

        assertFalse(userServiceUnderTest.isEmailFree("m.mustermann@t-online.de"));
    }

    @Test
    public void unusedEmailShouldBeFree() {
        saveTestUser();

        assertTrue(userServiceUnderTest.isEmailFree("paulmitp@gmail.com"));
    }

    @Test
    public void savedUserShouldBeNotEnabled() throws UserDoesNotExistException {
        saveTestUser();

        assertFalse(userServiceUnderTest.isEnabled("Musti"));
    }

    @Test
    public void requestingEnabledStatusForUnknownUserShouldThrowUserDoesNotExistException() {
        saveTestUser();

        assertThrows(UserDoesNotExistException.class, () -> userServiceUnderTest.isEnabled("Mustus"));
    }

    @Test
    public void savedUserShouldBeEnabledAfterEnabling()
            throws UserDoesNotExistException, GenericServiceException {
        saveTestUser();

        userServiceUnderTest.enable(userServiceUnderTest.user().get(0).getId(), "Operator");

        assertTrue(userServiceUnderTest.isEnabled("Musti"));
    }

    @Test
    public void resetPasswordShouldApply()
            throws GenericServiceException, UserDoesNotExistException, WrongPasswordException {
        saveTestUser();

        userServiceUnderTest.resetPassword(
                "Musti",
                "m.mustermann@t-online.de",
                "passworT"
        );
        assertEquals(UserRole.PROVIDER, userServiceUnderTest.login("Musti", "passworT"));
    }

    @Test
    public void resetPasswordWithWrongCredentialsShouldThrowGenericServiceException() {
        saveTestUser();

        assertThrows(GenericServiceException.class, () -> userServiceUnderTest.resetPassword(
                "Mustus",
                "m.mustermann@t-online.de",
                "passworT"
        ));
    }
}
