package at.fhtw.bweng.repository;

import at.fhtw.bweng.model.Aircraft;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AircraftRepository extends CrudRepository<Aircraft, UUID> {

    @Override
    List<Aircraft> findAll();

    // Finds an aircraft by its ID
    @Override
    Optional<Aircraft> findById(UUID id);

    // Finds an aircraft by its SerialNumber
    Optional<Aircraft> findBySerialNumber(String serialNumber);

    // Checks if a aircraft exists by its ID
    @Override
    boolean existsById(UUID id);

    // Deletes a aircraft by its ID
    @Override
    void deleteById(UUID id);



}
