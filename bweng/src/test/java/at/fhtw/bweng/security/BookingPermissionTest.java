package at.fhtw.bweng.security;

import at.fhtw.bweng.model.Booking;
import at.fhtw.bweng.model.User;
import at.fhtw.bweng.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BookingPermissionTest {

    private BookingPermission bookingPermission;

    @Mock
    private BookingService bookingService;

    @Mock
    private Authentication authentication;

    private UserPrincipal userPrincipal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookingPermission = new BookingPermission(bookingService);
    }

    @Test
    void supports_returnsTrueForBookingClassName() {
        boolean result = bookingPermission.supports(authentication, "at.fhtw.bweng.model.Booking");
        assertThat(result).isTrue();
    }

    @Test
    void supports_returnsFalseForOtherClassNames() {
        boolean result = bookingPermission.supports(authentication, "OtherClass");
        assertThat(result).isFalse();
    }

    @Test
    void hasPermission_returnsTrueForAdminRole() {
        UUID resourceId = UUID.randomUUID();
        userPrincipal = new UserPrincipal(UUID.randomUUID(), "adminUser", "password", "ADMIN");
        when(authentication.getPrincipal()).thenReturn(userPrincipal);

        boolean result = bookingPermission.hasPermission(authentication, resourceId);
        assertThat(result).isTrue();
    }

    @Test
    void hasPermission_returnsTrueForMatchingUserBooking() {
        UUID resourceId = UUID.randomUUID();
        userPrincipal = new UserPrincipal(UUID.randomUUID(), "regularUser", "password", "USER");
        Booking booking = mock(Booking.class);
        User user = mock(User.class);

        when(bookingService.getBookingById(resourceId)).thenReturn(booking);
        when(booking.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(userPrincipal.getId());
        when(authentication.getPrincipal()).thenReturn(userPrincipal);

        boolean result = bookingPermission.hasPermission(authentication, resourceId);
        assertThat(result).isTrue();
    }

    @Test
    void hasPermission_returnsFalseForNonMatchingUserBooking() {
        UUID resourceId = UUID.randomUUID();
        UUID anotherUserId = UUID.randomUUID();
        userPrincipal = new UserPrincipal(anotherUserId, "regularUser", "password", "USER");
        Booking booking = mock(Booking.class);
        User user = mock(User.class);

        when(bookingService.getBookingById(resourceId)).thenReturn(booking);
        when(booking.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(UUID.randomUUID());
        when(authentication.getPrincipal()).thenReturn(userPrincipal);

        boolean result = bookingPermission.hasPermission(authentication, resourceId);
        assertThat(result).isFalse();
    }

    @Test
    void hasPermission_returnsFalseWhenBookingNotFound() {
        UUID resourceId = UUID.randomUUID();
        userPrincipal = new UserPrincipal(UUID.randomUUID(), "regularUser", "password", "USER");

        when(bookingService.getBookingById(resourceId)).thenReturn(null);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);

        boolean result = bookingPermission.hasPermission(authentication, resourceId);
        assertThat(result).isFalse();
    }

    @Test
    void hasPermission_returnsFalseWhenResourceIdIsNullAndNotAdmin() {
        // Arrange
        userPrincipal = new UserPrincipal(UUID.randomUUID(), "regularUser", "password", "USER");
        when(authentication.getPrincipal()).thenReturn(userPrincipal);

        // Act
        boolean result = bookingPermission.hasPermission(authentication, null);

        // Assert
        assertThat(result).isFalse();
    }

}