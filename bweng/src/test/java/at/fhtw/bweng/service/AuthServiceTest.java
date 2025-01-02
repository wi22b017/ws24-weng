package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.TokenRequestDto;
import at.fhtw.bweng.dto.TokenResponseDto;
import at.fhtw.bweng.security.UserPrincipal;
import at.fhtw.bweng.security.jwt.TokenIssuer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private TokenIssuer tokenIssuer;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private AuthService authService;

    private static final UUID USER_ID = UUID.randomUUID();
    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "12345678A#Gs-6";
    private static final String ROLE = "ROLE_USER";
    private static final String TOKEN = "mocked-jwt-token";

    @Test
    void authenticate_returnsTokenResponseDto() {
        // arrange
        TokenRequestDto tokenRequestDto = new TokenRequestDto(USERNAME, PASSWORD);
        UserPrincipal userPrincipal = new UserPrincipal(USER_ID, USERNAME, PASSWORD, ROLE);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(tokenIssuer.issue(USER_ID, USERNAME, ROLE)).thenReturn(TOKEN);

        SecurityContextHolder.setContext(securityContext);

        // act
        TokenResponseDto response = authService.authenticate(tokenRequestDto);

        // assert
        assertThat(response.getToken()).isEqualTo(TOKEN);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenIssuer, times(1)).issue(USER_ID, USERNAME, ROLE);
    }

}
