package ec.edu.espe.banquito.party.dto;

import ec.edu.espe.banquito.party.enums.CustomerStatusEnum;
import ec.edu.espe.banquito.party.enums.CustomerTypeEnum;
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

    public CustomerResponseDTO() {
    }

    public CustomerResponseDTO(Integer id, CustomerTypeEnum customerType, String identificationType,
                               String identification, String firstName, String lastName,
                               String legalName, String fullName, String email,
                               String mobilePhone, String address, CustomerStatusEnum status) {
        this.id = id;
        this.customerType = customerType;
        this.identificationType = identificationType;
        this.identification = identification;
        this.firstName = firstName;
        this.lastName = lastName;
        this.legalName = legalName;
        this.fullName = fullName;
        this.email = email;
        this.mobilePhone = mobilePhone;
        this.address = address;
        this.status = status;
    }
}