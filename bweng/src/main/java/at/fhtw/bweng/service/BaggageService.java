package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.BaggageDto;
import at.fhtw.bweng.model.Baggage;
import at.fhtw.bweng.model.BaggageType;
import at.fhtw.bweng.repository.BaggageRepository;
import at.fhtw.bweng.repository.BaggageTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class BaggageService {

    private BaggageRepository baggageRepository;
    private BaggageTypeRepository baggageTypeRepository;

    public BaggageService(BaggageRepository baggageRepository, BaggageTypeRepository baggageTypeRepository) {
       this.baggageRepository = baggageRepository;
       this.baggageTypeRepository = baggageTypeRepository;
    }

    //get all baggages
    public List<Baggage> getAllBaggages() {
        return baggageRepository.findAll();
    }

    //get list of baggages by type
    public List<Baggage> getBaggagesByType(UUID baggageTypeId) {
        if(baggageTypeId == null) {
            throw new IllegalArgumentException("baggageTypeId must not be null.");
        }
        List<Baggage> baggages = baggageRepository.findByBaggageTypeId(baggageTypeId);
        if(baggages.isEmpty()){
            throw new NoSuchElementException("No baggages found for baggage type ID " + baggageTypeId);
        }
        return baggages;
    }

    //get a baggage by its id
    public Baggage getBaggageById(UUID id) {
        return baggageRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Baggage with ID " + id + " not found"));
    }

    //delete a baggage by its id
    public void deleteBaggage(UUID id){
        if (baggageRepository.existsById(id)) {
            baggageRepository.deleteById(id);
        } else {
            throw new NoSuchElementException("Baggage with ID " + id + " not found.");
        }
    }

    //add a baggage
    public UUID addBaggage(BaggageDto baggageDto) {
        BaggageType baggageType = baggageTypeRepository.findById(baggageDto.baggageTypeId())
                .orElseThrow(() -> new NoSuchElementException("Baggage type with ID " + baggageDto.baggageTypeId() + " not found"));
        Baggage newBaggage = new Baggage();
        newBaggage.setBaggageType(baggageType);
        return baggageRepository.save(newBaggage).getId();
    }


    //update a baggage
    public void updateBaggage(UUID id, BaggageDto baggageDto) {
        Baggage existingBaggage = baggageRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Baggage with ID " + id + " not found"));
        BaggageType baggageType = baggageTypeRepository.findById(baggageDto.baggageTypeId())
                .orElseThrow(() -> new NoSuchElementException("Baggage type with ID " + baggageDto.baggageTypeId() + " not found"));
        existingBaggage.setBaggageType(baggageType);
        baggageRepository.save(existingBaggage);
    }

}
