package swtcamper.backend.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import swtcamper.backend.entities.Picture;

import java.util.List;

@Repository
public interface PictureRepository extends CrudRepository<Picture, Long> {
  List<Picture> findAll();
}
