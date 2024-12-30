package at.fhtw.bweng.repository;

import at.fhtw.bweng.model.Picture;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PictureRepository extends CrudRepository<Picture, UUID> {
}
