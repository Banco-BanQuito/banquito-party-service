package ec.edu.espe.banquito.core.party.controller;

import ec.edu.espe.banquito.core.party.dto.CoreParameterResponseDTO;
import ec.edu.espe.banquito.core.party.service.CoreParameterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CoreParameterControllerTest {

    @Mock
    private CoreParameterService coreParameterService;

    @Test
    void getCoreParametersDelegatesToService() {
        CoreParameterResponseDTO parameter = new CoreParameterResponseDTO("CODE", "Nombre", "valor", "STRING", "desc");
        when(coreParameterService.findAll()).thenReturn(List.of(parameter));

        CoreParameterController controller = new CoreParameterController(coreParameterService);

        assertThat(controller.getCoreParameters()).containsExactly(parameter);
    }
}
