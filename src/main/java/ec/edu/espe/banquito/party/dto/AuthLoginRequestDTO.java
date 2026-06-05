package ec.edu.espe.banquito.party.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthLoginRequestDTO {

    private String username;
    private String password;

    public AuthLoginRequestDTO() {
    }

    public AuthLoginRequestDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}