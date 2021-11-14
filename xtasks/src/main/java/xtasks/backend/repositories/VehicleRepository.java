package xtasks.backend.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import xtasks.backend.entities.Vehicle;

import java.util.List;

@Repository
public interface VehicleRepository extends CrudRepository<Vehicle, Long> {
    List<Vehicle> findAll();
}
