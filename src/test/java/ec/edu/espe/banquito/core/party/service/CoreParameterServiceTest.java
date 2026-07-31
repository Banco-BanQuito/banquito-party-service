package ec.edu.espe.banquito.core.party.service;

import ec.edu.espe.banquito.core.party.dto.CoreParameterResponseDTO;
import ec.edu.espe.banquito.core.party.model.CoreParameter;
import ec.edu.espe.banquito.core.party.repository.CoreParameterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CoreParameterServiceTest {

    @Mock
    private CoreParameterRepository coreParameterRepository;

    @InjectMocks
    private CoreParameterService service;

    @Test
    void findAllMapsEntitiesToResponseDtos() {
        CoreParameter parameter = new CoreParameter("MAX_RETRIES");
        parameter.setValueString("3");
        when(coreParameterRepository.findAll()).thenReturn(List.of(parameter));

        List<CoreParameterResponseDTO> result = service.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCode()).isEqualTo("MAX_RETRIES");
    }

    @Test
    void findAllReturnsEmptyListWhenNoParametersExist() {
        when(coreParameterRepository.findAll()).thenReturn(List.of());

        assertThat(service.findAll()).isEmpty();
    }
}
