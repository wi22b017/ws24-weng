package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.FlightDto;
import at.fhtw.bweng.model.Aircraft;
import at.fhtw.bweng.model.Airline;
import at.fhtw.bweng.model.Airport;
import at.fhtw.bweng.model.Flight;
import at.fhtw.bweng.repository.AircraftRepository;
import at.fhtw.bweng.repository.AirlineRepository;
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
    private AirlineRepository airlineRepository;
    private AircraftRepository aircraftRepository;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public FlightService(FlightRepository flightRepository, AirportRepository airportRepository, AirlineRepository airlineRepository, AircraftRepository aircraftRepository) {
        this.flightRepository = flightRepository;
        this.airportRepository = airportRepository;
        this.airlineRepository = airlineRepository;
        this.aircraftRepository = aircraftRepository;
    }

    public UUID addFlight(FlightDto flightDto) {

        LocalDateTime departureTime = LocalDateTime.parse(flightDto.departureTime(), DATE_TIME_FORMATTER);
        LocalDateTime arrivalTime = LocalDateTime.parse(flightDto.arrivalTime(), DATE_TIME_FORMATTER);

        // Find or create the origin airport
        Airport flightOrigin = airportRepository.findByCode(flightDto.flightOrigin().airportCode())
                .orElseGet(() -> {
                    Airport newAirport = new Airport(null, flightDto.flightOrigin().airportText(), flightDto.flightOrigin().airportCode());
                    return airportRepository.save(newAirport);
                });

        // Find or create the destination airport
        Airport flightDestination = airportRepository.findByCode(flightDto.flightDestination().airportCode())
                .orElseGet(() -> {
                    Airport newAirport = new Airport(null, flightDto.flightDestination().airportText(), flightDto.flightDestination().airportCode());
                    return airportRepository.save(newAirport);
                });

        // Find or create the airline
        Airline airline = airlineRepository.findByName(flightDto.aircraft().airline().name())
                .orElseGet(() -> {
                    Airline newAirline = new Airline(null, flightDto.aircraft().airline().name());
                    return airlineRepository.save(newAirline);
                });

        // Find or create the aircraft
        Aircraft aircraft = aircraftRepository.findBySerialNumber(flightDto.aircraft().serialNumber())
                .orElseGet(() -> {
                    Aircraft newAircraft = new Aircraft(null, flightDto.aircraft().serialNumber(), flightDto.aircraft().manufacturer(),
                            flightDto.aircraft().model(), flightDto.aircraft().capacity(), airline);
                    return aircraftRepository.save(newAircraft);
                });

        Flight flight = new Flight(null, flightDto.flightNumber(), departureTime, arrivalTime, flightOrigin, flightDestination, aircraft);
        flightRepository.save(flight);
        return flight.getId();
    }
}
