package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.PassengerDto;
import at.fhtw.bweng.model.Baggage;
import at.fhtw.bweng.model.BaggageType;
import at.fhtw.bweng.model.Passenger;
import at.fhtw.bweng.model.User;
import at.fhtw.bweng.repository.BaggageRepository;
import at.fhtw.bweng.repository.BaggageTypeRepository;
import at.fhtw.bweng.repository.PassengerRepository;
import at.fhtw.bweng.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class PassengerService {

    private PassengerRepository passengerRepository;
    private BaggageRepository baggageRepository;
    private BaggageTypeRepository baggageTypeRepository;

    public PassengerService(PassengerRepository passengerRepository, BaggageRepository baggageRepository, BaggageTypeRepository baggageTypeRepository) {
        this.passengerRepository = passengerRepository;
        this.baggageRepository = baggageRepository;
        this.baggageTypeRepository = baggageTypeRepository;

    }

    public UUID addPassenger(PassengerDto passengerDto) {
        UUID baggageTypeId = passengerDto.baggage().baggageTypeId(); // Access baggageTypeId
        BaggageType baggageType = baggageTypeRepository.findById(baggageTypeId)
                .orElseThrow(() -> new NoSuchElementException("Baggage type with ID " + baggageTypeId + " not found"));

        Baggage baggage = new Baggage();
        baggage.setBaggageType(baggageType);
        baggage = baggageRepository.save(baggage);

        Passenger passenger = new Passenger(
                null,
                passengerDto.firstName(),
                passengerDto.lastName(),
                passengerDto.birthday(),
                passengerDto.seatNumber(),
                baggage
        );

        try {
            return passengerRepository.save(passenger).getId();
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Passenger already exists");
        }
    }



    public List<Passenger> getAllPassengers() {
        return passengerRepository.findAll();
    }

    public Passenger getPassengerById(UUID id) {
        return passengerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Passenger with ID " + id + " not found"));
    }

    public void updatePassenger(UUID id, PassengerDto passengerDto) {
        // Fetch the existing passenger by ID
        Passenger passenger = getPassengerById(id);

        // Fetch the baggage type by ID from the DTO
        UUID baggageTypeId = passengerDto.baggage().baggageTypeId();
        BaggageType baggageType = baggageTypeRepository.findById(baggageTypeId)
                .orElseThrow(() -> new NoSuchElementException("Baggage type with ID " + baggageTypeId + " not found"));

        // Update the passenger details
        passenger.setFirstName(passengerDto.firstName());
        passenger.setLastName(passengerDto.lastName());
        passenger.setBirthday(passengerDto.birthday());
        passenger.setSeatNumber(passengerDto.seatNumber());

        // Update or create the baggage if necessary
        Baggage baggage = passenger.getBaggage();
        if (baggage == null) {
            baggage = new Baggage();
        }
        baggage.setBaggageType(baggageType);
        baggage = baggageRepository.save(baggage);

        passenger.setBaggage(baggage);

        try {
            passengerRepository.save(passenger);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Passenger with the same data already exists.");
        }
    }

    public void deletePassenger(UUID id) {


        if(!passengerRepository.existsById(id)) {
            throw new NoSuchElementException("Passenger with ID " + id + " not found");
        }
        passengerRepository.deleteById(id);


    }

    public List<Passenger> findPassengersByBookingId(UUID bookingId) {
        return passengerRepository.findPassengersByBookingId(bookingId);
    }


}
