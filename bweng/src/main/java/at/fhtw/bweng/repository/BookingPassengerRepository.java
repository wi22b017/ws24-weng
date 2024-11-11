package at.fhtw.bweng.repository;

import at.fhtw.bweng.model.BookingPassenger;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface BookingPassengerRepository extends CrudRepository<BookingPassenger, UUID>{

    void deleteByBookingId(UUID id);

}
