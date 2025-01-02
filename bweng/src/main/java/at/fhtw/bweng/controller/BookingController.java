package at.fhtw.bweng.controller;

import at.fhtw.bweng.dto.BookingDto;
import at.fhtw.bweng.model.Booking;
import at.fhtw.bweng.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Object result = bookingService.getBookings(id);
        return ResponseEntity.ok(result);
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

    @PatchMapping("/bookings/{id}/status")
    public ResponseEntity<Map<String, String>> updateBookingStatus(
            @PathVariable UUID id,
            @RequestBody Map<String, String> statusUpdate) {
        // Validate the request body to ensure 'status' is provided
        if (!statusUpdate.containsKey("status")) {
            throw new IllegalArgumentException("Status field is required.");
        }
        // Extract the new status
        String newStatus = statusUpdate.get("status");

        //users can cancel their bookings, but admins can set any status
        if (!"Cancelled".equalsIgnoreCase(newStatus)) {
            ensureAdminRole();
        }

        bookingService.updateBookingStatus(id, newStatus);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Booking status updated successfully");
        response.put("id", id.toString());
        response.put("newStatus", newStatus);
        return ResponseEntity.ok(response);
    }

    private void ensureAdminRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            throw new SecurityException("Only administrators are allowed to change the status field.");
        }
    }
}
