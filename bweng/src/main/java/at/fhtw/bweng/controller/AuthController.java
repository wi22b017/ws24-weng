package at.fhtw.bweng.controller;

import at.fhtw.bweng.dto.TokenRequestDto;
import at.fhtw.bweng.dto.TokenResponseDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    //private final AuthService authService;

    public AuthController(/*AuthService authService*/) {
        //this.authService = authService;
    }

    /*@PostMapping("/token")
    public TokenResponseDto token(@RequestBody @Valid final TokenRequestDto tokenRequestDto) {
        return authService.authenticate(tokenRequestDto);
    }*/

    @PostMapping("/token")
    public TokenResponseDto token(@RequestBody @Valid final TokenRequestDto tokenRequestDto) {
        return new TokenResponseDto("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJmYWI3ZDI1MC1jMTIwLTQyNjQtYmQyYy1jNmEzNTZkZDdhMmQiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.Aq1F-v_bSi70unXheBtLIbm8YEm23oU2Xgo4GL-cnzM");
    }
}
