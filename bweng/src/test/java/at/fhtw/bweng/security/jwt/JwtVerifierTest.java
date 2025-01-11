package at.fhtw.bweng.security.jwt;

import at.fhtw.bweng.property.JwtProperties;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtVerifierTest {

    private JwtVerifier jwtVerifier;
    private JwtProperties jwtProperties;

    @BeforeEach
    void setUp() {
        jwtProperties = mock(JwtProperties.class);
        jwtVerifier = new JwtVerifier(jwtProperties);
    }

    @Test
    void verify_withValidToken_returnsDecodedJWT() {
        // Arrange
        String secret = "test-secret";
        String token = JWT.create()
                .withSubject("test-user")
                .sign(Algorithm.HMAC256(secret));

        when(jwtProperties.getSecret()).thenReturn(secret);

        // Act
        DecodedJWT decodedJWT = jwtVerifier.verify(token);

        // Assert
        assertThat(decodedJWT).isNotNull();
        assertThat(decodedJWT.getSubject()).isEqualTo("test-user");
    }

    @Test
    void verify_withInvalidToken_throwsJWTVerificationException() {
        // Arrange
        String secret = "test-secret";
        String invalidToken = "invalid.jwt.token";

        when(jwtProperties.getSecret()).thenReturn(secret);

        // Act & Assert
        assertThatThrownBy(() -> jwtVerifier.verify(invalidToken))
                .isInstanceOf(JWTVerificationException.class);
    }

    @Test
    void verify_withDifferentSecret_throwsJWTVerificationException() {
        // Arrange
        String correctSecret = "correct-secret";
        String differentSecret = "wrong-secret";
        String token = JWT.create()
                .withSubject("test-user")
                .sign(Algorithm.HMAC256(correctSecret));

        when(jwtProperties.getSecret()).thenReturn(differentSecret);

        // Act & Assert
        assertThatThrownBy(() -> jwtVerifier.verify(token))
                .isInstanceOf(JWTVerificationException.class);
    }
}