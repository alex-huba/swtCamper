package swtcamper.backend.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import swtcamper.backend.entities.Vehicle;

@Repository
public interface VehicleRepository extends CrudRepository<Vehicle, Long> {
  List<Vehicle> findAll();
}
