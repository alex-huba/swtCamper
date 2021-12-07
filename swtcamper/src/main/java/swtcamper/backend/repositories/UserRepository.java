package swtcamper.backend.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import swtcamper.backend.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
  List<User> findAll();
  Optional<User> findById(Long id);
  Optional<User> findByUsername(String username);
  boolean existsByUsernameAndPassword(String username, String password);
  boolean existsByUsername(String username);
}
