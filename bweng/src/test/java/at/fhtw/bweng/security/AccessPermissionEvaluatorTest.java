package at.fhtw.bweng.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AccessPermissionEvaluatorTest {

    @Mock
    private AccessPermission mockAccessPermission;

    @Mock
    private Authentication mockAuthentication;

    private AccessPermissionEvaluator evaluator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        evaluator = new AccessPermissionEvaluator(Collections.singletonList(mockAccessPermission));
    }

    @Test
    void hasPermission_withUnsupportedPermission_returnsFalse() {
        // Arrange
        String targetType = "SomeClass";
        UUID targetId = UUID.randomUUID();

        when(mockAccessPermission.supports(mockAuthentication, targetType)).thenReturn(false);

        // Act
        boolean result = evaluator.hasPermission(mockAuthentication, targetId, targetType, null);

        // Assert
        assertThat(result).isFalse();
        verify(mockAccessPermission).supports(mockAuthentication, targetType);
        verify(mockAccessPermission, never()).hasPermission(any(), any());
    }

    @Test
    void hasPermission_withSupportedPermissionButNoAccess_returnsFalse() {
        // Arrange
        String targetType = "SomeClass";
        UUID targetId = UUID.randomUUID();

        when(mockAccessPermission.supports(mockAuthentication, targetType)).thenReturn(true);
        when(mockAccessPermission.hasPermission(mockAuthentication, targetId)).thenReturn(false);

        // Act
        boolean result = evaluator.hasPermission(mockAuthentication, targetId, targetType, null);

        // Assert
        assertThat(result).isFalse();
        verify(mockAccessPermission).supports(mockAuthentication, targetType);
        verify(mockAccessPermission).hasPermission(mockAuthentication, targetId);
    }

    @Test
    void hasPermission_withSupportedPermissionAndAccess_returnsTrue() {
        // Arrange
        String targetType = "SomeClass";
        UUID targetId = UUID.randomUUID();

        when(mockAccessPermission.supports(mockAuthentication, targetType)).thenReturn(true);
        when(mockAccessPermission.hasPermission(mockAuthentication, targetId)).thenReturn(true);

        // Act
        boolean result = evaluator.hasPermission(mockAuthentication, targetId, targetType, null);

        // Assert
        assertThat(result).isTrue();
        verify(mockAccessPermission).supports(mockAuthentication, targetType);
        verify(mockAccessPermission).hasPermission(mockAuthentication, targetId);
    }

    @Test
    void hasPermission_withUnsupportedTargetDomainObject_returnsFalse() {
        // Act
        boolean result = evaluator.hasPermission(mockAuthentication, new Object(), null);

        // Assert
        assertThat(result).isFalse();
    }
}