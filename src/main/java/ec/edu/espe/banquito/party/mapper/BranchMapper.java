package ec.edu.espe.banquito.party.mapper;

import ec.edu.espe.banquito.party.dto.BranchResponseDTO;
import ec.edu.espe.banquito.party.model.Branch;

public class BranchMapper {

    private BranchMapper() {
    }

    public static BranchResponseDTO toResponse(Branch branch) {
        return new BranchResponseDTO(
                branch.getId(),
                branch.getBranchCode(),
                branch.getName(),
                branch.getCity()
        );
    }
}
