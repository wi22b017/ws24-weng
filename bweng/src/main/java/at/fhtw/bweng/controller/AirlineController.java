package at.fhtw.bweng.controller;

import at.fhtw.bweng.dto.AirlineDto;
import at.fhtw.bweng.model.Airline;
import at.fhtw.bweng.service.AirlineService;
import jakarta.persistence.NoResultException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@RestController
public class AirlineController {

    private AirlineService airlineService;

    public AirlineController(AirlineService airlineService) {
        this.airlineService = airlineService;
    }

    @PostMapping("/airlines")
    public ResponseEntity<?> addAirline(@RequestBody @Valid AirlineDto airlineDto) {

            UUID uuid = airlineService.addAirline(airlineDto);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Airline added successfully");
            response.put("id", uuid.toString());
            return ResponseEntity.created(URI.create("/airlines/" + uuid.toString())).body(response);
    }

    @GetMapping(value = {"/airlines", "/airlines/{id}"})
    public ResponseEntity<?> getAirlines(@PathVariable(required = false) UUID id) {
        Object result = airlineService.getAirlines(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/airlines/{id}")
    public ResponseEntity<?> updateAirline(@PathVariable UUID id, @RequestBody @Valid AirlineDto airlineDto) {
            airlineService.updateAirline(id, airlineDto);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Airline updated successfully");
            response.put("id", id.toString());
            return ResponseEntity.ok(response);
    }


    @DeleteMapping("/airlines/{id}")
    public ResponseEntity<?> deleteAirline(@PathVariable UUID id) {
            airlineService.deleteAirline(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Airline deleted successfully");
            response.put("id", id.toString());
            return ResponseEntity.ok(response);
    }
}
