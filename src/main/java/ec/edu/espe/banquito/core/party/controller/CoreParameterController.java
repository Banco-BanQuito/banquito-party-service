package ec.edu.espe.banquito.core.party.controller;

import ec.edu.espe.banquito.core.party.dto.CoreParameterResponseDTO;
import ec.edu.espe.banquito.core.party.service.CoreParameterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/core-parameters")
@RequiredArgsConstructor
public class CoreParameterController {

    private final CoreParameterService coreParameterService;

    @GetMapping
    public List<CoreParameterResponseDTO> getCoreParameters() {
        return this.coreParameterService.findAll();
    }
}