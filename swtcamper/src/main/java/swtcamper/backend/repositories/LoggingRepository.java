package swtcamper.backend.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import swtcamper.backend.entities.LoggingMessage;

@Repository
public interface LoggingRepository
  extends CrudRepository<LoggingMessage, Long> {
  List<LoggingMessage> findAll();
}
