package at.fhtw.bweng.controller;

import at.fhtw.bweng.dto.BookDto;
import at.fhtw.bweng.model.Book;
import at.fhtw.bweng.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
public class BookController {

    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public List<Book> getBooks() {
        return bookService.getAllBooks();
    }

//    @GetMapping("/books/{id}")
//    public Book getBook(@PathVariable UUID id) {
//        return bookService.getBook(id);
//    }
//
    @PostMapping("/books")
    public ResponseEntity<Book> addBook(@RequestBody @Valid BookDto bookDto) {
        UUID uuid = bookService.addBook(bookDto);
        return ResponseEntity
                .created(URI.create("/books/" + uuid))
                .build();
    }
//
//    @PatchMapping("/books/{id}")
//    public ResponseEntity<Book> patchBook(@PathVariable UUID id,
//                                          @RequestBody BookDto bookDto) {
//        return ResponseEntity.ok(bookService.patchBook(id, bookDto));
//    }
//
//    @PutMapping("/books/{id}")
//    public ResponseEntity<Book> updateBook(@PathVariable UUID id,
//                                           @RequestBody @Valid BookDto bookDto) {
//        return ResponseEntity.ok(bookService.patchBook(id, bookDto));
//    }
//
//    @DeleteMapping("/books/{id}")
//    public ResponseEntity<Void> deleteBook(@PathVariable UUID id) {
//        bookService.removeBook(id);
//        return ResponseEntity.ok().build();
//    }
}
