package at.fhtw.bweng.security;

import at.fhtw.bweng.model.User;
import at.fhtw.bweng.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class CustomUserDetailServiceTest {

    @Mock
    private UserService userService;

    private CustomUserDetailService customUserDetailService;

    private User activeUser;
    private User inactiveUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customUserDetailService = new CustomUserDetailService(userService);

        // Initialize reusable User objects
        activeUser = new User(
                UUID.randomUUID(),
                "Male",
                "John",
                "Doe",
                "validUser",
                "password",
                "john.doe@example.com",
                LocalDate.of(1990, 1, 1),
                "ROLE_USER",
                "ACTIVE",
                null, // Address
                null  // PaymentMethod
        );

        inactiveUser = new User(
                UUID.randomUUID(),
                "Female",
                "Jane",
                "Doe",
                "inactiveUser",
                "password",
                "jane.doe@example.com",
                LocalDate.of(1992, 2, 2),
                "ROLE_USER",
                "INACTIVE",
                null, // Address
                null  // PaymentMethod
        );
    }

    @Test
    void loadUserByUsername_withValidUsername_returnsUserDetails() {
        // Arrange
        when(userService.getUserByUsername(activeUser.getUsername())).thenReturn(activeUser);

        // Act
        UserPrincipal userPrincipal = (UserPrincipal) customUserDetailService.loadUserByUsername(activeUser.getUsername());

        // Assert
        assertThat(userPrincipal.getUsername()).isEqualTo(activeUser.getUsername());
        assertThat(userPrincipal.getPassword()).isEqualTo(activeUser.getPassword());
        assertThat(userPrincipal.getRole()).isEqualTo(activeUser.getRole());
        verify(userService).getUserByUsername(activeUser.getUsername());
    }

    @Test
    void loadUserByUsername_withInactiveUser_throwsIllegalStateException() {
        // Arrange
        when(userService.getUserByUsername(inactiveUser.getUsername())).thenReturn(inactiveUser);

        // Act & Assert
        assertThatThrownBy(() -> customUserDetailService.loadUserByUsername(inactiveUser.getUsername()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("User account is not active");
        verify(userService).getUserByUsername(inactiveUser.getUsername());
    }

    @Test
    void loadUserByUsername_withValidEmail_returnsUserDetails() {
        // Arrange
        when(userService.getUserByUsername(activeUser.getEmail())).thenThrow(NoSuchElementException.class);
        when(userService.getUserByEmail(activeUser.getEmail())).thenReturn(activeUser);

        // Act
        UserPrincipal userPrincipal = (UserPrincipal) customUserDetailService.loadUserByUsername(activeUser.getEmail());

        // Assert
        assertThat(userPrincipal.getUsername()).isEqualTo(activeUser.getUsername());
        assertThat(userPrincipal.getPassword()).isEqualTo(activeUser.getPassword());
        assertThat(userPrincipal.getRole()).isEqualTo(activeUser.getRole());
        verify(userService).getUserByUsername(activeUser.getEmail());
        verify(userService).getUserByEmail(activeUser.getEmail());
    }

    @Test
    void loadUserByUsername_withInactiveEmail_throwsIllegalStateException() {
        // Arrange
        when(userService.getUserByUsername(inactiveUser.getEmail())).thenThrow(NoSuchElementException.class);
        when(userService.getUserByEmail(inactiveUser.getEmail())).thenReturn(inactiveUser);

        // Act & Assert
        assertThatThrownBy(() -> customUserDetailService.loadUserByUsername(inactiveUser.getEmail()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("User account is not active");
        verify(userService).getUserByUsername(inactiveUser.getEmail());
        verify(userService).getUserByEmail(inactiveUser.getEmail());
    }

    @Test
    void loadUserByUsername_withNonExistentUsernameOrEmail_throwsUsernameNotFoundException() {
        // Arrange
        String input = "nonexistentUserOrEmail";
        when(userService.getUserByUsername(input)).thenThrow(NoSuchElementException.class);
        when(userService.getUserByEmail(input)).thenThrow(NoSuchElementException.class);

        // Act & Assert
        assertThatThrownBy(() -> customUserDetailService.loadUserByUsername(input))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("User not found with username or email: " + input);
        verify(userService).getUserByUsername(input);
        verify(userService).getUserByEmail(input);
    }
}