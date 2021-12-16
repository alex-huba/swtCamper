package swtcamper.backend.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import swtcamper.backend.entities.Booking;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Long> {
  // TODO: implement Booking repository
}
