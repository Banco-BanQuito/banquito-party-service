package ec.edu.espe.banquito.core.party.controller;

import ec.edu.espe.banquito.core.party.dto.HolidayResponseDTO;
import ec.edu.espe.banquito.core.party.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/holidays")
@RequiredArgsConstructor
public class HolidayController {

    private final HolidayService holidayService;

    @GetMapping
    public List<HolidayResponseDTO> getHolidays() {
        return this.holidayService.findAll();
    }
}