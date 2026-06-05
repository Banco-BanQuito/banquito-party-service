package ec.edu.espe.banquito.party.controller;

import ec.edu.espe.banquito.party.dto.BranchResponseDTO;
import ec.edu.espe.banquito.party.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @GetMapping
    public List<BranchResponseDTO> getBranches() {
        return this.branchService.findAll();
    }
}