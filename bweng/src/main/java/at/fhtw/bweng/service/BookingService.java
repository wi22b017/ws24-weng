package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.BookingDto;
import at.fhtw.bweng.model.Booking;
import at.fhtw.bweng.model.Flight;
import at.fhtw.bweng.model.PaymentMethod;
import at.fhtw.bweng.model.User;
import at.fhtw.bweng.repository.BookingRepository;
import at.fhtw.bweng.repository.FlightRepository;
import at.fhtw.bweng.repository.PaymentMethodRepository;
import at.fhtw.bweng.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class BookingService {

    private BookingRepository bookingRepository;
    private PaymentMethodRepository paymentMethodRepository;
    private FlightRepository flightRepository;
    private UserRepository userRepository;


    public BookingService(BookingRepository bookingRepository,
                          PaymentMethodRepository paymentMethodRepository,
                          FlightRepository flightRepository,UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.flightRepository = flightRepository;
        this.userRepository = userRepository;
    }
    //get all bookings
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    //get a booking by its id
    public Booking getBookingById(UUID id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Booking with ID " + id + " not found"));
    }

    //delete a booking by its id
    public void deleteBooking(UUID id) {
        if (bookingRepository.existsById(id)) {
            bookingRepository.deleteById(id);
        } else {
            throw new NoSuchElementException("Booking with ID " + id + " not found.");
        }
    }

    //add a booking
    public UUID addBooking(BookingDto bookingDto) {
        Booking newBooking = new Booking();
        mapBookingDtoToBookingEntity(bookingDto, newBooking);
        return bookingRepository.save(newBooking).getId();
    }
    //update a booking
    public void updateBooking(UUID id, BookingDto bookingDto) {
        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking with ID" + id +"not found."));
        mapBookingDtoToBookingEntity(bookingDto, existingBooking);
        bookingRepository.save(existingBooking);
    }

    //helper method to map BookingDto to Booking entity
    private Booking mapBookingDtoToBookingEntity(BookingDto bookingDto, Booking booking){
        booking.setStatus(bookingDto.status());
        booking.setPrice(bookingDto.price());
        booking.setBookingDate(bookingDto.bookingDate());
        User user = userRepository.findById(bookingDto.userId())
                .orElseThrow(() -> new NoSuchElementException("Payment Method with ID " + bookingDto.userId() + " not found."));
        booking.setUser(user);
        PaymentMethod paymentMethod = paymentMethodRepository.findById(bookingDto.paymentMethodId())
                .orElseThrow(() -> new NoSuchElementException("Payment Method with ID " + bookingDto.paymentMethodId() + " not found."));
        booking.setPaymentMethod(paymentMethod);
        Flight flight = flightRepository.findById(bookingDto.flightId())
                .orElseThrow(() -> new NoSuchElementException("Flight with ID " + bookingDto.flightId() + " not found."));
        booking.setFlight(flight);

        return booking;
    }
}
