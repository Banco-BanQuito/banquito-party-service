package ec.edu.espe.banquito.party.mapper;

import ec.edu.espe.banquito.party.dto.CoreParameterResponseDTO;
import ec.edu.espe.banquito.party.model.CoreParameter;

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
