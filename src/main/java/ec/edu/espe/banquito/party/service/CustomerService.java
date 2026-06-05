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
        String fullName = this.buildFullName(customer);

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

    private String buildFullName(Customer customer) {
        if (customer.getCustomerType() != null && "JURIDICO".equals(customer.getCustomerType().getValue())) {
            return customer.getLegalName();
        }

        return ((customer.getFirstName() != null ? customer.getFirstName() : "") + " " +
                (customer.getLastName() != null ? customer.getLastName() : "")).trim();
    }
}