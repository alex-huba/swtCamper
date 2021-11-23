package swtcamper.backend.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import swtcamper.backend.entities.Vehicle;

import java.util.List;

@Repository
public interface VehicleRepository extends CrudRepository<Vehicle, Long> {
    List<Vehicle> findAll();
}
