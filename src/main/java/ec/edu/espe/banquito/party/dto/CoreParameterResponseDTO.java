package ec.edu.espe.banquito.party.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoreParameterResponseDTO {

    private String code;
    private String name;
    private String valueString;
    private String dataType;
    private String description;

    public CoreParameterResponseDTO() {
    }

    public CoreParameterResponseDTO(String code, String name, String valueString,
                                    String dataType, String description) {
        this.code = code;
        this.name = name;
        this.valueString = valueString;
        this.dataType = dataType;
        this.description = description;
    }
}