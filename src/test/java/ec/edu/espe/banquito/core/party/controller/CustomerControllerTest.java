package ec.edu.espe.banquito.core.party.controller;

import ec.edu.espe.banquito.core.party.dto.CustomerRequestDTO;
import ec.edu.espe.banquito.core.party.dto.CustomerResponseDTO;
import ec.edu.espe.banquito.core.party.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    private CustomerController controller;

    @BeforeEach
    void setUp() {
        controller = new CustomerController(customerService);
    }

    @Test
    void createCustomerDelegatesToServiceAndReturnsCreated() {
        CustomerRequestDTO request = new CustomerRequestDTO();
        CustomerResponseDTO created = new CustomerResponseDTO();
        created.setId(7);
        when(customerService.create(request)).thenReturn(created);

        ResponseEntity<CustomerResponseDTO> response = controller.createCustomer(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isSameAs(created);
    }

    @Test
    void getCustomerDelegatesToService() {
        CustomerResponseDTO expected = new CustomerResponseDTO();
        when(customerService.findByIdOrIdentification("5")).thenReturn(expected);

        assertThat(controller.getCustomer("5")).isSameAs(expected);
    }

    @Test
    void updateCustomerStatusDelegatesToService() {
        CustomerResponseDTO expected = new CustomerResponseDTO();
        when(customerService.updateStatus("5", "BLOQUEADO")).thenReturn(expected);

        assertThat(controller.updateCustomerStatus("5", "BLOQUEADO")).isSameAs(expected);
    }
}
