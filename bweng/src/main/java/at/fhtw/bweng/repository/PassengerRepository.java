package at.fhtw.bweng.repository;

import at.fhtw.bweng.model.Passenger;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PassengerRepository extends CrudRepository<Passenger, UUID> {
    @Override
    List<Passenger> findAll();

    @Override
    Optional<Passenger> findById(UUID id);

    @Override
    boolean existsById(UUID id);

    @Override
    void delete(Passenger passenger);

    @Query("SELECT p FROM Passenger p JOIN p.bookingPassengers bp WHERE bp.booking.id = :bookingId")
    List<Passenger> findPassengersByBookingId(@Param("bookingId") UUID bookingId);

}
