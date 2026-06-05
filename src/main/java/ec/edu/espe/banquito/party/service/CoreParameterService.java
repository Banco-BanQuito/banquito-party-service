package ec.edu.espe.banquito.party.service;

import ec.edu.espe.banquito.party.dto.CoreParameterResponseDTO;
import ec.edu.espe.banquito.party.model.CoreParameter;
import ec.edu.espe.banquito.party.repository.CoreParameterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoreParameterService {

    private final CoreParameterRepository coreParameterRepository;

    public List<CoreParameterResponseDTO> findAll() {
        return this.coreParameterRepository.findAll()
                .stream()
                .map(this::buildCoreParameterResponse)
                .toList();
    }

    private CoreParameterResponseDTO buildCoreParameterResponse(CoreParameter coreParameter) {
        return new CoreParameterResponseDTO(
                coreParameter.getCode(),
                coreParameter.getName(),
                coreParameter.getValueString(),
                coreParameter.getDataType(),
                coreParameter.getDescription()
        );
    }
}