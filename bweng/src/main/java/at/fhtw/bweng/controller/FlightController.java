package at.fhtw.bweng.controller;

import at.fhtw.bweng.dto.FlightDto;
import at.fhtw.bweng.model.Flight;
import at.fhtw.bweng.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
public class FlightController {

    private FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PostMapping("/flights")
    public ResponseEntity<Flight> addFlight(@RequestBody @Valid FlightDto flightDto) {
        UUID uuid = flightService.addFlight(flightDto);
        return ResponseEntity
                .created(URI.create("/flights/" + uuid))
                .build();
    }

}
