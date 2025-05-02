package pg.edu.pl.lsea.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pg.edu.pl.lsea.backend.entities.Aircraft;
import pg.edu.pl.lsea.backend.entities.Flight;

import java.util.Optional;

/**
 *  Main goal of this repository is to persist Flight objects in relational database (ORM)
 */
@Repository
public interface FlightRepo extends JpaRepository<Flight, Long> {
    Optional<Flight> findByIcao24(String icao24);
}
