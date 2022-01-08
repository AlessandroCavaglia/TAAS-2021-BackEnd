package esameTAAS.productMicroservice.Repositories;


import esameTAAS.productMicroservice.Models.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match,Long> {
}
