package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.AirlineDto;
import at.fhtw.bweng.dto.AirportDto;
import at.fhtw.bweng.model.Airline;
import at.fhtw.bweng.model.Airport;
import at.fhtw.bweng.repository.AirlineRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.List;

@Service
public class AirlineService {

    private AirlineRepository airlineRepository;

    public AirlineService(AirlineRepository airlineRepository) {
        this.airlineRepository = airlineRepository;
    }

    public UUID addAirline(AirlineDto airlineDto) {
        Airline airline = new Airline(null, airlineDto.name());
        return airlineRepository.save(airline).getId();
    }

    public List<Airline> getAllAirlines(){
        return airlineRepository.findAll();
    }

}
