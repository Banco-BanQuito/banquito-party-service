package ec.edu.espe.banquito.core.party.mapper;

import ec.edu.espe.banquito.core.party.dto.HolidayResponseDTO;
import ec.edu.espe.banquito.core.party.model.Holiday;

public class HolidayMapper {

    private HolidayMapper() {
    }

    public static HolidayResponseDTO toResponse(Holiday holiday) {
        return new HolidayResponseDTO(
                holiday.getHolidayDate(),
                holiday.getName(),
                holiday.getIsWeekend()
        );
    }
}
