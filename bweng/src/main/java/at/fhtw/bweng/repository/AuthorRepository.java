package at.fhtw.bweng.repository;

import at.fhtw.bweng.model.Author;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface AuthorRepository extends CrudRepository<Author, UUID> {
}
