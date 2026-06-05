package ec.edu.espe.banquito.party.service;

import ec.edu.espe.banquito.party.dto.CustomerResponseDTO;
import ec.edu.espe.banquito.party.exception.CustomerNotFoundException;
import ec.edu.espe.banquito.party.model.Customer;
import ec.edu.espe.banquito.party.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerResponseDTO findByIdOrIdentification(String value) {
        Customer customer;

        if (value.matches("\\d+") && value.length() <= 6) {
            Integer id = Integer.valueOf(value);
            customer = this.customerRepository.findById(id)
                    .orElseThrow(() -> new CustomerNotFoundException("Cliente no encontrado con id: " + value));
        } else {
            customer = this.customerRepository.findByIdentification(value)
                    .orElseThrow(() -> new CustomerNotFoundException("Cliente no encontrado con identificación: " + value));
        }

        return this.buildCustomerResponse(customer);
    }

    private CustomerResponseDTO buildCustomerResponse(Customer customer) {
        String fullName;

        if (customer.getCustomerType() != null && "JURIDICO".equals(customer.getCustomerType().getValue())) {
            fullName = customer.getLegalName();
        } else {
            fullName = ((customer.getFirstName() != null ? customer.getFirstName() : "") + " " +
                    (customer.getLastName() != null ? customer.getLastName() : "")).trim();
        }

        return new CustomerResponseDTO(
                customer.getId(),
                customer.getCustomerType(),
                customer.getIdentificationType(),
                customer.getIdentification(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getLegalName(),
                fullName,
                customer.getEmail(),
                customer.getMobilePhone(),
                customer.getAddress(),
                customer.getStatus()
        );
    }
}