package at.fhtw.bweng.security.jwt;

import at.fhtw.bweng.security.UserPrincipal;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserPrincipalAuthenticationTokenTest {

    @Test
    void constructor_initializesWithPrincipalAndSetsAuthenticated() {
        // Arrange
        UUID userId = UUID.randomUUID();
        String username = "test-user";
        String role = "USER";
        UserPrincipal principal = new UserPrincipal(userId, username, "password", role);

        // Act
        UserPrincipalAuthenticationToken token = new UserPrincipalAuthenticationToken(principal);

        // Assert
        assertThat(token.getPrincipal()).isEqualTo(principal);
        assertThat(token.isAuthenticated()).isTrue();
        assertThat(token.getAuthorities()).containsExactly(new SimpleGrantedAuthority(role));
    }

    @Test
    void getCredentials_returnsNull() {
        // Arrange
        UUID userId = UUID.randomUUID();
        String username = "test-user";
        String role = "USER";
        UserPrincipal principal = new UserPrincipal(userId, username, "password", role);

        UserPrincipalAuthenticationToken token = new UserPrincipalAuthenticationToken(principal);

        // Act
        Object credentials = token.getCredentials();

        // Assert
        assertThat(credentials).isNull();
    }

    @Test
    void getPrincipal_returnsUserPrincipal() {
        // Arrange
        UUID userId = UUID.randomUUID();
        String username = "test-user";
        String role = "USER";
        UserPrincipal principal = new UserPrincipal(userId, username, "password", role);

        UserPrincipalAuthenticationToken token = new UserPrincipalAuthenticationToken(principal);

        // Act
        Object actualPrincipal = token.getPrincipal();

        // Assert
        assertThat(actualPrincipal).isEqualTo(principal);
    }
}