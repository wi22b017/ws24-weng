package at.fhtw.bweng.controller;

import at.fhtw.bweng.dto.AircraftDto;
import at.fhtw.bweng.model.Aircraft;
import at.fhtw.bweng.service.AircraftService;
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
public class AircraftController {

    private AircraftService aircraftService;

    public AircraftController(AircraftService aircraftService) {
        this.aircraftService = aircraftService;
    }

    @PostMapping("/aircrafts")
    public ResponseEntity<Aircraft> addAircraft(@RequestBody @Valid AircraftDto aircraftDto) {
        UUID uuid = aircraftService.addAircraft(aircraftDto);
        return ResponseEntity
                .created(URI.create("/aircrafts/" + uuid))
                .build();
    }

    @GetMapping("/aircrafts")
    public List<Aircraft> getAircrafts() {
        return aircraftService.getAllAircrafts();
    }

}
