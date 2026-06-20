package ec.edu.espe.banquito.party.service;

import ec.edu.espe.banquito.party.client.AccountLookupGrpcClient;
import ec.edu.espe.banquito.party.dto.CustomerByAccountResponseDTO;
import ec.edu.espe.banquito.party.dto.CustomerRequestDTO;
import ec.edu.espe.banquito.party.dto.CustomerResponseDTO;
import ec.edu.espe.banquito.party.enums.CustomerStatusEnum;
import ec.edu.espe.banquito.party.enums.CustomerTypeEnum;
import ec.edu.espe.banquito.party.exception.CustomerNotFoundException;
import ec.edu.espe.banquito.party.grpc.accountlookup.AccountLookupResponse;
import ec.edu.espe.banquito.party.model.Customer;
import ec.edu.espe.banquito.party.model.CustomerSubtype;
import ec.edu.espe.banquito.party.repository.CustomerRepository;
import ec.edu.espe.banquito.party.repository.CustomerSubtypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerSubtypeRepository customerSubtypeRepository;
    private final AccountLookupGrpcClient accountLookupGrpcClient;

    @Transactional
    public CustomerResponseDTO create(CustomerRequestDTO request) {
        this.customerRepository.findByIdentification(request.getIdentification())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Ya existe un cliente con esa identificación");
                });

        CustomerSubtype subtype = this.customerSubtypeRepository.findById(request.getCustomerSubtypeId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Subtipo de cliente no encontrado: " + request.getCustomerSubtypeId()));

        Customer customer = new Customer();
        customer.setCustomerSubtype(subtype);
        customer.setCustomerType(CustomerTypeEnum.valueOf(request.getCustomerType()));
        customer.setIdentificationType(request.getIdentificationType());
        customer.setIdentification(request.getIdentification());
        customer.setEmail(request.getEmail());
        customer.setMobilePhone(request.getMobilePhone());
        customer.setAddress(request.getAddress());
        customer.setStatus(CustomerStatusEnum.ACTIVO);
        customer.setIsFavorite(false);
        customer.setVersion(0);

        if (customer.getCustomerType() == CustomerTypeEnum.JURIDICO) {
            customer.setLegalName(request.getLegalName());
            customer.setConstitutionDate(request.getConstitutionDate());

            Customer representative = this.customerRepository.findById(request.getLegalRepresentativeId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Representante legal no encontrado: " + request.getLegalRepresentativeId()));
            customer.setLegalRepresentative(representative);
        } else {
            customer.setFirstName(request.getFirstName());
            customer.setLastName(request.getLastName());
            customer.setBirthDate(request.getBirthDate());
        }

        return this.buildCustomerResponse(this.customerRepository.save(customer));
    }

    @Transactional
    public CustomerResponseDTO updateStatus(String id, String status) {
        Customer customer = this.customerRepository.findById(Integer.valueOf(id))
                .orElseThrow(() -> new CustomerNotFoundException("Cliente no encontrado con id: " + id));

        customer.setStatus(CustomerStatusEnum.valueOf(status));

        return this.buildCustomerResponse(this.customerRepository.save(customer));
    }

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