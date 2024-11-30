package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.AirlineDto;
import at.fhtw.bweng.model.Airline;
import at.fhtw.bweng.repository.AirlineRepository;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class AirlineService {

    private AirlineRepository airlineRepository;

    public AirlineService(AirlineRepository airlineRepository) {

        this.airlineRepository = airlineRepository;
    }

    public UUID addAirline(AirlineDto airlineDto) {
        Airline airline = new Airline();
        airline.setName(airlineDto.name());

        try {
            return airlineRepository.save(airline).getId();
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("Airline with the same data already exists.");
        }
    }

    public Object getAirlines(UUID id) {
        if (id != null) {
            return getAirlineById(id);
        } else {
            return getAllAirlines();
        }
    }

    public List<Airline> getAllAirlines(){
        return airlineRepository.findAll();
    }

    public Airline getAirlineById(UUID id) {
        return airlineRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Airline with id " + id + " not found"));
    }

    public Airline getAirlineByName(String name) {
        return airlineRepository.findByName(name)
                .orElseThrow(() -> new NoSuchElementException("Airline with name " + name + " not found"));
    }

    public void updateAirline(UUID id, @Valid AirlineDto airlineDto) {
        Airline airline = getAirlineById(id);

        airline.setName(airlineDto.name());

        try{
            airlineRepository.save(airline);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("Airline with the same name already exists.");
        }

    }

    public void deleteAirline(UUID id) {
        if (!airlineRepository.existsById(id)) {
            throw new NoSuchElementException("Airline with id " + id + " not found");
        }

        airlineRepository.deleteById(id);
    }

}
