package swtcamper.backend.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import swtcamper.backend.entities.Picture;

@Repository
public interface PictureRepository extends CrudRepository<Picture, Long> {
    List<Picture> findAll();
}
