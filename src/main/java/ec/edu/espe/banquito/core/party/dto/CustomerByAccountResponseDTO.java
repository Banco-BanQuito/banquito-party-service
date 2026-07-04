package ec.edu.espe.banquito.core.party.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerByAccountResponseDTO {

    private Long accountId;
    private String accountNumber;
    private Integer customerId;
    private String fullName;
    private String customerType;
    private String accountStatus;
    private String customerStatus;

    public CustomerByAccountResponseDTO() {
    }

    public CustomerByAccountResponseDTO(Long accountId, String accountNumber, Integer customerId,
                                        String fullName, String customerType, String accountStatus,
                                        String customerStatus) {
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.fullName = fullName;
        this.customerType = customerType;
        this.accountStatus = accountStatus;
        this.customerStatus = customerStatus;
    }
}