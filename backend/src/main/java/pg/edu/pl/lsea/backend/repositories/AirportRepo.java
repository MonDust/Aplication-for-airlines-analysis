package pg.edu.pl.lsea.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pg.edu.pl.lsea.backend.entities.Airport;

import java.util.Optional;

/**
 *  Main goal of this repository is to persist Airport objects in relational database (ORM)
 */
@Repository
public interface AirportRepo extends JpaRepository<Airport, Long> {
    Optional<Airport> findByCode(String code);
}
