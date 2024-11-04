package at.fhtw.bweng.repository;

import at.fhtw.bweng.model.Airline;
import at.fhtw.bweng.model.Airport;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

public interface AirlineRepository extends CrudRepository<Airline, UUID> {

    @Override
    List<Airline> findAll();

    Optional<Airline> findByName(String name);
}
