package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.BookingDto;
import at.fhtw.bweng.model.*;
import at.fhtw.bweng.repository.*;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class BookingService {

    private BookingRepository bookingRepository;
    private PaymentMethodRepository paymentMethodRepository;
    private FlightRepository flightRepository;
    private UserRepository userRepository;
    private PassengerService passengerService;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;


    public BookingService(BookingRepository bookingRepository,
                          PaymentMethodRepository paymentMethodRepository,
                          FlightRepository flightRepository,
                          UserRepository userRepository,
                          PassengerRepository passengerRepository,
                          PassengerService passengerService) {
        this.bookingRepository = bookingRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.flightRepository = flightRepository;
        this.userRepository = userRepository;
        this.passengerService = passengerService;
    }

    public Object getBookings(UUID id) {
        if (id != null) {
            return getBookingById(id);
        } else {
            return getAllBookings();
        }
    }

    //get all bookings
    public List<Booking> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();

        // Get the system's current timezone
        ZoneId systemZone = ZoneId.systemDefault();

        // Convert each booking's bookingDateTime to the system's timezone
        bookings.forEach(booking -> {
            booking.setBookingDate(booking.getBookingDate().atZoneSameInstant(ZoneOffset.UTC).withZoneSameInstant(systemZone).toOffsetDateTime());
        });

        return bookings;

    }

    //get all bookings by userId
    public List<Booking> getBookingsByUserId(UUID userId) {
        if(userId == null){
            throw new IllegalArgumentException("User ID must not be null.");
        }
        List<Booking> bookings =  bookingRepository.findBookingByUserId(userId);
        if(bookings.isEmpty()) {
            throw new NoSuchElementException("No bookings found for user ID " + userId);
        }

        // Get the system's current timezone
        ZoneId systemZone = ZoneId.systemDefault();
        // Convert each booking's bookingDateTime to the system's timezone
        bookings.forEach(booking -> {
            booking.setBookingDate(booking.getBookingDate().atZoneSameInstant(ZoneOffset.UTC).withZoneSameInstant(systemZone).toOffsetDateTime());
        });

        return bookings;
    }

    //get a booking by its id
    public Booking getBookingById(UUID id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Booking with ID " + id + " not found"));

        // Get the system's current timezone
        ZoneId systemZone = ZoneId.systemDefault();
        // Convert each booking's bookingDateTime to the system's timezone
        booking.setBookingDate(booking.getBookingDate().atZoneSameInstant(ZoneOffset.UTC).withZoneSameInstant(systemZone).toOffsetDateTime());

        return booking;
    }

    @Transactional
    public void deleteBooking(UUID id) {
        if (!bookingRepository.existsById(id)) {
            throw new NoSuchElementException("Booking with ID " + id + " not found.");
        }

        // Find passengers associated with this booking and delete them
        List<Passenger> passengers = passengerService.findPassengersByBookingId(id);
        passengers.forEach(passenger -> passengerService.deletePassenger(passenger.getId()));

        // Delete the booking itself
        bookingRepository.deleteById(id);
    }


    @Transactional
    public UUID addBooking(BookingDto bookingDto) {

        Booking booking = new Booking();
        mapBookingDtoToBookingEntity(bookingDto, booking);

        bookingRepository.save(booking); // Persist booking first

        bookingDto.passengers().forEach(passengerDto -> passengerService.addPassenger(passengerDto, booking));

        return booking.getId();
    }


    //update a booking
    @Transactional
    public void updateBooking(UUID id, BookingDto bookingDto) {
        // Retrieve existing booking
        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking with ID " + id + " not found."));

        // Update booking fields
        mapBookingDtoToBookingEntity(bookingDto, existingBooking);

        // Update passengers based on the BookingDto
        bookingDto.passengers().forEach(passengerDto -> {
            if (passengerDto.id() != null) {
                passengerService.updatePassenger(passengerDto.id(), passengerDto);
            } else {
                throw new IllegalArgumentException("Passenger ID must not be null for updating.");
            }
        });

        // Save the updated booking
        bookingRepository.save(existingBooking);
    }

    //helper method to map BookingDto to Booking entity
    private void mapBookingDtoToBookingEntity(BookingDto bookingDto, Booking booking){
        booking.setStatus(bookingDto.status());
        booking.setPrice(bookingDto.price());

        OffsetDateTime bookingDate = OffsetDateTime.parse(bookingDto.bookingDate(), DATE_TIME_FORMATTER);
        booking.setBookingDate(bookingDate);

        User user = userRepository.findById(bookingDto.userId())
                .orElseThrow(() -> new NoSuchElementException("User with ID " + bookingDto.userId() + " not found."));
        booking.setUser(user);
        PaymentMethod paymentMethod = paymentMethodRepository.findById(bookingDto.paymentMethodId())
                .orElseThrow(() -> new NoSuchElementException("Payment Method with ID " + bookingDto.paymentMethodId() + " not found."));
        booking.setPaymentMethod(paymentMethod);
        Flight flight = flightRepository.findById(bookingDto.flightId())
                .orElseThrow(() -> new NoSuchElementException("Flight with ID " + bookingDto.flightId() + " not found."));
        booking.setFlight(flight);
    }

}
