package pg.edu.pl.lsea.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pg.edu.pl.lsea.backend.entities.Aircraft;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 *  Main goal of this repository is to persist Aircraft objects in relational database (ORM)
 */
@Repository
public interface AircraftRepo extends JpaRepository<Aircraft, Long> {
    Optional<Aircraft> findByIcao24(String icao24);

    List<Aircraft> findByIcao24In(Collection<String> icao24List);

    @Query("SELECT a.icao24 FROM Aircraft a")
    Set<String> findAllIcao24();
}
