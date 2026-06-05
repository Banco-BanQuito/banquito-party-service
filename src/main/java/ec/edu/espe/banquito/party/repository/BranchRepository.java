package ec.edu.espe.banquito.party.repository;

import ec.edu.espe.banquito.party.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, Integer> {

    Optional<Branch> findByBranchCode(String branchCode);
}