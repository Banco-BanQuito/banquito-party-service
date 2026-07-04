package ec.edu.espe.banquito.core.party.mapper;

import ec.edu.espe.banquito.core.party.dto.CustomerSubtypeResponseDTO;
import ec.edu.espe.banquito.core.party.model.CustomerSubtype;

public class CustomerSubtypeMapper {

    private CustomerSubtypeMapper() {
    }

    public static CustomerSubtypeResponseDTO toResponse(CustomerSubtype customerSubtype) {
        return new CustomerSubtypeResponseDTO(
                customerSubtype.getId(),
                customerSubtype.getCustomerType(),
                customerSubtype.getName(),
                customerSubtype.getDescription(),
                customerSubtype.getStatus(),
                customerSubtype.getObservations()
        );
    }
}
