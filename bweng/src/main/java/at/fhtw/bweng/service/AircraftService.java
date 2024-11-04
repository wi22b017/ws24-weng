package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.AircraftDto;
import at.fhtw.bweng.dto.AirlineDto;
import at.fhtw.bweng.model.Aircraft;
import at.fhtw.bweng.model.Airline;
import at.fhtw.bweng.repository.AircraftRepository;
import at.fhtw.bweng.repository.AirlineRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.List;

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
        return aircraftRepository.save(aircraft).getId();
    }

    public List<Aircraft> getAllAircrafts(){
        return aircraftRepository.findAll();
    }


}
