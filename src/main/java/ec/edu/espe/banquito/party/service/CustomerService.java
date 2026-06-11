package ec.edu.espe.banquito.party.service;

import ec.edu.espe.banquito.party.client.AccountLookupGrpcClient;
import ec.edu.espe.banquito.party.dto.CustomerByAccountResponseDTO;
import ec.edu.espe.banquito.party.dto.CustomerResponseDTO;
import ec.edu.espe.banquito.party.exception.CustomerNotFoundException;
import ec.edu.espe.banquito.party.grpc.accountlookup.AccountLookupResponse;
import ec.edu.espe.banquito.party.model.Customer;
import ec.edu.espe.banquito.party.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AccountLookupGrpcClient accountLookupGrpcClient;

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

    public CustomerByAccountResponseDTO findCustomerByAccountNumber(String accountNumber) {
        AccountLookupResponse accountResponse = this.accountLookupGrpcClient.getAccountByNumber(accountNumber);

        Integer customerId = Math.toIntExact(accountResponse.getCustomerId());

        Customer customer = this.customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente no encontrado con id: " + customerId));

        String fullName = this.buildFullName(customer);

        return new CustomerByAccountResponseDTO(
                accountResponse.getAccountId(),
                accountResponse.getAccountNumber(),
                customer.getId(),
                fullName,
                customer.getCustomerType() != null ? customer.getCustomerType().getValue() : null,
                accountResponse.getStatus(),
                customer.getStatus() != null ? customer.getStatus().getValue() : null
        );
    }

    private CustomerResponseDTO buildCustomerResponse(Customer customer) {
        CustomerResponseDTO response = new CustomerResponseDTO();

        response.setId(customer.getId());
        response.setCustomerType(customer.getCustomerType());
        response.setIdentificationType(customer.getIdentificationType());
        response.setIdentification(customer.getIdentification());
        response.setFirstName(customer.getFirstName());
        response.setLastName(customer.getLastName());
        response.setLegalName(customer.getLegalName());

        String fullName;

        if (customer.getLegalName() != null && !customer.getLegalName().isBlank()) {
            fullName = customer.getLegalName();
        } else {
            String firstName = customer.getFirstName() != null ? customer.getFirstName() : "";
            String lastName = customer.getLastName() != null ? customer.getLastName() : "";
            fullName = (firstName + " " + lastName).trim();
        }

        response.setFullName(fullName);
        response.setEmail(customer.getEmail());
        response.setMobilePhone(customer.getMobilePhone());
        response.setAddress(customer.getAddress());
        response.setStatus(customer.getStatus());

        return response;
    }

    private String buildFullName(Customer customer) {
        if (customer.getCustomerType() != null && "JURIDICO".equals(customer.getCustomerType().getValue())) {
            return customer.getLegalName();
        }

        return ((customer.getFirstName() != null ? customer.getFirstName() : "") + " " +
                (customer.getLastName() != null ? customer.getLastName() : "")).trim();
    }
}