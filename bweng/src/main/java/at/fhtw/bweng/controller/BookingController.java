package at.fhtw.bweng.controller;

import at.fhtw.bweng.dto.BookingDto;
import at.fhtw.bweng.model.Booking;
import at.fhtw.bweng.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasPermission(#id, 'at.fhtw.bweng.model.Booking', 'create')")
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
    @PreAuthorize("#id == null ? hasPermission(null, 'at.fhtw.bweng.model.Booking', 'read') : hasPermission(#id, 'at.fhtw.bweng.model.Booking', 'read')")
    public ResponseEntity<?> getBookings(@PathVariable(required = false) UUID id) {
        Object result = bookingService.getBookings(id);
        return ResponseEntity.ok(result);
    }

    //get bookings made by a specific user
    @GetMapping(value = {"/bookings/user/{userId}"})
    @PreAuthorize("hasPermission(#userId, 'at.fhtw.bweng.model.Booking', 'read')")
    public ResponseEntity<?> getBookingsByUserId(@PathVariable UUID userId) {
        List<Booking> bookings = bookingService.getBookingsByUserId(userId);
        return ResponseEntity.ok(bookings);
    }

    @PutMapping("/bookings/{id}")
    @PreAuthorize("hasPermission(#id, 'at.fhtw.bweng.model.Booking', 'update')")
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
    @PreAuthorize("hasPermission(#id, 'at.fhtw.bweng.model.Booking', 'delete')")
    public ResponseEntity<Map<String, String>> deleteBooking(@PathVariable UUID id) {

        bookingService.deleteBooking(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Booking deleted successfully");
        response.put("id", id.toString());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/bookings/{id}/status")
    @PreAuthorize("hasPermission(#id, 'at.fhtw.bweng.model.Booking', 'update')")
    public ResponseEntity<Map<String, String>> updateBookingStatus(
            @PathVariable UUID id,
            @RequestBody Map<String, String> statusUpdate) {
        bookingService.updateBookingStatus(id, statusUpdate);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Booking status updated successfully");
        response.put("id", id.toString());
        return ResponseEntity.ok(response);
    }

}
