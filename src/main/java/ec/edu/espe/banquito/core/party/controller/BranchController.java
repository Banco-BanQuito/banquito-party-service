package ec.edu.espe.banquito.core.party.controller;

import ec.edu.espe.banquito.core.party.dto.BranchRequestDTO;
import ec.edu.espe.banquito.core.party.dto.BranchResponseDTO;
import ec.edu.espe.banquito.core.party.service.BranchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<BranchResponseDTO> createBranch(@Valid @RequestBody BranchRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.branchService.create(request));
    }
}
