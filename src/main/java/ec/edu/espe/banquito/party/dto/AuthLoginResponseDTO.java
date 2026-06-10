package ec.edu.espe.banquito.party.dto;

import ec.edu.espe.banquito.party.enums.CustomerStatusEnum;
import ec.edu.espe.banquito.party.enums.CustomerTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthLoginResponseDTO {

    private Integer customerId;
    private String username;
    private String fullName;
    private CustomerTypeEnum customerType;
    private CustomerStatusEnum customerStatus;
    private String credentialStatus;
    private boolean mustChangePassword;

    public AuthLoginResponseDTO() {
    }

    public AuthLoginResponseDTO(Integer customerId, String username, String fullName,
                                CustomerTypeEnum customerType, CustomerStatusEnum customerStatus,
                                String credentialStatus, boolean mustChangePassword) {
        this.customerId = customerId;
        this.username = username;
        this.fullName = fullName;
        this.customerType = customerType;
        this.customerStatus = customerStatus;
        this.credentialStatus = credentialStatus;
        this.mustChangePassword = mustChangePassword;
    }
}