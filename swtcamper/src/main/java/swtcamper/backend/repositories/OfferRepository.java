package swtcamper.backend.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import swtcamper.backend.entities.Offer;

@Repository
public interface OfferRepository extends CrudRepository<Offer, Long> {
  List<Offer> findAll();
}
