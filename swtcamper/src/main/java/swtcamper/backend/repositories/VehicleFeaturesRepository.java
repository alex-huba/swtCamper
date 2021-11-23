package swtcamper.backend.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import swtcamper.backend.entities.VehicleFeatures;

@Repository
public interface VehicleFeaturesRepository
  extends CrudRepository<VehicleFeatures, Long> {
  List<VehicleFeatures> findAll();
}
