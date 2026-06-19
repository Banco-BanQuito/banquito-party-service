package ec.edu.espe.banquito.party.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CustomerRequestDTO {

    @NotBlank
    private String customerType;

    @NotNull
    private Integer customerSubtypeId;

    @NotBlank
    private String identificationType;

    @NotBlank
    private String identification;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String mobilePhone;

    @NotBlank
    private String address;

    private String firstName;
    private String lastName;
    private LocalDate birthDate;

    private String legalName;
    private LocalDate constitutionDate;
    private Integer legalRepresentativeId;
}
