package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.BaggageDto;
import at.fhtw.bweng.dto.BookingDto;
import at.fhtw.bweng.model.Baggage;
import at.fhtw.bweng.model.Booking;
import at.fhtw.bweng.model.Flight;
import at.fhtw.bweng.model.PaymentMethod;
import at.fhtw.bweng.repository.BookingRepository;
import at.fhtw.bweng.repository.FlightRepository;
import at.fhtw.bweng.repository.PaymentMethodRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private BookingRepository bookingRepository;
    private PaymentMethodRepository paymentMethodRepository;
    private FlightRepository flightRepository;


    public BookingService(BookingRepository bookingRepository,
                          PaymentMethodRepository paymentMethodRepository,
                          FlightRepository flightRepository) {
        this.bookingRepository = bookingRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.flightRepository = flightRepository;
    }
    //get all bookings
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    //get a booking by its id
    public Optional<Booking> getBookingById(UUID id) {
        return bookingRepository.findById(id);
    }

    //delete a booking by its id
    public void deleteBooking(UUID id) {
        if (bookingRepository.existsById(id)) {
            bookingRepository.deleteById(id);
        } else {
            throw new RuntimeException("Booking with ID " + id + " not found.");
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
        booking.setSeatNumber(bookingDto.seatNumber());
        booking.setBookingDate(bookingDto.bookingDate());

        PaymentMethod paymentMethod = paymentMethodRepository.findById(bookingDto.paymentMethodId())
                .orElseThrow(() -> new RuntimeException("Payment Method with ID " + bookingDto.paymentMethodId() + " not found."));
        booking.setPaymentMethod(paymentMethod);

        Flight flight = flightRepository.findById(bookingDto.flightId())
                .orElseThrow(() -> new RuntimeException("Flight with ID " + bookingDto.flightId() + " not found."));
        booking.setFlight(flight);

        if (bookingDto.baggages() != null) {
            booking.setBaggages(bookingDto.baggages().stream()
                    .map(baggageDto -> mapBaggageDtoToBaggageEntity(baggageDto, booking))
                    .collect(Collectors.toList()));
        }
        return booking;
    }

    private Baggage mapBaggageDtoToBaggageEntity(BaggageDto baggageDto, Booking booking) {
        Baggage baggage = new Baggage();
        baggage.setType(baggageDto.type());
        baggage.setFee(baggageDto.fee());
        baggage.setWeight(baggageDto.weight());
        baggage.setLength(baggageDto.length());
        baggage.setWidth(baggageDto.width());
        baggage.setHeight(baggageDto.height());
        baggage.setBooking(booking);
        return baggage;
    }

}
