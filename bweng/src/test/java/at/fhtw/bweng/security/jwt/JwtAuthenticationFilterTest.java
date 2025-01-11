package at.fhtw.bweng.security.jwt;

import at.fhtw.bweng.security.UserPrincipal;
import at.fhtw.bweng.security.jwt.UserPrincipalAuthenticationToken;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    @Mock
    private JwtVerifier jwtVerifier;

    @Mock
    private JwtToPrincipalConverter jwtToPrincipalConverter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtVerifier, jwtToPrincipalConverter);
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_withValidJwt_setsAuthentication() throws Exception {
        // Arrange
        String token = "valid.jwt.token";
        DecodedJWT decodedJWT = mock(DecodedJWT.class);
        UserPrincipal userPrincipal = new UserPrincipal(UUID.randomUUID(), "testUser", "password", "USER");

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtVerifier.verify(token)).thenReturn(decodedJWT);
        when(jwtToPrincipalConverter.convert(decodedJWT)).thenReturn(userPrincipal);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).isEqualTo(userPrincipal);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_withoutAuthorizationHeader_doesNotSetAuthentication() throws Exception {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_withMalformedBearerToken_doesNotSetAuthentication() throws Exception {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("InvalidTokenFormat");

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }
}