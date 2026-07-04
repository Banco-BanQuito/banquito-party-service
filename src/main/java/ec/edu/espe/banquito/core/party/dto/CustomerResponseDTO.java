package ec.edu.espe.banquito.core.party.dto;

import ec.edu.espe.banquito.core.party.enums.CustomerStatusEnum;
import ec.edu.espe.banquito.core.party.enums.CustomerTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerResponseDTO {

    private Integer id;
    private CustomerTypeEnum customerType;
    private String identificationType;
    private String identification;
    private String firstName;
    private String lastName;
    private String legalName;
    private String fullName;
    private String email;
    private String mobilePhone;
    private String address;
    private CustomerStatusEnum status;

}