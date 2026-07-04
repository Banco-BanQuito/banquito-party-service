package ec.edu.espe.banquito.core.party.service;

import ec.edu.espe.banquito.core.party.client.AccountLookupGrpcClient;
import ec.edu.espe.banquito.core.party.dto.CustomerResponseDTO;
import ec.edu.espe.banquito.core.party.enums.CustomerStatusEnum;
import ec.edu.espe.banquito.core.party.enums.CustomerTypeEnum;
import ec.edu.espe.banquito.core.party.exception.CustomerNotFoundException;
import ec.edu.espe.banquito.core.party.model.Customer;
import ec.edu.espe.banquito.core.party.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountLookupGrpcClient accountLookupGrpcClient;

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
