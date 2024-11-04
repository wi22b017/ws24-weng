package at.fhtw.bweng.repository;

import at.fhtw.bweng.model.Aircraft;
import at.fhtw.bweng.model.Airline;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.repository.CrudRepository;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

public interface AircraftRepository extends CrudRepository<Aircraft, UUID> {

    @Override
    List<Aircraft> findAll();

    Optional<Aircraft> findBySerialNumber(String serialNumber);
}
