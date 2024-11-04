package at.fhtw.bweng.repository;

import at.fhtw.bweng.model.Aircraft;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AircraftRepository extends CrudRepository<Aircraft, UUID> {

    @Override
    List<Aircraft> findAll();

    Optional<Aircraft> findBySerialNumber(String serialNumber);
}
