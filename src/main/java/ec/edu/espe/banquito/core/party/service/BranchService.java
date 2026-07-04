package ec.edu.espe.banquito.core.party.service;

import ec.edu.espe.banquito.core.party.dto.BranchRequestDTO;
import ec.edu.espe.banquito.core.party.dto.BranchResponseDTO;
import ec.edu.espe.banquito.core.party.mapper.BranchMapper;
import ec.edu.espe.banquito.core.party.model.Branch;
import ec.edu.espe.banquito.core.party.repository.BranchRepository;
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
                .map(BranchMapper::toResponse)
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
        branch.setVersion(0);

        return BranchMapper.toResponse(this.branchRepository.save(branch));
    }
}
