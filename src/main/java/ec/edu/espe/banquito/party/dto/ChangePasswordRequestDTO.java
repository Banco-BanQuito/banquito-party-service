package ec.edu.espe.banquito.party.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequestDTO {

    private String username;
    private String currentPassword;
    private String newPassword;

    public ChangePasswordRequestDTO() {
        // Constructor requerido por frameworks de serialización/deserialización.
    }
}
