package ec.edu.espe.banquito.party.controller;

import ec.edu.espe.banquito.party.dto.CustomerByAccountResponseDTO;
import ec.edu.espe.banquito.party.dto.CustomerResponseDTO;
import ec.edu.espe.banquito.party.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/customers")
@RequiredArgsConstructor
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
}