package ec.edu.espe.banquito.party.service;

import ec.edu.espe.banquito.party.dto.AuthLoginRequestDTO;
import ec.edu.espe.banquito.party.dto.AuthLoginResponseDTO;
import ec.edu.espe.banquito.party.exception.InvalidCredentialsException;
import ec.edu.espe.banquito.party.model.Customer;
import ec.edu.espe.banquito.party.model.WebCredential;
import ec.edu.espe.banquito.party.repository.WebCredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final WebCredentialRepository webCredentialRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthLoginResponseDTO login(AuthLoginRequestDTO request) {
        WebCredential credential = this.webCredentialRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Usuario o contraseña incorrectos"));

        if (!"ACTIVO".equalsIgnoreCase(credential.getStatus().getValue())) {
            throw new InvalidCredentialsException("La credencial del usuario no está activa");
        }

        boolean passwordMatches = this.passwordEncoder.matches(
                request.getPassword(),
                credential.getPasswordHash()
        );

        if (!passwordMatches) {
            throw new InvalidCredentialsException("Usuario o contraseña incorrectos");
        }

        Customer customer = credential.getCustomer();

        String fullName;
        if (customer.getCustomerType() != null && "JURIDICO".equals(customer.getCustomerType().getValue())) {
            fullName = customer.getLegalName();
        } else {
            fullName = ((customer.getFirstName() != null ? customer.getFirstName() : "") + " " +
                    (customer.getLastName() != null ? customer.getLastName() : "")).trim();
        }

        return new AuthLoginResponseDTO(
                customer.getId(),
                credential.getUsername(),
                fullName,
                customer.getCustomerType(),
                customer.getStatus(),
                credential.getStatus().getValue()
        );
    }
}