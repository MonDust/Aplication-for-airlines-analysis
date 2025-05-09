package pg.edu.pl.lsea.backend.repositories.original;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pg.edu.pl.lsea.backend.entities.original.Flight;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface FlightRepo extends JpaRepository<Flight, Long> {
    List<Flight> findAllByIcao24(String icao24);

    // Find flights within a time window
    List<Flight> findByFirstSeenGreaterThanEqualAndLastSeenLessThanEqual(int start, int end);

    void deleteByFirstSeenGreaterThanEqualAndLastSeenLessThanEqual(int start, int end);

    Optional<Flight> findByIcao24AndFirstSeen(String icao24, int firstSeen);

    List<Flight> findAllByIcao24In(Set<String> icao24s);
}
