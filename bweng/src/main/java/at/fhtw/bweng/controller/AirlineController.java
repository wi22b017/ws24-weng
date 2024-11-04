package at.fhtw.bweng.controller;

import at.fhtw.bweng.dto.AirlineDto;
import at.fhtw.bweng.model.Airline;
import at.fhtw.bweng.service.AirlineService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
public class AirlineController {

    private AirlineService airlineService;

    public AirlineController(AirlineService airlineService) {
        this.airlineService = airlineService;
    }

    @PostMapping("/airlines")
    public ResponseEntity<Airline> addAirline(@RequestBody @Valid AirlineDto airlineDto) {
        UUID uuid = airlineService.addAirline(airlineDto);
        return ResponseEntity
                .created(URI.create("/airlines/" + uuid))
                .build();
    }

    @GetMapping("/airlines")
    public List<Airline> getAirlines() {
        return airlineService.getAllAirlines();
    }




}
