package ec.edu.espe.banquito.core.party.controller;

import ec.edu.espe.banquito.core.party.dto.BranchRequestDTO;
import ec.edu.espe.banquito.core.party.dto.BranchResponseDTO;
import ec.edu.espe.banquito.core.party.service.BranchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BranchControllerTest {

    @Mock
    private BranchService branchService;

    private BranchController controller;

    @BeforeEach
    void setUp() {
        controller = new BranchController(branchService);
    }

    @Test
    void getBranchesDelegatesToBranchService() {
        BranchResponseDTO branch = new BranchResponseDTO(1, "B001", "Matriz", "Quito");
        when(branchService.findAll()).thenReturn(List.of(branch));

        assertThat(controller.getBranches()).containsExactly(branch);
    }

    @Test
    void createBranchDelegatesToBranchServiceAndReturnsCreated() {
        BranchRequestDTO request = new BranchRequestDTO();
        BranchResponseDTO created = new BranchResponseDTO(2, "B002", "Sucursal Norte", "Quito");
        when(branchService.create(request)).thenReturn(created);

        ResponseEntity<BranchResponseDTO> response = controller.createBranch(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
}
