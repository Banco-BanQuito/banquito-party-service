package ec.edu.espe.banquito.core.party.service;

import ec.edu.espe.banquito.core.party.dto.HolidayResponseDTO;
import ec.edu.espe.banquito.core.party.model.Holiday;
import ec.edu.espe.banquito.core.party.repository.HolidayRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HolidayServiceTest {

    @Mock
    private HolidayRepository holidayRepository;

    @InjectMocks
    private HolidayService service;

    @Test
    void findAllReturnsHolidaysSortedByDate() {
        Holiday later = new Holiday(LocalDate.of(2026, 12, 25));
        later.setIsWeekend(false);
        Holiday earlier = new Holiday(LocalDate.of(2026, 1, 1));
        earlier.setIsWeekend(false);
        when(holidayRepository.findAll()).thenReturn(List.of(later, earlier));

        List<HolidayResponseDTO> result = service.findAll();

        assertThat(result.get(0).getHolidayDate()).isEqualTo(LocalDate.of(2026, 1, 1));
    }

    @Test
    void findAllReturnsEmptyListWhenNoHolidaysExist() {
        when(holidayRepository.findAll()).thenReturn(List.of());

        assertThat(service.findAll()).isEmpty();
    }
}
