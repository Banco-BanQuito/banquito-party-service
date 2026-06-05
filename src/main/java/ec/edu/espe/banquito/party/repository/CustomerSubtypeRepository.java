package ec.edu.espe.banquito.party.repository;

import ec.edu.espe.banquito.party.model.CustomerSubtype;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerSubtypeRepository extends JpaRepository<CustomerSubtype, Integer> {
}