package at.fhtw.bweng.repository;

import at.fhtw.bweng.model.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface BookRepository extends CrudRepository<Book, UUID> {
    @Override
    List<Book> findAll();
}
