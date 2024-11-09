package at.fhtw.bweng.repository;

import at.fhtw.bweng.model.Airline;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AirlineRepository extends CrudRepository<Airline, UUID> {

    @Override
    List<Airline> findAll();

    @Override
    Optional<Airline> findById(UUID id);

    Optional<Airline> findByName(String name);

    @Override
    boolean existsById(UUID id);

    @Override
    void deleteById(UUID id);
}
