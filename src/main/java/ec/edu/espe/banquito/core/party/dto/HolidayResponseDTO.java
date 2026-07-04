package ec.edu.espe.banquito.core.party.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class HolidayResponseDTO {

    private LocalDate holidayDate;
    private String name;
    private Boolean isWeekend;

    public HolidayResponseDTO() {
    }

    public HolidayResponseDTO(LocalDate holidayDate, String name, Boolean isWeekend) {
        this.holidayDate = holidayDate;
        this.name = name;
        this.isWeekend = isWeekend;
    }
}