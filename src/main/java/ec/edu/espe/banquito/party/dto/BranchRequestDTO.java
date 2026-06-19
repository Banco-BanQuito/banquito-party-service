package ec.edu.espe.banquito.party.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BranchRequestDTO {

    @NotBlank
    @Size(max = 10)
    private String branchCode;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String city;
}
