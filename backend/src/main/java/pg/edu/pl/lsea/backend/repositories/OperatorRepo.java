package pg.edu.pl.lsea.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pg.edu.pl.lsea.backend.entities.Operator;

import java.util.Optional;

/**
 *  Main goal of this repository is to persist Operator objects in relational database (ORM)
 */
@Repository
public interface OperatorRepo extends JpaRepository<Operator, Long> {
    Optional<Operator> findByName(String name);
}
