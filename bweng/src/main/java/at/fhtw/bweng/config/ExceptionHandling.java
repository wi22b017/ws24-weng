package at.fhtw.bweng.config;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class ExceptionHandling {

    /*@ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException() {
        return "redirect:/books";
    }*/

    // Handle NoSuchElementException (for missing resources)
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleNoSuchElementException() {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Resource not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Handle DataIntegrityViolationException (for duplicate entries or constraint violations)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "A entity with the same data already exists.");
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

}
