package swtcamper.backend.services;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import swtcamper.backend.entities.User;
import swtcamper.backend.entities.UserRole;
import swtcamper.backend.repositories.UserRepository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userServiceUnderTest;

    @BeforeEach
    public void saveTestUser() {
        when(userServiceUnderTest.create("ThomasK96",
                "password",
                "t.kretschmann@t-online.de",
                "1829309182308213",
                "Thomas",
                "Kretschmann",
                UserRole.PROVIDER,
                false)).thenReturn(any());
    }

    @Test
    public void whenSaveValidUserItShouldReturnUser() {
        // when
        User testUser = userServiceUnderTest.create("ThomasK96",
                "password",
                "t.kretschmann@t-online.de",
                "1829309182308213",
                "Thomas",
                "Kretschmann",
                UserRole.PROVIDER,
                false);

        // then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(
                User.class
        );
        verify(userRepository, times(1)).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();

        assertEquals(testUser, capturedUser);
    }
}
