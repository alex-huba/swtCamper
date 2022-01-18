package swtcamper.backend.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import swtcamper.backend.entities.UserReport;

@Repository
public interface UserReportRepository extends CrudRepository<UserReport, Long> {
  List<UserReport> findAll();
}
