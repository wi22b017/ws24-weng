package at.fhtw.bweng.controller;

import at.fhtw.bweng.dto.BookingDto;
import at.fhtw.bweng.model.Booking;
import at.fhtw.bweng.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class BookingController {
    private BookingService bookingService;

    public BookingController(BookingService bookingService) {
    this.bookingService = bookingService;
    }

    @PostMapping("/bookings")
    public ResponseEntity<Map<String, String>> addBookingNew(@RequestBody @Valid BookingDto bookingDto) {
        UUID uuid = bookingService.addBooking(bookingDto);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Booking created successfully");
        response.put("id", uuid.toString());

        return ResponseEntity
                .created(URI.create("/bookings/" + uuid))
                .body(response);
    }

    @GetMapping(value = {"/bookings", "/bookings/{id}"})
    public ResponseEntity<?> getBookings(@PathVariable(required = false) UUID id) {
        if (id != null) {
            Booking booking = bookingService.getBookingById(id);
            return ResponseEntity.ok(booking);
        } else {
            List<Booking> bookings = bookingService.getAllBookings();
            return ResponseEntity.ok(bookings);
        }
    }

    //get bookings made by a specific user
    @GetMapping(value = {"/bookings/user/{userId}"})
    public ResponseEntity<?> getBookingsByUserId(@PathVariable UUID userId) {
        List<Booking> bookings = bookingService.getBookingsByUserId(userId);
        return ResponseEntity.ok(bookings);
    }

    @PutMapping("/bookings/{id}")
    public ResponseEntity<Map<String, String>> updateBooking(
            @PathVariable UUID id,
            @RequestBody @Valid BookingDto bookingDto) {

        bookingService.updateBooking(id, bookingDto);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Booking updated successfully");
        response.put("id", id.toString());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<Map<String, String>> deleteBooking(@PathVariable UUID id) {

        bookingService.deleteBooking(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Booking deleted successfully");
        response.put("id", id.toString());
        return ResponseEntity.ok(response);
    }
}
