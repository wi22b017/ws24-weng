package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.BaggageDto;
import at.fhtw.bweng.model.Baggage;
import at.fhtw.bweng.model.Booking;
import at.fhtw.bweng.repository.BaggageRepository;
import at.fhtw.bweng.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BaggageService {

    private BaggageRepository baggageRepository;
    private BookingRepository bookingRepository;

    public BaggageService(BaggageRepository baggageRepository, BookingRepository bookingRepository) {
        this.baggageRepository = baggageRepository;
        this.bookingRepository = bookingRepository;
    }

    //get all baggages
    public List<Baggage> getAllBaggages() {
        return baggageRepository.findAll();
    }

    //get a baggage by its id
    public Optional<Baggage> getBaggageById(UUID id) {
        return baggageRepository.findById(id);
    }

    //delete a baggage by its id
    public void deleteBaggage(UUID id){
        if (baggageRepository.existsById(id)) {
            baggageRepository.deleteById(id);
        } else {
            throw new RuntimeException("Baggage with ID " + id + " not found.");
        }
    }

    //add a baggage
    public UUID addBaggage(BaggageDto baggageDto) {
        Baggage newBaggage = new Baggage();
        mapBaggageDtoToBaggageEntity(baggageDto,newBaggage);
        return baggageRepository.save(newBaggage).getId();
    }


    //update a baggage
    public void updateBaggage(UUID id, BaggageDto baggageDto) {
        Baggage existingBaggage = baggageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Baggage with ID" + id+"not found."));
        mapBaggageDtoToBaggageEntity(baggageDto,existingBaggage);
        baggageRepository.save(existingBaggage);
    }

    // helper method to map BaggageDto to Baggage entity
    private Baggage mapBaggageDtoToBaggageEntity(BaggageDto baggageDto, Baggage baggage) {
        baggage.setType(baggageDto.type());
        baggage.setFee(baggageDto.fee());
        baggage.setWeight(baggageDto.weight());
        baggage.setLength(baggageDto.length());
        baggage.setWidth(baggageDto.width());
        baggage.setHeight(baggageDto.height());

        // Fetch the associated booking entity
        Booking booking = bookingRepository.findById(baggageDto.bookingId())
                .orElseThrow(() -> new RuntimeException("Booking with ID " + baggageDto.bookingId() + " not found."));
        baggage.setBooking(booking);

        return baggage;
    }
}
