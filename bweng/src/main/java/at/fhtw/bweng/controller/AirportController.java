package at.fhtw.bweng.controller;

import at.fhtw.bweng.dto.AirportDto;
import at.fhtw.bweng.dto.FlightDto;
import at.fhtw.bweng.model.Airport;
import at.fhtw.bweng.model.Flight;
import at.fhtw.bweng.service.AirportService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;
import java.util.List;

@RestController
public class AirportController {

    private AirportService airportService;

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @PostMapping("/airports")
    public ResponseEntity<Airport> addAirport(@RequestBody @Valid AirportDto airportDto) {
        UUID uuid = airportService.addAirport(airportDto);
        return ResponseEntity
                .created(URI.create("/airports/" + uuid))
                .build();
    }

    @GetMapping("/airports")
    public List<Airport> getAirports() {
        return airportService.getAllAirports();
    }


}
