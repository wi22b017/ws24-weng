package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.AirportDto;
import at.fhtw.bweng.model.Airport;
import at.fhtw.bweng.repository.AirportRepository;
import org.springframework.dao.DataIntegrityViolationException;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AirportService {

    private AirportRepository airportRepository;

    public AirportService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    public UUID addAirport(AirportDto airportDto) {
        Airport newAirport = new Airport(null, airportDto.airportText(), airportDto.airportCode());
        return airportRepository.save(newAirport).getId();
    }

    public List<Airport> getAllAirports(){
        return airportRepository.findAll();
    }

    public Airport getAirportById(UUID id) {
        // Find airport by ID or throw exception if not found
        return airportRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Airport with ID " + id + " not found"));
    }

    public void updateAirport(UUID id, AirportDto airportDto) {
        // Find airport and update if it exists, otherwise throw exception
        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Airport with ID " + id + " not found"));
        airport.setName(airportDto.airportText());
        airport.setCode(airportDto.airportCode());

        try {
            airportRepository.save(airport);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("Airport with the same data already exists.");
        }
    }

    public void deleteAirport(UUID id) {
        // Check if airport exists; if not, throw NoSuchElementException
        if (!airportRepository.existsById(id)) {
            throw new NoSuchElementException("Airport with ID " + id + " not found");
        }

        airportRepository.deleteById(id);
    }

}
