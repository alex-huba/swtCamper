package swtcamper.backend.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import swtcamper.backend.entities.UserReport;

import java.util.List;

@Repository
public interface UserReportRepository extends CrudRepository<UserReport, Long> {
  List<UserReport> findAll();
}
