package pg.edu.pl.lsea.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pg.edu.pl.lsea.backend.entities.Model;

import java.util.Optional;

/**
 *  Main goal of this repository is to persist Model objects in relational database (ORM)
 */
@Repository
public interface ModelRepo extends JpaRepository<Model, Long> {
    Optional<Model> findByName(String name);
}
