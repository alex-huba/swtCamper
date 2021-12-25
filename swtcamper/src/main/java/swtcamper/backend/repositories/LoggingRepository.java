package swtcamper.backend.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import swtcamper.backend.entities.LoggingMessage;

import java.util.List;

@Repository
public interface LoggingRepository extends CrudRepository<LoggingMessage, Long> {
    List<LoggingMessage> findAll();
}
