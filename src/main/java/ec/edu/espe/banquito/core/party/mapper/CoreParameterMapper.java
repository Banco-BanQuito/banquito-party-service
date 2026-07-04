package ec.edu.espe.banquito.core.party.mapper;

import ec.edu.espe.banquito.core.party.dto.CoreParameterResponseDTO;
import ec.edu.espe.banquito.core.party.model.CoreParameter;

public class CoreParameterMapper {

    private CoreParameterMapper() {
    }

    public static CoreParameterResponseDTO toResponse(CoreParameter coreParameter) {
        return new CoreParameterResponseDTO(
                coreParameter.getCode(),
                coreParameter.getName(),
                coreParameter.getValueString(),
                coreParameter.getDataType(),
                coreParameter.getDescription()
        );
    }
}
