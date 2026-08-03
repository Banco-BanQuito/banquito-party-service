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

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
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
    void create_debeCrearClienteNaturalYCuentaIdentityPlatform() {
        CustomerRequestDTO request = naturalRequest();
        CustomerSubtype subtype = subtype(1, CustomerTypeEnum.NATURAL);
        when(customerRepository.findByIdentification("1750285577")).thenReturn(Optional.empty());
        when(customerSubtypeRepository.findById(1)).thenReturn(Optional.of(subtype));
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> {
            Customer customer = invocation.getArgument(0);
            customer.setId(10);
            return customer;
        });

        CustomerResponseDTO result = customerService.create(request);

        assertThat(result.getId()).isEqualTo(10);
        assertThat(result.getIdentification()).isEqualTo("1750285577");
        assertThat(result.getFullName()).isEqualTo("Juan Perez");
        verify(identityPlatformService).createAccount("1750285577", "Juan Perez");
    }

    @Test
    void create_debeCrearClienteJuridicoConRepresentante() {
        CustomerRequestDTO request = legalRequest();
        CustomerSubtype subtype = subtype(2, CustomerTypeEnum.JURIDICO);
        Customer representative = buildCustomer(7, "0800000001", "Ana", "Lopez", null);
        when(customerRepository.findByIdentification("1792000001001")).thenReturn(Optional.empty());
        when(customerSubtypeRepository.findById(2)).thenReturn(Optional.of(subtype));
        when(customerRepository.findById(7)).thenReturn(Optional.of(representative));
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> {
            Customer customer = invocation.getArgument(0);
            customer.setId(11);
            return customer;
        });

        CustomerResponseDTO result = customerService.create(request);

        assertThat(result.getId()).isEqualTo(11);
        assertThat(result.getCustomerType()).isEqualTo(CustomerTypeEnum.JURIDICO);
        assertThat(result.getFullName()).isEqualTo("Empresa BanQuito");
        verify(identityPlatformService).createAccount("1792000001001", "Empresa BanQuito");
    }

    @Test
    void create_debeRechazarRepresentanteLegalInexistente() {
        CustomerRequestDTO request = legalRequest();
        CustomerSubtype subtype = subtype(2, CustomerTypeEnum.JURIDICO);
        when(customerRepository.findByIdentification("1792000001001")).thenReturn(Optional.empty());
        when(customerSubtypeRepository.findById(2)).thenReturn(Optional.of(subtype));
        when(customerRepository.findById(7)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Representante legal");
    }

    @Test
    void create_debeRechazarIdentificacionDuplicada() {
        CustomerRequestDTO request = naturalRequest();
        when(customerRepository.findByIdentification("1750285577"))
                .thenReturn(Optional.of(buildCustomer(1, "1750285577", "Juan", "Perez", null)));

        assertThatThrownBy(() -> customerService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ya existe");
    }

    @Test
    void create_debeRechazarSubtipoInexistente() {
        CustomerRequestDTO request = naturalRequest();
        when(customerRepository.findByIdentification("1750285577")).thenReturn(Optional.empty());
        when(customerSubtypeRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Subtipo");
    }

    @Test
    void updateStatus_debeCambiarEstadoDelCliente() {
        Customer customer = buildCustomer(1, "1750285577", "Juan", "Perez", null);
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CustomerResponseDTO result = customerService.updateStatus("1", "INACTIVO");

        assertThat(result.getStatus()).isEqualTo(CustomerStatusEnum.INACTIVO);
    }

    @Test
    void updateStatus_debeLanzarExceptionSiClienteNoExiste() {
        when(customerRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.updateStatus("99", "INACTIVO"))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void findCustomerByAccountNumber_debeResolverClienteDesdeCore() {
        AccountLookupResponse accountResponse = AccountLookupResponse.newBuilder()
                .setAccountId(1001L)
                .setAccountNumber("1010114999")
                .setCustomerId(10L)
                .setStatus("ACTIVA")
                .build();
        Customer customer = buildCustomer(10, "1750285577", "Juan", "Perez", null);
        when(accountLookupGrpcClient.getAccountByNumber("1010114999")).thenReturn(accountResponse);
        when(customerRepository.findById(10)).thenReturn(Optional.of(customer));

        CustomerByAccountResponseDTO result = customerService.findCustomerByAccountNumber("1010114999");

        assertThat(result.getAccountNumber()).isEqualTo("1010114999");
        assertThat(result.getCustomerId()).isEqualTo(10);
        assertThat(result.getFullName()).isEqualTo("Juan Perez");
    }

    @Test
    void findCustomerByAccountNumber_debeLanzarExceptionSiNoExisteCliente() {
        AccountLookupResponse accountResponse = AccountLookupResponse.newBuilder()
                .setAccountId(1001L)
                .setAccountNumber("1010114999")
                .setCustomerId(99L)
                .setStatus("ACTIVA")
                .build();
        when(accountLookupGrpcClient.getAccountByNumber("1010114999")).thenReturn(accountResponse);
        when(customerRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.findCustomerByAccountNumber("1010114999"))
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

    private CustomerSubtype subtype(Integer id, CustomerTypeEnum type) {
        CustomerSubtype subtype = new CustomerSubtype();
        subtype.setId(id);
        subtype.setCustomerType(type);
        subtype.setName(type.name());
        subtype.setStatus(CustomerStatusEnum.ACTIVO);
        return subtype;
    }

    private CustomerRequestDTO naturalRequest() {
        CustomerRequestDTO request = new CustomerRequestDTO();
        request.setCustomerType("NATURAL");
        request.setCustomerSubtypeId(1);
        request.setIdentificationType("CEDULA");
        request.setIdentification("1750285577");
        request.setEmail("juan@banquito.internal");
        request.setMobilePhone("0999999999");
        request.setAddress("Quito");
        request.setFirstName("Juan");
        request.setLastName("Perez");
        request.setBirthDate(LocalDate.of(1990, 1, 1));
        return request;
    }

    private CustomerRequestDTO legalRequest() {
        CustomerRequestDTO request = new CustomerRequestDTO();
        request.setCustomerType("JURIDICO");
        request.setCustomerSubtypeId(2);
        request.setIdentificationType("RUC");
        request.setIdentification("1792000001001");
        request.setEmail("empresa@banquito.internal");
        request.setMobilePhone("0999999999");
        request.setAddress("Quito");
        request.setLegalName("Empresa BanQuito");
        request.setConstitutionDate(LocalDate.of(2020, 1, 1));
        request.setLegalRepresentativeId(7);
        return request;
    }
}
