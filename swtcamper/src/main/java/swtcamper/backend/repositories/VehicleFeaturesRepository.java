package xtasks.backend.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import xtasks.backend.entities.VehicleFeatures;

import java.util.List;

@Repository
public interface VehicleFeaturesRepository extends CrudRepository<VehicleFeatures, Long> {
    List<VehicleFeatures> findAll();
}