package at.fhtw.bweng.controller;

import at.fhtw.bweng.dto.AircraftDto;
import at.fhtw.bweng.dto.FlightDto;
import at.fhtw.bweng.model.Aircraft;
import at.fhtw.bweng.model.Flight;
import at.fhtw.bweng.service.AircraftService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AircraftController {

    private AircraftService aircraftService;

    public AircraftController(AircraftService aircraftService) {
        this.aircraftService = aircraftService;
    }

    @PostMapping("/aircrafts")
    @PreAuthorize("hasPermission(null, 'at.fhtw.bweng.model.Aircraft', 'create')")
    public ResponseEntity<Map<String, String>> addAircraft(@RequestBody @Valid AircraftDto aircraftDto) {
        UUID uuid = aircraftService.addAircraft(aircraftDto);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Aircraft created successfully");
        response.put("id", uuid.toString());

        return ResponseEntity
                .created(URI.create("/aircrafts/" + uuid))
                .body(response);
    }

    @GetMapping(value = {"/aircrafts", "/aircrafts/{id}"})
    @PreAuthorize("#id == null ? hasPermission(null, 'at.fhtw.bweng.model.Aircraft', 'read') : hasPermission(#id, 'at.fhtw.bweng.model.Aircraft', 'read')")
    public ResponseEntity<?> getAircrafts(
            @PathVariable(required = false) UUID id,
            @RequestParam(required = false) String serialNumber) {
        Object response = aircraftService.getAircrafts(id, serialNumber);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/aircrafts/{id}")
    @PreAuthorize("hasPermission(#id, 'at.fhtw.bweng.model.Aircraft', 'update')")
    public ResponseEntity<Map<String, String>> updateAircraft(
            @PathVariable UUID id,
            @RequestBody @Valid AircraftDto aircraftDto) {

        // Call service to update, which may throw NoSuchElementException if not found
        aircraftService.updateAircraft(id, aircraftDto);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Aircraft updated successfully");
        response.put("id", id.toString());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("aircrafts/{id}")
    @PreAuthorize("hasPermission(#id, 'at.fhtw.bweng.model.Aircraft', 'delete')")
    public ResponseEntity<Map<String, String>> deleteAircraft(@PathVariable UUID id){

        aircraftService.deleteAircraft(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Aircraft deleted successfully");
        response.put("id", id.toString());
        return ResponseEntity.ok(response);

    }
}
