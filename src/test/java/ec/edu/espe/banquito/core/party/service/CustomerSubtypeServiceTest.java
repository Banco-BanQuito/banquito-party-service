package ec.edu.espe.banquito.core.party.service;

import ec.edu.espe.banquito.core.party.dto.CustomerSubtypeResponseDTO;
import ec.edu.espe.banquito.core.party.enums.CustomerTypeEnum;
import ec.edu.espe.banquito.core.party.model.CustomerSubtype;
import ec.edu.espe.banquito.core.party.repository.CustomerSubtypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerSubtypeServiceTest {

    @Mock
    private CustomerSubtypeRepository customerSubtypeRepository;

    @InjectMocks
    private CustomerSubtypeService service;

    @Test
    void findAllMapsEntitiesToResponseDtos() {
        CustomerSubtype subtype = new CustomerSubtype(1);
        subtype.setCustomerType(CustomerTypeEnum.NATURAL);
        subtype.setName("VIP");
        when(customerSubtypeRepository.findAll()).thenReturn(List.of(subtype));

        List<CustomerSubtypeResponseDTO> result = service.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("VIP");
    }

    @Test
    void findAllReturnsEmptyListWhenNoSubtypesExist() {
        when(customerSubtypeRepository.findAll()).thenReturn(List.of());

        assertThat(service.findAll()).isEmpty();
    }
}
