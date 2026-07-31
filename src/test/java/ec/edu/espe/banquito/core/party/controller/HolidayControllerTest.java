package ec.edu.espe.banquito.core.party.controller;

import ec.edu.espe.banquito.core.party.dto.HolidayResponseDTO;
import ec.edu.espe.banquito.core.party.service.HolidayService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HolidayControllerTest {

    @Mock
    private HolidayService holidayService;

    @Test
    void getHolidaysDelegatesToService() {
        HolidayResponseDTO holiday = new HolidayResponseDTO(LocalDate.of(2026, 1, 1), "Ano Nuevo", false);
        when(holidayService.findAll()).thenReturn(List.of(holiday));

        HolidayController controller = new HolidayController(holidayService);

        assertThat(controller.getHolidays()).containsExactly(holiday);
    }
}
