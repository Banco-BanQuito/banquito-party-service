package ec.edu.espe.banquito.party.service;

import ec.edu.espe.banquito.party.dto.BranchRequestDTO;
import ec.edu.espe.banquito.party.dto.BranchResponseDTO;
import ec.edu.espe.banquito.party.model.Branch;
import ec.edu.espe.banquito.party.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public BranchResponseDTO create(BranchRequestDTO request) {
        this.branchRepository.findByBranchCode(request.getBranchCode())
                .ifPresent(branch -> {
                    throw new IllegalArgumentException("Branch code already exists");
                });

        Branch branch = new Branch();
        branch.setBranchCode(request.getBranchCode());
        branch.setName(request.getName());
        branch.setCity(request.getCity());
        branch.setCreationDate(LocalDateTime.now());
        branch.setVersion(0);

        return buildBranchResponse(this.branchRepository.save(branch));
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
