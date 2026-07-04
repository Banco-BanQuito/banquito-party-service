package ec.edu.espe.banquito.core.party.enums;

import lombok.Getter;

@Getter
public enum CustomerStatusEnum {

    ACTIVO("ACTIVO"),
    INACTIVO("INACTIVO"),
    BLOQUEADO("BLOQUEADO"),
    SUSPENDIDO("SUSPENDIDO");

    private final String value;

    CustomerStatusEnum(String value) {
        this.value = value;
    }
}