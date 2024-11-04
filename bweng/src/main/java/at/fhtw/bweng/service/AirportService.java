package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.AirportDto;
import at.fhtw.bweng.model.Airport;
import at.fhtw.bweng.repository.AirportRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.List;

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

}
