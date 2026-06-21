package ec.edu.espe.banquito.party.controller;

import ec.edu.espe.banquito.party.dto.CustomerSubtypeResponseDTO;
import ec.edu.espe.banquito.party.service.CustomerSubtypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/customer-subtypes")
@RequiredArgsConstructor
public class CustomerSubtypeController {

    private final CustomerSubtypeService customerSubtypeService;

    @GetMapping
    public List<CustomerSubtypeResponseDTO> getCustomerSubtypes() {
        return this.customerSubtypeService.findAll();
    }
}