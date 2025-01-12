package at.fhtw.bweng.security;

import at.fhtw.bweng.model.Booking;
import at.fhtw.bweng.service.BookingService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BookingPermission implements AccessPermission {
    private final BookingService bookingService;

    public BookingPermission(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Override
    public boolean supports(Authentication authentication, String className) {
        // Supports the Booking class
        return className.equals("at.fhtw.bweng.model.Booking");
    }

    @Override
    public boolean hasPermission(Authentication authentication, UUID resourceId) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        // Admins have access to all resources
        if (principal.getRole().equals("ADMIN")) {
            return true;
        }

        // For bookings, check if the user owns the booking
        if (resourceId != null) {
            Booking booking = bookingService.getBookingById(resourceId);

            // Allow access if the authenticated user owns the booking
            return booking != null && booking.getUser().getId().equals(principal.getId());
        }

        return false;
    }
}
