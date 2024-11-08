package at.fhtw.bweng.repository;

import at.fhtw.bweng.model.Booking;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends CrudRepository<Booking, UUID> {

    @Override
    List<Booking> findAll();

    @Override
    Optional<Booking> findById(UUID id);

    @Override
    boolean existsById(UUID id);

    @Override
    void deleteById(UUID uuid);

}
