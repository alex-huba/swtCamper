package xtasks.backend.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import xtasks.backend.entities.Offer;
import xtasks.backend.entities.Vehicle;
import java.util.List;

@Repository
public interface OfferRepository extends CrudRepository<Offer, Long> {
    List<Offer> findAll();
}
