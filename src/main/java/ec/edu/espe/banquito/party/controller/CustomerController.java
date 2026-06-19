package ec.edu.espe.banquito.party.controller;

import ec.edu.espe.banquito.party.dto.CustomerByAccountResponseDTO;
import ec.edu.espe.banquito.party.dto.CustomerRequestDTO;
import ec.edu.espe.banquito.party.dto.CustomerResponseDTO;
import ec.edu.espe.banquito.party.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/customers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/by-account/{accountNumber}")
    public CustomerByAccountResponseDTO getCustomerByAccountNumber(@PathVariable String accountNumber) {
        return this.customerService.findCustomerByAccountNumber(accountNumber);
    }

    @GetMapping("/{id}")
    public CustomerResponseDTO getCustomer(@PathVariable String id) {
        return this.customerService.findByIdOrIdentification(id);
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(@Valid @RequestBody CustomerRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.customerService.create(request));
    }

    @PatchMapping("/{id}/status/{status}")
    public CustomerResponseDTO updateCustomerStatus(@PathVariable String id, @PathVariable String status) {
        return this.customerService.updateStatus(id, status);
    }
}