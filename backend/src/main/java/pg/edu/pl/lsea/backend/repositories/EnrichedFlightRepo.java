package pg.edu.pl.lsea.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pg.edu.pl.lsea.backend.entities.EnrichedFlight;

import java.util.Optional;

/**
 *  Main goal of this repository is to persist EnrichedFlight objects in relational database (ORM)
 */
@Repository
public interface EnrichedFlightRepo extends JpaRepository<EnrichedFlight, Long> {
    Optional<EnrichedFlight> findByIcao24(String icao24);
}
