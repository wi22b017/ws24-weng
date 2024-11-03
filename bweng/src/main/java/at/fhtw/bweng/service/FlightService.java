package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.FlightDto;
import at.fhtw.bweng.model.Airport;
import at.fhtw.bweng.model.Flight;
import at.fhtw.bweng.repository.AirportRepository;
import at.fhtw.bweng.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class FlightService {

    private FlightRepository flightRepository;
    private AirportRepository airportRepository;

    public FlightService(FlightRepository flightRepository, AirportRepository airportRepository) {
        this.flightRepository = flightRepository;
        this.airportRepository = airportRepository;
    }

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public UUID addFlight(FlightDto flightDto) {
        LocalDateTime departureTime = LocalDateTime.parse(flightDto.departureTime(), DATE_TIME_FORMATTER);
        LocalDateTime arrivalTime = LocalDateTime.parse(flightDto.arrivalTime(), DATE_TIME_FORMATTER);

        Airport flightOrigin = new Airport(null, flightDto.flightOrigin().airportCode(),flightDto.flightOrigin().airportText());
        Airport flightDestination = new Airport(null, flightDto.flightDestination().airportCode(),flightDto.flightDestination().airportText());
        airportRepository.save(flightOrigin);
        airportRepository.save(flightDestination);

        Flight flight = new Flight(null, flightDto.flightNumber(), departureTime, arrivalTime, flightOrigin, flightDestination);
        flightRepository.save(flight);
        return flight.getId();
    }
}
