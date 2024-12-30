package at.fhtw.bweng.repository;

import at.fhtw.bweng.model.Airport;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AirportRepository extends CrudRepository<Airport, UUID> {

    // Default method to find all airports
    @Override
    List<Airport> findAll();

    // Finds an airport by its ID
    @Override
    Optional<Airport> findById(UUID id);

    // Finds an airport by its unique code
    Optional<Airport> findByCode(String code);

    // Checks if an airport exists by its ID
    @Override
    boolean existsById(UUID id);

    // Deletes an airport by its ID
    @Override
    void deleteById(UUID id);




}
