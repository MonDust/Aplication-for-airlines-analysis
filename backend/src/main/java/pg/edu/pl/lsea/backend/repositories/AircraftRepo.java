package pg.edu.pl.lsea.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pg.edu.pl.lsea.backend.entities.Aircraft;

import java.util.Optional;

/**
 *  Main goal of this repository is to persist Aircraft objects in relational database (ORM)
 */
@Repository
public interface AircraftRepo extends JpaRepository<Aircraft, Long> {
    Optional<Aircraft> findByIcao24(String icao24);
}
