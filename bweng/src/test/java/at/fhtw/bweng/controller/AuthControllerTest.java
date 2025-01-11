package at.fhtw.bweng.controller;

import at.fhtw.bweng.dto.TokenRequestDto;
import at.fhtw.bweng.dto.TokenResponseDto;
import at.fhtw.bweng.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void token_returnsTokenResponseDto() {
        // Arrange
        TokenRequestDto tokenRequestDto = new TokenRequestDto("username", "password");
        TokenResponseDto expectedResponse = new TokenResponseDto("mockedToken123");
        when(authService.authenticate(tokenRequestDto)).thenReturn(expectedResponse);

        // Act
        TokenResponseDto actualResponse = authController.token(tokenRequestDto);

        // Assert
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getToken()).isEqualTo("mockedToken123");
        verify(authService, times(1)).authenticate(tokenRequestDto);
    }
}