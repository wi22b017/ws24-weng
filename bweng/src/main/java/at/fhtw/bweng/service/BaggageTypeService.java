package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.BaggageTypeDto;
import at.fhtw.bweng.model.BaggageType;
import at.fhtw.bweng.repository.BaggageTypeRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class BaggageTypeService {
    private BaggageTypeRepository baggageTypeRepository;

    public BaggageTypeService(BaggageTypeRepository baggageTypeRepository) {
        this.baggageTypeRepository = baggageTypeRepository;
    }

    public Object getBaggageTypes(UUID id, String name) {
        if (name != null) {
            return getBaggageTypeByName(name);
        } else if (id != null) {
            return getBaggageTypeById(id);
        } else {
            return getAllBaggageTypes();
        }
    }

    //get all baggage types
    public List<BaggageType> getAllBaggageTypes(){
        return baggageTypeRepository.findAll();
    }

    //get a baggage type by its id
    public BaggageType getBaggageTypeById(UUID id){
        return baggageTypeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Baggage type with ID " + id + " not found"));
    }

    //get a baggage type by its name
    public BaggageType getBaggageTypeByName(String name){
        return baggageTypeRepository.findByName(name)
                .orElseThrow(() -> new NoSuchElementException("Baggage type with name " + name + " not found"));
    }

    //delete a baggage by its id
    public void deleteBaggageType(UUID id) {
        if(baggageTypeRepository.existsById(id)) {
            baggageTypeRepository.deleteById(id);
        } else {
            throw new NoSuchElementException("Baggage type with ID " + id + " not found");
        }
    }

    //add a baggage type
    public UUID addBaggageType(BaggageTypeDto baggageTypeDto) {
        BaggageType newBaggageType = new BaggageType();
        newBaggageType.setName(baggageTypeDto.name());
        newBaggageType.setFee(baggageTypeDto.fee());
        try {
            return baggageTypeRepository.save(newBaggageType).getId();
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("Baggage Type with the same name already exists.");
        }
    }

    //update a baggage type ny its id
    public void updateBaggageType(UUID id,BaggageTypeDto baggageTypeDto){
        BaggageType existingBaggageType = baggageTypeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Baggage type with ID " + id + " not found"));
        existingBaggageType.setName(baggageTypeDto.name());
        existingBaggageType.setFee(baggageTypeDto.fee());
        baggageTypeRepository.save(existingBaggageType);
    }




}
