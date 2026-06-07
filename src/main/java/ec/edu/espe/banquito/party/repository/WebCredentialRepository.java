package ec.edu.espe.banquito.party.repository;

import ec.edu.espe.banquito.party.model.WebCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WebCredentialRepository extends JpaRepository<WebCredential, Integer> {

    Optional<WebCredential> findByUsername(String username);
}