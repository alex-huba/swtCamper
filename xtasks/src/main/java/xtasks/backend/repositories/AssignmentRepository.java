package xtasks.backend.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import xtasks.backend.entities.Assignment;
import xtasks.backend.entities.Person;
import xtasks.backend.entities.Task;

@Repository
public interface AssignmentRepository extends CrudRepository<Assignment, Long> {
  List<Assignment> findAll();

  List<Assignment> findByPerson(Person person);

  List<Assignment> findByTask(Task task);

  List<Assignment> findByPersonAndTask(Person person, Task task);
}
