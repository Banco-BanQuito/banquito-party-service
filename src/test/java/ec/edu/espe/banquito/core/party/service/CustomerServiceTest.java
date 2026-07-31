package ec.edu.espe.banquito.core.party.service;

import ec.edu.espe.banquito.core.party.client.AccountLookupGrpcClient;
import ec.edu.espe.banquito.core.party.dto.CustomerByAccountResponseDTO;
import ec.edu.espe.banquito.core.party.dto.CustomerRequestDTO;
import ec.edu.espe.banquito.core.party.dto.CustomerResponseDTO;
import ec.edu.espe.banquito.core.party.enums.CustomerStatusEnum;
import ec.edu.espe.banquito.core.party.enums.CustomerTypeEnum;
import ec.edu.espe.banquito.core.party.exception.CustomerNotFoundException;
import ec.edu.espe.banquito.core.party.grpc.accountlookup.AccountLookupResponse;
import ec.edu.espe.banquito.core.party.model.Customer;
import ec.edu.espe.banquito.core.party.model.CustomerSubtype;
import ec.edu.espe.banquito.core.party.repository.CustomerRepository;
import ec.edu.espe.banquito.core.party.repository.CustomerSubtypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerSubtypeRepository customerSubtypeRepository;

    @Mock
    private AccountLookupGrpcClient accountLookupGrpcClient;

    @Mock
    private IdentityPlatformService identityPlatformService;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void findByIdOrIdentification_debeBuscarPorId_cuandoValorEsNumericoCorto() {
        Customer customer = buildCustomer(42, "0987654321", "Ana", "Herrera", null);
        when(customerRepository.findById(42)).thenReturn(Optional.of(customer));

        CustomerResponseDTO result = customerService.findByIdOrIdentification("42");

        assertThat(result.getId()).isEqualTo(42);
        assertThat(result.getIdentification()).isEqualTo("0987654321");
    }

    @Test
    void findByIdOrIdentification_debeBuscarPorIdentificacion_cuandoValorEsLargo() {
        Customer customer = buildCustomer(1, "0987654321", "Ana", "Herrera", null);
        when(customerRepository.findByIdentification("0987654321")).thenReturn(Optional.of(customer));

        CustomerResponseDTO result = customerService.findByIdOrIdentification("0987654321");

        assertThat(result.getIdentification()).isEqualTo("0987654321");
    }

    @Test
    void findByIdOrIdentification_debeLanzarException_cuandoClienteNoEncontradoPorId() {
        when(customerRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.findByIdOrIdentification("99"))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void findByIdOrIdentification_debeLanzarException_cuandoClienteNoEncontradoPorIdentificacion() {
        when(customerRepository.findByIdentification("1234567890")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.findByIdOrIdentification("1234567890"))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessageContaining("1234567890");
    }

    @Test
    void findByIdOrIdentification_debeUsarLegalName_cuandoEstaPresente() {
        Customer customer = buildCustomer(1, "1792000001001", null, null, "Empresa ABC S.A.");
        customer.setCustomerType(CustomerTypeEnum.JURIDICO);
        when(customerRepository.findByIdentification("1792000001001")).thenReturn(Optional.of(customer));

        CustomerResponseDTO result = customerService.findByIdOrIdentification("1792000001001");

        assertThat(result.getFullName()).isEqualTo("Empresa ABC S.A.");
    }

    @Test
    void findByIdOrIdentification_debeConcatenarNombres_cuandoNoHayLegalName() {
        Customer customer = buildCustomer(1, "0987654321", "Ana", "Herrera", null);
        when(customerRepository.findByIdentification("0987654321")).thenReturn(Optional.of(customer));

        CustomerResponseDTO result = customerService.findByIdOrIdentification("0987654321");

        assertThat(result.getFullName()).isEqualTo("Ana Herrera");
    }

    @Test
    void findByIdOrIdentification_debeRetornarNombreVacio_cuandoNombresNulos() {
        Customer customer = buildCustomer(1, "0987654321", null, null, null);
        when(customerRepository.findByIdentification("0987654321")).thenReturn(Optional.of(customer));

        CustomerResponseDTO result = customerService.findByIdOrIdentification("0987654321");

        assertThat(result.getFullName()).isEmpty();
    }

    @Test
    void create_debeGuardarClienteYCrearCuentaDeAcceso_cuandoDatosValidos() {
        CustomerRequestDTO request = new CustomerRequestDTO();
        request.setCustomerType("NATURAL");
        request.setCustomerSubtypeId(1);
        request.setIdentificationType("CEDULA");
        request.setIdentification("0987654321");
        request.setFirstName("Ana");
        request.setLastName("Herrera");

        when(customerRepository.findByIdentification("0987654321")).thenReturn(Optional.empty());
        when(customerSubtypeRepository.findById(1)).thenReturn(Optional.of(new CustomerSubtype(1)));
        when(customerRepository.save(any(Customer.class))).thenAnswer(inv -> inv.getArgument(0));

        CustomerResponseDTO result = customerService.create(request);

        assertThat(result.getIdentification()).isEqualTo("0987654321");
        org.mockito.Mockito.verify(identityPlatformService).createAccount("0987654321", "Ana Herrera");
    }

    @Test
    void create_debeLanzarExcepcion_cuandoIdentificacionYaExiste() {
        CustomerRequestDTO request = new CustomerRequestDTO();
        request.setIdentification("0987654321");
        when(customerRepository.findByIdentification("0987654321"))
                .thenReturn(Optional.of(buildCustomer(1, "0987654321", "Ana", "Herrera", null)));

        assertThatThrownBy(() -> customerService.create(request))
                .isInstanceOf(IllegalArgumentException.class);

        org.mockito.Mockito.verifyNoInteractions(identityPlatformService);
    }

    @Test
    void updateStatus_debeActualizarEstado_cuandoClienteExiste() {
        Customer customer = buildCustomer(5, "0987654321", "Ana", "Herrera", null);
        when(customerRepository.findById(5)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenAnswer(inv -> inv.getArgument(0));

        CustomerResponseDTO result = customerService.updateStatus("5", "BLOQUEADO");

        assertThat(result.getStatus()).isEqualTo(CustomerStatusEnum.BLOQUEADO);
    }

    @Test
    void create_debeLanzarExcepcion_cuandoSubtipoNoExiste() {
        CustomerRequestDTO request = new CustomerRequestDTO();
        request.setIdentification("0987654321");
        request.setCustomerSubtypeId(99);
        when(customerRepository.findByIdentification("0987654321")).thenReturn(Optional.empty());
        when(customerSubtypeRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.create(request))
                .isInstanceOf(IllegalArgumentException.class);

        org.mockito.Mockito.verifyNoInteractions(identityPlatformService);
    }

    @Test
    void create_debeGuardarClienteJuridico_cuandoRepresentanteLegalExiste() {
        CustomerRequestDTO request = new CustomerRequestDTO();
        request.setCustomerType("JURIDICO");
        request.setCustomerSubtypeId(2);
        request.setIdentification("1792000001001");
        request.setLegalName("Empresa ABC S.A.");
        request.setLegalRepresentativeId(10);

        when(customerRepository.findByIdentification("1792000001001")).thenReturn(Optional.empty());
        when(customerSubtypeRepository.findById(2)).thenReturn(Optional.of(new CustomerSubtype(2)));
        Customer representative = buildCustomer(10, "0911111111", "Carlos", "Diaz", null);
        when(customerRepository.findById(10)).thenReturn(Optional.of(representative));
        when(customerRepository.save(any(Customer.class))).thenAnswer(inv -> inv.getArgument(0));

        CustomerResponseDTO result = customerService.create(request);

        assertThat(result.getFullName()).isEqualTo("Empresa ABC S.A.");
    }

    @Test
    void findCustomerByAccountNumber_debeCombinarDatosDeCuentaYCliente() {
        AccountLookupResponse accountResponse = AccountLookupResponse.newBuilder()
                .setAccountId(100L).setAccountNumber("1234567890").setCustomerId(7L).setStatus("ACTIVA")
                .build();
        when(accountLookupGrpcClient.getAccountByNumber("1234567890")).thenReturn(accountResponse);
        Customer customer = buildCustomer(7, "0987654321", "Ana", "Herrera", null);
        when(customerRepository.findById(7)).thenReturn(Optional.of(customer));

        CustomerByAccountResponseDTO result = customerService.findCustomerByAccountNumber("1234567890");

        assertThat(result.getFullName()).isEqualTo("Ana Herrera");
        assertThat(result.getAccountStatus()).isEqualTo("ACTIVA");
    }

    @Test
    void updateStatus_debeLanzarExcepcion_cuandoClienteNoExiste() {
        when(customerRepository.findById(404)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.updateStatus("404", "ACTIVO"))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    private Customer buildCustomer(Integer id, String identification,
                                   String firstName, String lastName, String legalName) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setIdentification(identification);
        customer.setIdentificationType("CEDULA");
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setLegalName(legalName);
        customer.setStatus(CustomerStatusEnum.ACTIVO);
        return customer;
    }
}
