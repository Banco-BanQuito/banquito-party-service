package ec.edu.espe.banquito.core.party.service;

import ec.edu.espe.banquito.core.party.dto.CustomerSubtypeResponseDTO;
import ec.edu.espe.banquito.core.party.mapper.CustomerSubtypeMapper;
import ec.edu.espe.banquito.core.party.repository.CustomerSubtypeRepository;
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
                .map(CustomerSubtypeMapper::toResponse)
                .toList();
    }
}
