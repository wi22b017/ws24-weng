package at.fhtw.bweng.security.jwt;


import at.fhtw.bweng.property.JwtProperties;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class JwtIssuerTest {

    @Mock
    private JwtProperties jwtProperties;

    private JwtIssuer jwtIssuer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtIssuer = new JwtIssuer(jwtProperties);
    }

    @Test
    void issue_createsJwtTokenWithCorrectClaims() {
        // Arrange
        UUID userId = UUID.randomUUID();
        String username = "testuser";
        String role = "USER";
        String secret = "testsecret";
        Instant beforeIssueTime = Instant.now();

        when(jwtProperties.getSecret()).thenReturn(secret);

        // Act
        String token = jwtIssuer.issue(userId, username, role);
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secret)).build().verify(token);

        // Assert
        assertThat(decodedJWT.getSubject()).isEqualTo(userId.toString());
        assertThat(decodedJWT.getClaim("username").asString()).isEqualTo(username);
        assertThat(decodedJWT.getClaim("role").asString()).isEqualTo(role);
        assertThat(decodedJWT.getExpiresAt().toInstant()).isAfter(beforeIssueTime);
        assertThat(decodedJWT.getExpiresAt().toInstant()).isBefore(Instant.now().plus(Duration.of(1, ChronoUnit.DAYS)));
    }
}