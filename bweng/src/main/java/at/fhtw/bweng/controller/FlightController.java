package at.fhtw.bweng.controller;

import at.fhtw.bweng.dto.FlightDto;
import at.fhtw.bweng.model.Airport;
import at.fhtw.bweng.model.Flight;
import at.fhtw.bweng.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;
import java.util.List;


@RestController
public class FlightController {

    private FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PostMapping("/flights")
    public ResponseEntity<Map<String, String>> addFlight(@RequestBody @Valid FlightDto flightDto) {
        UUID uuid = flightService.addFlight(flightDto);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Flight created successfully");
        response.put("id", uuid.toString());

        return ResponseEntity
                .created(URI.create("/flights/" + uuid))
                .body(response);
    }

    @GetMapping(value = {"/flights", "/flights/{id}"})
    public ResponseEntity<?> getFlights(
            @PathVariable(required = false) UUID id,
            @RequestParam(required = false) String flightNumber) {

        if (flightNumber != null) {
            // Fetch flight by flight number if query parameter is provided
            Flight flight = flightService.getFlightByFlightNumber(flightNumber);
            return ResponseEntity.ok(flight);
        } else if (id != null) {
            // Fetch flight by ID if path variable is provided
            Flight flight = flightService.getFlightById(id);
            return ResponseEntity.ok(flight);
        } else {
            // Fetch all flights if neither flightNumber nor ID is provided
            List<Flight> flights = flightService.getAllFlights();
            return ResponseEntity.ok(flights);
        }
    }

    @PutMapping("/flights/{id}")
    public ResponseEntity<Map<String, String>> updateFlight(
            @PathVariable UUID id,
            @RequestBody @Valid FlightDto flightDto) {

        // Call service to update, which may throw NoSuchElementException if not found
        flightService.updateFlight(id, flightDto);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Flight updated successfully");
        response.put("id", id.toString());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/flights/{id}")
    public ResponseEntity<Map<String, String>> deleteFlight(@PathVariable UUID id) {

        // Call service to delete, which may throw NoSuchElementException if not found
        flightService.deleteFlight(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Flight deleted successfully");
        response.put("id", id.toString());
        return ResponseEntity.ok(response);
    }

}
