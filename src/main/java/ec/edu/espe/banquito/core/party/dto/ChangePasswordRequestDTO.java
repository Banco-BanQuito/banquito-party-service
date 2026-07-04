package ec.edu.espe.banquito.core.party.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequestDTO {

    private String username;
    private String currentPassword;
    private String newPassword;

    public ChangePasswordRequestDTO() {
    }
}
