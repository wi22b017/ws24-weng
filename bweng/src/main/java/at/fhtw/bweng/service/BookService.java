package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.BookDto;
import at.fhtw.bweng.model.Author;
import at.fhtw.bweng.model.Book;
import at.fhtw.bweng.repository.AuthorRepository;
import at.fhtw.bweng.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BookService {

    private BookRepository bookRepository;
    private AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository,
                       AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

//    public Book getBook(UUID id) {
//        return books.stream()
//                .filter(book -> book.getId().equals(id))
//                .findFirst()
//                .orElseThrow();
//    }
//
    public UUID addBook(BookDto bookDto) {
        Author author = new Author(bookDto.author().firstName(),
                bookDto.author().lastName());
        authorRepository.save(author);
        Book book = new Book(bookDto.title(),
                author,
                bookDto.releaseYear());
        bookRepository.save(book);
        return book.getId();
    }

//    public Book patchBook(UUID id, BookDto bookDto) {
//        Book book = getBook(id);
//        if (bookDto.title() != null) {
//            book.setTitle(bookDto.title());
//        }
//        if (bookDto.author() != null) {
//            book.setAuthor(bookDto.author());
//        }
//        if (bookDto.releaseYear() > 0) {
//            book.setReleaseYear(bookDto.releaseYear());
//        }
//
//        return book;
//    }
//
//    public void removeBook(UUID id) {
//        books.removeIf(book -> book.getId().equals(id));
//    }
}
