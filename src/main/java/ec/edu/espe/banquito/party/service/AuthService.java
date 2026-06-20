package ec.edu.espe.banquito.party.service;

import ec.edu.espe.banquito.party.dto.AuthLoginRequestDTO;
import ec.edu.espe.banquito.party.dto.AuthLoginResponseDTO;
import ec.edu.espe.banquito.party.dto.ChangePasswordRequestDTO;
import ec.edu.espe.banquito.party.enums.CustomerStatusEnum;
import ec.edu.espe.banquito.party.exception.InvalidCredentialsException;
import ec.edu.espe.banquito.party.model.Customer;
import ec.edu.espe.banquito.party.model.WebCredential;
import ec.edu.espe.banquito.party.repository.CustomerRepository;
import ec.edu.espe.banquito.party.repository.WebCredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final WebCredentialRepository webCredentialRepository;
    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public AuthLoginResponseDTO login(AuthLoginRequestDTO request) {
        WebCredential credential = webCredentialRepository.findByUsername(request.getUsername())
                .orElseGet(() -> createInitialCredential(request.getUsername()));

        if (!"ACTIVO".equalsIgnoreCase(credential.getStatus().getValue())) {
            throw new InvalidCredentialsException("La credencial del usuario no está activa");
        }

        if (!passwordEncoder.matches(request.getPassword(), credential.getPasswordHash())) {
            throw new InvalidCredentialsException("Usuario o contraseña incorrectos");
        }

        // lastLogin == null indica primer ingreso → debe cambiar contraseña
        boolean mustChangePassword = (credential.getLastLogin() == null);

        if (!mustChangePassword) {
            credential.setLastLogin(LocalDateTime.now());
            webCredentialRepository.save(credential);
        }

        Customer customer = credential.getCustomer();

        return new AuthLoginResponseDTO(
                customer.getId(),
                credential.getUsername(),
                buildFullName(customer),
                customer.getCustomerType(),
                customer.getStatus(),
                credential.getStatus().getValue(),
                mustChangePassword
        );
    }

    @Transactional
    public void changePassword(ChangePasswordRequestDTO request) {
        WebCredential credential = webCredentialRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), credential.getPasswordHash())) {
            throw new InvalidCredentialsException("Contraseña actual incorrecta");
        }

        credential.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        credential.setLastLogin(LocalDateTime.now());
        webCredentialRepository.save(credential);
    }

    // Creación lazy: primera vez que el cliente intenta ingresar.
    // La contraseña inicial es la cédula/RUC del cliente.
    private WebCredential createInitialCredential(String identification) {
        Customer customer = customerRepository.findByIdentification(identification)
                .orElseThrow(() -> new InvalidCredentialsException("Usuario o contraseña incorrectos"));

        if (customer.getStatus() == null || !"ACTIVO".equals(customer.getStatus().getValue())) {
            throw new InvalidCredentialsException("Cliente inactivo");
        }

        WebCredential credential = new WebCredential();
        credential.setCustomer(customer);
        credential.setUsername(customer.getIdentification());
        credential.setPasswordHash(passwordEncoder.encode(customer.getIdentification()));
        credential.setStatus(CustomerStatusEnum.ACTIVO);
        // lastLogin queda null → mustChangePassword = true en el primer login
        return webCredentialRepository.save(credential);
    }

    private String buildFullName(Customer customer) {
        if (customer.getCustomerType() != null && "JURIDICO".equals(customer.getCustomerType().getValue())) {
            return customer.getLegalName();
        }
        return ((customer.getFirstName() != null ? customer.getFirstName() : "") + " " +
                (customer.getLastName() != null ? customer.getLastName() : "")).trim();
    }
}
