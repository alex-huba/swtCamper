package xtasks.backend.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import xtasks.backend.entities.Person;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {
  List<Person> findAll();
}
