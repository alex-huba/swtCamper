package xtasks.backend.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import xtasks.backend.entities.Task;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {
  List<Task> findAll();
}