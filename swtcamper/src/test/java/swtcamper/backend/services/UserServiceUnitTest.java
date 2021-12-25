package swtcamper.backend.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import swtcamper.backend.entities.User;
import swtcamper.backend.entities.UserRole;
import swtcamper.backend.repositories.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceUnitTest {
    @Spy
    @InjectMocks
    private UserService userServiceUnderTest;

    @Spy
    private UserRepository userRepository;

    @Test
    public void firstUserThatLogsInWillBeOperator() {
        // given
        User normalUser = new User();
        normalUser.setUsername("Nicici");
        normalUser.setPassword("nici123");
        normalUser.setEmail("nici.mueller@gmail.com");
        normalUser.setPhone("0174123123");
        normalUser.setName("Nici");
        normalUser.setSurname("Müller");
        normalUser.setUserRole(UserRole.OPERATOR);
        normalUser.setEnabled(false);

        // when
        when(userRepository.save(normalUser)).thenReturn(normalUser);

        userServiceUnderTest.create("Nicici", "nici123", "nici.mueller@gmail.com", "0174123123", "Nici", "Müller", UserRole.PROVIDER, false);

        // then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(
                User.class
        );

        verify(userRepository).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser).isNotNull().isEqualTo(normalUser);
    }
}
