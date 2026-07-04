package ec.edu.espe.banquito.core.party.controller;

import ec.edu.espe.banquito.core.party.dto.AuthLoginRequestDTO;
import ec.edu.espe.banquito.core.party.dto.AuthLoginResponseDTO;
import ec.edu.espe.banquito.core.party.dto.ChangePasswordRequestDTO;
import ec.edu.espe.banquito.core.party.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequestDTO request) {
        this.authService.changePassword(request);
        return ResponseEntity.ok().build();
    }
}