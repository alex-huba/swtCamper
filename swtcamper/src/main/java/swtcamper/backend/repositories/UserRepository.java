package swtcamper.backend.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import swtcamper.backend.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
  List<User> findAll();
}
