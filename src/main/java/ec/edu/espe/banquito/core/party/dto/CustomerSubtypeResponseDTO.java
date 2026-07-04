package ec.edu.espe.banquito.core.party.dto;

import ec.edu.espe.banquito.core.party.enums.CustomerStatusEnum;
import ec.edu.espe.banquito.core.party.enums.CustomerTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerSubtypeResponseDTO {

    private Integer id;
    private CustomerTypeEnum customerType;
    private String name;
    private String description;
    private CustomerStatusEnum status;
    private String observations;

    public CustomerSubtypeResponseDTO() {
    }

    public CustomerSubtypeResponseDTO(Integer id, CustomerTypeEnum customerType, String name,
                                      String description, CustomerStatusEnum status, String observations) {
        this.id = id;
        this.customerType = customerType;
        this.name = name;
        this.description = description;
        this.status = status;
        this.observations = observations;
    }
}