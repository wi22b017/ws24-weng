package at.fhtw.bweng.controller;

import at.fhtw.bweng.dto.PassengerDto;
import at.fhtw.bweng.model.Passenger;
import at.fhtw.bweng.service.PassengerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class PassengerController {

    private final PassengerService passengerService;

    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @PostMapping("/passengers")
    @PreAuthorize("hasPermission(null, 'at.fhtw.bweng.model.Passenger', 'create')")
    public ResponseEntity<?> addPassenger(@RequestBody @Valid PassengerDto passengerDto) {
        UUID uuid = passengerService.addPassenger(passengerDto);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Passenger added successfully");
        response.put("uuid", uuid.toString());
        return ResponseEntity.created(URI.create("/passengers/" + uuid.toString())).body(response);
    }

    @GetMapping(value = {"/passengers", "/passengers/{id}"})
    @PreAuthorize("#id == null ? hasPermission(null, 'at.fhtw.bweng.model.Passenger', 'read') : hasPermission(#id, 'at.fhtw.bweng.model.Passenger', 'read')")
    public ResponseEntity<?> getPassengers(@PathVariable(required = false) UUID id) {
        Object result = passengerService.getPassengers(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/passengers/{id}")
    @PreAuthorize("hasPermission(#id, 'at.fhtw.bweng.model.Passenger', 'update')")
    public ResponseEntity<?> updatePassenger(@PathVariable UUID id, @RequestBody @Valid PassengerDto passengerDto) {
        passengerService.updatePassenger(id, passengerDto);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Passenger updated successfully");
        response.put("uuid", id.toString());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/passengers/{id}")
    @PreAuthorize("hasPermission(#id, 'at.fhtw.bweng.model.Passenger', 'delete')")
    public ResponseEntity<?> deletePassenger(@PathVariable UUID id) {
        passengerService.deletePassenger(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Passenger deleted successfully");
        response.put("uuid", id.toString());
        return ResponseEntity.ok(response);
    }
}
