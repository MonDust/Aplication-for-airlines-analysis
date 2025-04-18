package pg.edu.pl.lsea.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pg.edu.pl.lsea.backend.entities.Aircraft;

@Repository
public interface AircraftRepo extends JpaRepository<Aircraft, String> {
}
