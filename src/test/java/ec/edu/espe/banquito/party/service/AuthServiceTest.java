package ec.edu.espe.banquito.party.service;

import ec.edu.espe.banquito.party.dto.AuthLoginRequestDTO;
import ec.edu.espe.banquito.party.dto.AuthLoginResponseDTO;
import ec.edu.espe.banquito.party.dto.ChangePasswordRequestDTO;
import ec.edu.espe.banquito.party.enums.CustomerStatusEnum;
import ec.edu.espe.banquito.party.enums.CustomerTypeEnum;
import ec.edu.espe.banquito.party.exception.InvalidCredentialsException;
import ec.edu.espe.banquito.party.model.Customer;
import ec.edu.espe.banquito.party.model.WebCredential;
import ec.edu.espe.banquito.party.repository.CustomerRepository;
import ec.edu.espe.banquito.party.repository.WebCredentialRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private WebCredentialRepository webCredentialRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AuthService authService;

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    @Test
    void login_debeRetornarMustChangePasswordTrue_cuandoEsPrimerLogin() {
        Customer customer = buildCustomer(1, "0987654321", "Ana", "Herrera");
        WebCredential credential = buildCredential("0987654321", "pass123", CustomerStatusEnum.ACTIVO, null, customer);

        when(webCredentialRepository.findByUsername("0987654321")).thenReturn(Optional.of(credential));

        AuthLoginResponseDTO result = authService.login(buildLoginRequest("0987654321", "pass123"));

        assertThat(result.isMustChangePassword()).isTrue();
        verify(webCredentialRepository, never()).save(any());
    }

    @Test
    void login_debeActualizarLastLogin_cuandoNoEsPrimerLogin() {
        Customer customer = buildCustomer(1, "0987654321", "Ana", "Herrera");
        WebCredential credential = buildCredential("0987654321", "pass123", CustomerStatusEnum.ACTIVO,
                LocalDateTime.now().minusDays(1), customer);

        when(webCredentialRepository.findByUsername("0987654321")).thenReturn(Optional.of(credential));
        when(webCredentialRepository.save(any())).thenReturn(credential);

        AuthLoginResponseDTO result = authService.login(buildLoginRequest("0987654321", "pass123"));

        assertThat(result.isMustChangePassword()).isFalse();
        verify(webCredentialRepository).save(credential);
    }

    @Test
    void login_debeLanzarException_cuandoCredencialEstaInactiva() {
        Customer customer = buildCustomer(1, "0987654321", "Ana", "Herrera");
        WebCredential credential = buildCredential("0987654321", "pass123", CustomerStatusEnum.INACTIVO, null, customer);

        when(webCredentialRepository.findByUsername("0987654321")).thenReturn(Optional.of(credential));

        AuthLoginRequestDTO request = buildLoginRequest("0987654321", "pass123");

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void login_debeLanzarException_cuandoPasswordEsIncorrecta() {
        Customer customer = buildCustomer(1, "0987654321", "Ana", "Herrera");
        WebCredential credential = buildCredential("0987654321", "pass123", CustomerStatusEnum.ACTIVO, null, customer);

        when(webCredentialRepository.findByUsername("0987654321")).thenReturn(Optional.of(credential));

        AuthLoginRequestDTO request = buildLoginRequest("0987654321", "wrong");

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void login_debeUsarLegalName_cuandoClienteEsJuridico() {
        Customer customer = buildCustomer(1, "1790000001001", null, null);
        customer.setCustomerType(CustomerTypeEnum.JURIDICO);
        customer.setLegalName("Empresa XYZ S.A.");
        WebCredential credential = buildCredential("1790000001001", "pass123", CustomerStatusEnum.ACTIVO,
                LocalDateTime.now().minusDays(1), customer);

        when(webCredentialRepository.findByUsername("1790000001001")).thenReturn(Optional.of(credential));
        when(webCredentialRepository.save(any())).thenReturn(credential);

        AuthLoginResponseDTO result = authService.login(buildLoginRequest("1790000001001", "pass123"));

        assertThat(result.getFullName()).isEqualTo("Empresa XYZ S.A.");
    }

    @Test
    void changePassword_debeActualizarPassword_cuandoPasswordActualEsCorrecta() {
        Customer customer = buildCustomer(1, "0987654321", "Ana", "Herrera");
        WebCredential credential = buildCredential("0987654321", "oldPass", CustomerStatusEnum.ACTIVO, null, customer);

        when(webCredentialRepository.findByUsername("0987654321")).thenReturn(Optional.of(credential));
        when(webCredentialRepository.save(any())).thenReturn(credential);

        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO();
        request.setUsername("0987654321");
        request.setCurrentPassword("oldPass");
        request.setNewPassword("newPass123");

        authService.changePassword(request);

        ArgumentCaptor<WebCredential> captor = ArgumentCaptor.forClass(WebCredential.class);
        verify(webCredentialRepository).save(captor.capture());
        assertThat(ENCODER.matches("newPass123", captor.getValue().getPasswordHash())).isTrue();
        assertThat(captor.getValue().getLastLogin()).isNotNull();
    }

    @Test
    void changePassword_debeLanzarException_cuandoPasswordActualEsIncorrecta() {
        Customer customer = buildCustomer(1, "0987654321", "Ana", "Herrera");
        WebCredential credential = buildCredential("0987654321", "correctPass", CustomerStatusEnum.ACTIVO, null, customer);

        when(webCredentialRepository.findByUsername("0987654321")).thenReturn(Optional.of(credential));

        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO();
        request.setUsername("0987654321");
        request.setCurrentPassword("wrongPass");
        request.setNewPassword("newPass123");

        assertThatThrownBy(() -> authService.changePassword(request))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void changePassword_debeLanzarException_cuandoUsuarioNoExiste() {
        when(webCredentialRepository.findByUsername("noexiste")).thenReturn(Optional.empty());

        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO();
        request.setUsername("noexiste");
        request.setCurrentPassword("pass");
        request.setNewPassword("newpass");

        assertThatThrownBy(() -> authService.changePassword(request))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    private Customer buildCustomer(Integer id, String identification, String firstName, String lastName) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setIdentification(identification);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setCustomerType(CustomerTypeEnum.NATURAL);
        customer.setStatus(CustomerStatusEnum.ACTIVO);
        return customer;
    }

    private WebCredential buildCredential(String username, String rawPassword,
                                          CustomerStatusEnum status, LocalDateTime lastLogin,
                                          Customer customer) {
        WebCredential credential = new WebCredential();
        credential.setUsername(username);
        credential.setPasswordHash(ENCODER.encode(rawPassword));
        credential.setStatus(status);
        credential.setLastLogin(lastLogin);
        credential.setCustomer(customer);
        return credential;
    }

    private AuthLoginRequestDTO buildLoginRequest(String username, String password) {
        AuthLoginRequestDTO request = new AuthLoginRequestDTO();
        request.setUsername(username);
        request.setPassword(password);
        return request;
    }
}