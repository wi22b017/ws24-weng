package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.PassengerDto;
import at.fhtw.bweng.model.Baggage;
import at.fhtw.bweng.model.Passenger;
import at.fhtw.bweng.model.User;
import at.fhtw.bweng.repository.BaggageRepository;
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
    private UserRepository userRepository;
    private BaggageRepository baggageRepository;

    public PassengerService(PassengerRepository passengerRepository, UserRepository userRepository, BaggageRepository baggageRepository) {
        this.passengerRepository = passengerRepository;
        this.userRepository = userRepository;
        this.baggageRepository = baggageRepository;
    }

    public UUID addPassenger(PassengerDto passengerDto) {
        Baggage baggage = baggageRepository.findById(passengerDto.baggageId())
                .orElseThrow(() -> new NoSuchElementException("Baggage with ID " + passengerDto.baggageId() + " not found"));
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
        Passenger passenger = getPassengerById(id);

        Baggage baggage = baggageRepository.findById(passengerDto.baggageId())
                .orElseThrow(() -> new NoSuchElementException("Baggage with ID " + passengerDto.baggageId() + " not found"));

        passenger.setFirstName(passengerDto.firstName());
        passenger.setLastName(passengerDto.lastName());
        passenger.setBirthday(passengerDto.birthday());
        passenger.setSeatNumber(passengerDto.seatNumber());

        try{
            passengerRepository.save(passenger);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Passenger with the same data already exists.");
        } catch (NoSuchElementException e){
            throw new NoSuchElementException("Passenger with ID " + id + " not found");
        }

    }

    public void deletePassenger(UUID id) {


        if(!passengerRepository.existsById(id)) {
            throw new NoSuchElementException("Passenger with ID " + id + " not found");
        }
        passengerRepository.deleteById(id);


    }
}
