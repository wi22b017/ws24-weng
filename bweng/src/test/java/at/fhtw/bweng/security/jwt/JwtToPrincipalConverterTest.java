package at.fhtw.bweng.security.jwt;

import at.fhtw.bweng.security.UserPrincipal;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class JwtToPrincipalConverterTest {

    @Mock
    private DecodedJWT decodedJWT;

    @Mock
    private Claim usernameClaim;

    @Mock
    private Claim roleClaim;

    private JwtToPrincipalConverter jwtToPrincipalConverter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtToPrincipalConverter = new JwtToPrincipalConverter();
    }

    @Test
    void convert_decodesJwtToUserPrincipal() {
        // Arrange
        UUID userId = UUID.randomUUID();
        String username = "testuser";
        String role = "USER";

        when(decodedJWT.getSubject()).thenReturn(userId.toString());
        when(decodedJWT.getClaim("username")).thenReturn(usernameClaim);
        when(decodedJWT.getClaim("role")).thenReturn(roleClaim);
        when(usernameClaim.asString()).thenReturn(username);
        when(roleClaim.asString()).thenReturn(role);

        // Act
        UserPrincipal result = jwtToPrincipalConverter.convert(decodedJWT);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getUsername()).isEqualTo(username);
        assertThat(result.getRole()).isEqualTo(role);
        assertThat(result.getPassword()).isEmpty();
    }
}
