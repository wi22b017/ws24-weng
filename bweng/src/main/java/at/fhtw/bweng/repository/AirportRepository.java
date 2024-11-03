package at.fhtw.bweng.repository;

import at.fhtw.bweng.model.Airport;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

import java.util.UUID;

public interface AirportRepository extends CrudRepository<Airport, UUID> {

    Optional<Airport> findByCode(String code);


}
