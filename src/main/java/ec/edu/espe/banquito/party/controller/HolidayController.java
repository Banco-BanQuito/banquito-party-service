package ec.edu.espe.banquito.party.controller;

import ec.edu.espe.banquito.party.dto.HolidayResponseDTO;
import ec.edu.espe.banquito.party.service.HolidayService;
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