package ec.edu.espe.banquito.party.mapper;

import ec.edu.espe.banquito.party.dto.CustomerResponseDTO;
import ec.edu.espe.banquito.party.model.Customer;

public class CustomerMapper {

    private CustomerMapper() {
    }

    public static CustomerResponseDTO toResponse(Customer customer) {
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

    public static String buildFullName(Customer customer) {
        if (customer.getCustomerType() != null && "JURIDICO".equals(customer.getCustomerType().getValue())) {
            return customer.getLegalName();
        }

        return ((customer.getFirstName() != null ? customer.getFirstName() : "") + " " +
                (customer.getLastName() != null ? customer.getLastName() : "")).trim();
    }
}
