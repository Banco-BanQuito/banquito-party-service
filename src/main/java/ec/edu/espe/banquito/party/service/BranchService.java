package ec.edu.espe.banquito.party.service;

import ec.edu.espe.banquito.party.dto.BranchResponseDTO;
import ec.edu.espe.banquito.party.model.Branch;
import ec.edu.espe.banquito.party.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;

    public List<BranchResponseDTO> findAll() {
        return this.branchRepository.findAll()
                .stream()
                .map(this::buildBranchResponse)
                .toList();
    }

    private BranchResponseDTO buildBranchResponse(Branch branch) {
        return new BranchResponseDTO(
                branch.getId(),
                branch.getBranchCode(),
                branch.getName(),
                branch.getCity()
        );
    }
}