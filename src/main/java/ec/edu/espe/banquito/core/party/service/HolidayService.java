package ec.edu.espe.banquito.core.party.service;

import ec.edu.espe.banquito.core.party.dto.HolidayResponseDTO;
import ec.edu.espe.banquito.core.party.mapper.HolidayMapper;
import ec.edu.espe.banquito.core.party.model.Holiday;
import ec.edu.espe.banquito.core.party.repository.HolidayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HolidayService {

    private final HolidayRepository holidayRepository;

    public List<HolidayResponseDTO> findAll() {
        return this.holidayRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Holiday::getHolidayDate))
                .map(HolidayMapper::toResponse)
                .toList();
    }
}
