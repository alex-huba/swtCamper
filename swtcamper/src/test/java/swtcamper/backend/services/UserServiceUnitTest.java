package swtcamper.backend.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import swtcamper.backend.entities.User;
import swtcamper.backend.entities.UserRole;
import swtcamper.backend.repositories.UserRepository;
import swtcamper.backend.services.exceptions.GenericServiceException;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceUnitTest {
    @Spy
    @InjectMocks
    private UserService userServiceUnderTest;

    @Spy
    private UserRepository userRepository;

    @Test
    public void createProjectWorksForValidInputs()
            throws GenericServiceException {
        // When
        userServiceUnderTest.create(
                "ThomasK96",
                "password",
                "t.kretschmann@t-online.de",
                "1829309182308213",
                "Thomas",
                "Kretschmann",
                UserRole.PROVIDER,
                false
        );

        // Then
        verify(userRepository, times(1)).save(any());
        verifyNoMoreInteractions(userRepository);
    }
}
