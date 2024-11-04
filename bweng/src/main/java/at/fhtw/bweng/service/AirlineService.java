package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.AirlineDto;
import at.fhtw.bweng.model.Airline;
import at.fhtw.bweng.repository.AirlineRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
