package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.AircraftDto;
import at.fhtw.bweng.model.Aircraft;
import at.fhtw.bweng.model.Airline;
import at.fhtw.bweng.repository.AircraftRepository;
import at.fhtw.bweng.repository.AirlineRepository;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.NoSuchElementException;

@Service
public class AircraftService {

    private AircraftRepository aircraftRepository;
    private AirlineRepository airlineRepository;


    public AircraftService(AircraftRepository aircraftRepository, AirlineRepository airlineRepository) {
        this.aircraftRepository = aircraftRepository;
        this.airlineRepository = airlineRepository;
    }

    public UUID addAircraft(AircraftDto aircraftDto) {

        // Find or create the airline
        Airline airline = airlineRepository.findByName(aircraftDto.airline().name())
                .orElseGet(() -> {
                    Airline newAirline = new Airline(null, aircraftDto.airline().name());
                    return airlineRepository.save(newAirline);
                });

        Aircraft aircraft = new Aircraft(null, aircraftDto.serialNumber(), aircraftDto.manufacturer(), aircraftDto.model(), aircraftDto.capacity(), airline);

        try {
            return aircraftRepository.save(aircraft).getId();
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("Aircraft with the same data already exists.");
        }
    }

    public List<Aircraft> getAllAircrafts(){
        return aircraftRepository.findAll();
    }


    public Aircraft getAircraftById(UUID id) {
        return aircraftRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Aircraft with ID "+id+" not found"));
    }

    public Aircraft getAircraftBySerialNumber(String serialNumber) {
        return aircraftRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new NoSuchElementException("Aircraft with serial number "+serialNumber+" not found"));
    }

    public void updateAircraft(UUID id, @Valid AircraftDto aircraftDto) {

        Aircraft aircraft = getAircraftById(id);

        aircraft.setManufacturer(aircraftDto.manufacturer());
        aircraft.setModel(aircraftDto.model());
        aircraft.setCapacity(aircraftDto.capacity());

        // Find or create the airline
        Airline airline = airlineRepository.findByName(aircraftDto.airline().name())
                .orElseGet(() -> {
                    Airline newAirline = new Airline(null, aircraftDto.airline().name());
                    return airlineRepository.save(newAirline);
                });

        aircraft.setAirline(airline);
        aircraft.setSerialNumber(aircraftDto.serialNumber());

        try {
            aircraftRepository.save(aircraft);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("Aircraft with the same data already exists.");
        }
    }

    public void deleteAircraft(UUID id) {
        // Check if aircraft exists; if not, throw NoSuchElementException
        if(!aircraftRepository.existsById(id)){
            throw new NoSuchElementException("Aircraft with ID "+id+" not found");
        }
        aircraftRepository.deleteById(id);
    }
}
