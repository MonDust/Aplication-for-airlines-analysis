package pg.edu.pl.lsea.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pg.edu.pl.lsea.backend.entities.EnrichedFlight;
import pg.edu.pl.lsea.backend.entities.Flight;

/**
 *  Main goal of this repository is to persist Flight objects in relational database (ORM)
 */
@Repository
public interface EnrichedFlightRepo extends JpaRepository<EnrichedFlight, String> {

}
