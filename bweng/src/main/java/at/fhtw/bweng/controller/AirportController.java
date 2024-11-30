package at.fhtw.bweng.controller;

import at.fhtw.bweng.dto.AirportDto;
import at.fhtw.bweng.model.Airport;
import at.fhtw.bweng.service.AirportService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AirportController {

    private AirportService airportService;

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @PostMapping("/airports")
    public ResponseEntity<Map<String, String>> addAirport(@RequestBody @Valid AirportDto airportDto) {
        UUID uuid = airportService.addAirport(airportDto);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Airport created successfully");
        response.put("id", uuid.toString());

        return ResponseEntity
                .created(URI.create("/airports/" + uuid))
                .body(response);
    }

    @GetMapping(value = {"/airports", "/airports/{id}"})
    public ResponseEntity<?> getAirports(@PathVariable(required = false) UUID id) {
        Object result = airportService.getAirports(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/airports/{id}")
    public ResponseEntity<Map<String, String>> updateAirport(
            @PathVariable UUID id,
            @RequestBody @Valid AirportDto airportDto) {

        // Call service to update, which may throw NoSuchElementException if not found
        airportService.updateAirport(id, airportDto);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Airport updated successfully");
        response.put("id", id.toString());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/airports/{id}")
    public ResponseEntity<Map<String, String>> deleteAirport(@PathVariable UUID id) {

        // Call service to delete, which may throw NoSuchElementException if not found
        airportService.deleteAirport(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Airport deleted successfully");
        response.put("id", id.toString());
        return ResponseEntity.ok(response);
    }


}
