package ec.edu.espe.banquito.party.repository;

import ec.edu.espe.banquito.party.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Optional<Customer> findByIdentification(String identification);
}