package ec.edu.espe.banquito.core.party.repository;

import ec.edu.espe.banquito.core.party.model.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface HolidayRepository extends JpaRepository<Holiday, LocalDate> {
}