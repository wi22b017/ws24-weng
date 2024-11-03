package at.fhtw.bweng.repository;

import at.fhtw.bweng.model.Flight;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface FlightRepository extends CrudRepository<Flight, UUID> {
    @Override
    List<Flight> findAll();
}
