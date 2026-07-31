package ec.edu.espe.banquito.core.party.controller;

import ec.edu.espe.banquito.core.party.dto.CustomerSubtypeResponseDTO;
import ec.edu.espe.banquito.core.party.enums.CustomerStatusEnum;
import ec.edu.espe.banquito.core.party.enums.CustomerTypeEnum;
import ec.edu.espe.banquito.core.party.service.CustomerSubtypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerSubtypeControllerTest {

    @Mock
    private CustomerSubtypeService customerSubtypeService;

    @Test
    void getCustomerSubtypesDelegatesToService() {
        CustomerSubtypeResponseDTO subtype = new CustomerSubtypeResponseDTO(
                1, CustomerTypeEnum.NATURAL, "VIP", "desc", CustomerStatusEnum.ACTIVO, "obs");
        when(customerSubtypeService.findAll()).thenReturn(List.of(subtype));

        CustomerSubtypeController controller = new CustomerSubtypeController(customerSubtypeService);

        assertThat(controller.getCustomerSubtypes()).containsExactly(subtype);
    }
}
