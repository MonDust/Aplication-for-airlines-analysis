package pg.edu.pl.lsea.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pg.edu.pl.lsea.backend.entities.Airport;
import pg.edu.pl.lsea.backend.entities.Route;

import java.util.Optional;


import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface RouteRepo extends JpaRepository<Route, Long> {
    Optional<Route> findByOriginAndDestination(Airport origin, Airport destination);

    @Query("SELECT r FROM Route r JOIN FETCH r.origin JOIN FETCH r.destination")
    List<Route> findAllWithAirports();

}
