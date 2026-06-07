package ec.edu.espe.banquito.party.controller;

import ec.edu.espe.banquito.party.dto.AuthLoginRequestDTO;
import ec.edu.espe.banquito.party.dto.AuthLoginResponseDTO;
import ec.edu.espe.banquito.party.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthLoginResponseDTO login(@RequestBody AuthLoginRequestDTO request) {
        return this.authService.login(request);
    }
}