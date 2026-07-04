package ec.edu.espe.banquito.core.party.repository;

import ec.edu.espe.banquito.core.party.model.CoreParameter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoreParameterRepository extends JpaRepository<CoreParameter, String> {
}