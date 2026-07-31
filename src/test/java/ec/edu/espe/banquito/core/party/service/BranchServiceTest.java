package ec.edu.espe.banquito.core.party.service;

import ec.edu.espe.banquito.core.party.dto.BranchRequestDTO;
import ec.edu.espe.banquito.core.party.dto.BranchResponseDTO;
import ec.edu.espe.banquito.core.party.model.Branch;
import ec.edu.espe.banquito.core.party.repository.BranchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BranchServiceTest {

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private BranchService branchService;

    @Test
    void findAll_debeRetornarListaDeBranchesMapeados() {
        Branch branch = new Branch();
        branch.setId(1);
        branch.setBranchCode("001");
        branch.setName("Sucursal Norte");
        branch.setCity("Quito");

        when(branchRepository.findAll()).thenReturn(List.of(branch));

        List<BranchResponseDTO> result = branchService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1);
        assertThat(result.get(0).getBranchCode()).isEqualTo("001");
        assertThat(result.get(0).getName()).isEqualTo("Sucursal Norte");
        assertThat(result.get(0).getCity()).isEqualTo("Quito");
    }

    @Test
    void findAll_debeRetornarListaVacia_cuandoNoHayBranches() {
        when(branchRepository.findAll()).thenReturn(Collections.emptyList());

        List<BranchResponseDTO> result = branchService.findAll();

        assertThat(result).isEmpty();
    }

    @Test
    void create_debeGuardarBranch_cuandoCodigoNoExiste() {
        BranchRequestDTO request = new BranchRequestDTO();
        request.setBranchCode("002");
        request.setName("Sucursal Sur");
        request.setCity("Guayaquil");
        when(branchRepository.findByBranchCode("002")).thenReturn(Optional.empty());
        when(branchRepository.save(any(Branch.class))).thenAnswer(inv -> inv.getArgument(0));

        BranchResponseDTO result = branchService.create(request);

        assertThat(result.getBranchCode()).isEqualTo("002");
    }

    @Test
    void create_debeLanzarExcepcion_cuandoCodigoYaExiste() {
        BranchRequestDTO request = new BranchRequestDTO();
        request.setBranchCode("001");
        when(branchRepository.findByBranchCode("001")).thenReturn(Optional.of(new Branch()));

        assertThatThrownBy(() -> branchService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
