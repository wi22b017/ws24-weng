package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.BookingDto;
import at.fhtw.bweng.model.Booking;
import at.fhtw.bweng.model.BookingPassenger;
import at.fhtw.bweng.model.Flight;
import at.fhtw.bweng.model.Passenger;
import at.fhtw.bweng.model.PaymentMethod;
import at.fhtw.bweng.model.User;
import at.fhtw.bweng.repository.*;
import org.springframework.stereotype.Service;
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
    private BookingPassengerRepository bookingPassengerRepository;
    private PassengerService passengerService;


    public BookingService(BookingRepository bookingRepository,
                          PaymentMethodRepository paymentMethodRepository,
                          FlightRepository flightRepository,
                          UserRepository userRepository,
                          PassengerRepository passengerRepository,
                          BookingPassengerRepository bookingPassengerRepository,
                          PassengerService passengerService) {
        this.bookingRepository = bookingRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.flightRepository = flightRepository;
        this.userRepository = userRepository;
        this.bookingPassengerRepository = bookingPassengerRepository;
        this.passengerService = passengerService;
    }
    //get all bookings
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
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
        return bookings;
    }

    //get a booking by its id
    public Booking getBookingById(UUID id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Booking with ID " + id + " not found"));
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

    //add a booking
    @Transactional
    public UUID addBooking(BookingDto bookingDto) {
        Booking booking = new Booking();
        mapBookingDtoToBookingEntity(bookingDto, booking);
        bookingRepository.save(booking);

        // Process passengers in bookingDto and create BookingPassenger entries
        List<BookingPassenger> bookingPassengers = bookingDto.passengers().stream()
                .map(passengerDto -> {
                    UUID passengerId = passengerService.addPassenger(passengerDto);
                    Passenger passenger = passengerService.getPassengerById(passengerId);

                    // Create a BookingPassenger entry linking booking and passenger
                    BookingPassenger bookingPassenger = new BookingPassenger();
                    bookingPassenger.setBooking(booking);
                    bookingPassenger.setPassenger(passenger);

                    return bookingPassenger;
                })
                .collect(Collectors.toList());

        // Print the List<BookingPassenger> to the console
        System.out.println("BookingPassenger List before saving: ");
        bookingPassengers.forEach(System.out::println);

        // Save all BookingPassenger entries
        bookingPassengerRepository.saveAll(bookingPassengers);

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
    private Booking mapBookingDtoToBookingEntity(BookingDto bookingDto, Booking booking){
        booking.setStatus(bookingDto.status());
        booking.setPrice(bookingDto.price());
        booking.setBookingDate(bookingDto.bookingDate());
        User user = userRepository.findById(bookingDto.userId())
                .orElseThrow(() -> new NoSuchElementException("User with ID " + bookingDto.userId() + " not found."));
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
