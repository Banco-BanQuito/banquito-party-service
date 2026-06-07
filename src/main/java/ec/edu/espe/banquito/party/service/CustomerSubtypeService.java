package ec.edu.espe.banquito.party.service;

import ec.edu.espe.banquito.party.dto.CustomerSubtypeResponseDTO;
import ec.edu.espe.banquito.party.model.CustomerSubtype;
import ec.edu.espe.banquito.party.repository.CustomerSubtypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerSubtypeService {

    private final CustomerSubtypeRepository customerSubtypeRepository;

    public List<CustomerSubtypeResponseDTO> findAll() {
        return this.customerSubtypeRepository.findAll()
                .stream()
                .map(this::buildCustomerSubtypeResponse)
                .toList();
    }

    private CustomerSubtypeResponseDTO buildCustomerSubtypeResponse(CustomerSubtype customerSubtype) {
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