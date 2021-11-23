package swtcamper.backend.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import swtcamper.backend.entities.Offer;

import java.util.List;

@Repository
public interface OfferRepository extends CrudRepository<Offer, Long> {
    List<Offer> findAll();
}
