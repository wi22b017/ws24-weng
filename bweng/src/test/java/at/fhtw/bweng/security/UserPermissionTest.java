package at.fhtw.bweng.security;

import at.fhtw.bweng.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserPermissionTest {

    private UserPermission userPermission;

    @Mock
    private Authentication authentication;

    private UserPrincipal userPrincipal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userPermission = new UserPermission();
    }

    @Test
    void supports_returnsTrueForUserClassName() {
        // Act
        boolean result = userPermission.supports(authentication, User.class.getName());

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    void supports_returnsFalseForOtherClassNames() {
        // Act
        boolean result = userPermission.supports(authentication, "OtherClass");

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    void hasPermission_returnsTrueForAdminRole() {
        // Arrange
        UUID resourceId = UUID.randomUUID();
        userPrincipal = new UserPrincipal(UUID.randomUUID(), "adminUser", "password", "ADMIN");
        when(authentication.getPrincipal()).thenReturn(userPrincipal);

        // Act
        boolean result = userPermission.hasPermission(authentication, resourceId);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    void hasPermission_returnsTrueForMatchingResourceId() {
        // Arrange
        UUID resourceId = UUID.randomUUID();
        userPrincipal = new UserPrincipal(resourceId, "regularUser", "password", "USER");
        when(authentication.getPrincipal()).thenReturn(userPrincipal);

        // Act
        boolean result = userPermission.hasPermission(authentication, resourceId);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    void hasPermission_returnsFalseForNonMatchingResourceId() {
        // Arrange
        UUID resourceId = UUID.randomUUID();
        UUID anotherResourceId = UUID.randomUUID();
        userPrincipal = new UserPrincipal(anotherResourceId, "regularUser", "password", "USER");
        when(authentication.getPrincipal()).thenReturn(userPrincipal);

        // Act
        boolean result = userPermission.hasPermission(authentication, resourceId);

        // Assert
        assertThat(result).isFalse();
    }
}