package at.fhtw.bweng.repository;

import at.fhtw.bweng.model.Flight;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

public interface FlightRepository extends CrudRepository<Flight, UUID> {

    @Override
    List<Flight> findAll();

    // Finds a flight by its ID
    @Override
    Optional<Flight> findById(UUID id);

    // Finds a flight by its unique flightNumber
    Optional<Flight> findByFlightNumber(String flightNumber);

    // Checks if a flight exists by its ID
    @Override
    boolean existsById(UUID id);

    // Deletes a flight by its ID
    @Override
    void deleteById(UUID id);

}
