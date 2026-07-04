package ec.edu.espe.banquito.core.party.service;

import ec.edu.espe.banquito.core.party.client.AccountLookupGrpcClient;
import ec.edu.espe.banquito.core.party.dto.CustomerByAccountResponseDTO;
import ec.edu.espe.banquito.core.party.dto.CustomerRequestDTO;
import ec.edu.espe.banquito.core.party.dto.CustomerResponseDTO;
import ec.edu.espe.banquito.core.party.enums.CustomerStatusEnum;
import ec.edu.espe.banquito.core.party.enums.CustomerTypeEnum;
import ec.edu.espe.banquito.core.party.exception.CustomerNotFoundException;
import ec.edu.espe.banquito.core.party.grpc.accountlookup.AccountLookupResponse;
import ec.edu.espe.banquito.core.party.mapper.CustomerMapper;
import ec.edu.espe.banquito.core.party.model.Customer;
import ec.edu.espe.banquito.core.party.model.CustomerSubtype;
import ec.edu.espe.banquito.core.party.repository.CustomerRepository;
import ec.edu.espe.banquito.core.party.repository.CustomerSubtypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerService {

private static final String CUSTOMER_NOT_FOUND_BY_ID = "Cliente no encontrado con id: ";
private static final String CUSTOMER_NOT_FOUND_BY_IDENTIFICATION = "Cliente no encontrado con identificación: ";

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

    return CustomerMapper.toResponse(this.customerRepository.save(customer));
}

@Transactional
public CustomerResponseDTO updateStatus(String id, String status) {
    Customer customer = this.customerRepository.findById(Integer.valueOf(id))
            .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND_BY_ID + id));

    customer.setStatus(CustomerStatusEnum.valueOf(status));

    return CustomerMapper.toResponse(this.customerRepository.save(customer));
}

public CustomerResponseDTO findByIdOrIdentification(String value) {
    Customer customer;

    if (value.matches("\\d+") && value.length() <= 6) {
        Integer id = Integer.valueOf(value);
        customer = this.customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND_BY_ID + value));
    } else {
        customer = this.customerRepository.findByIdentification(value)
                .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND_BY_IDENTIFICATION + value));
    }

    return CustomerMapper.toResponse(customer);
}

public CustomerByAccountResponseDTO findCustomerByAccountNumber(String accountNumber) {
    AccountLookupResponse accountResponse = this.accountLookupGrpcClient.getAccountByNumber(accountNumber);

    Integer customerId = Math.toIntExact(accountResponse.getCustomerId());

    Customer customer = this.customerRepository.findById(customerId)
            .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND_BY_ID + customerId));

    String fullName = CustomerMapper.buildFullName(customer);

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
}
