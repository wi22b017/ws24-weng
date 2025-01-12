package at.fhtw.bweng.controller;

import at.fhtw.bweng.dto.TokenRequestDto;
import at.fhtw.bweng.dto.TokenResponseDto;
import at.fhtw.bweng.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/token")
    public TokenResponseDto token(@RequestBody @Valid final TokenRequestDto tokenRequestDto) {
        return authService.authenticate(tokenRequestDto);
    }

}
