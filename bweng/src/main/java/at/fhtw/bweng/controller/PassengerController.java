package at.fhtw.bweng.controller;

import at.fhtw.bweng.dto.PassengerDto;
import at.fhtw.bweng.model.Passenger;
import at.fhtw.bweng.service.PassengerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> addPassenger(@RequestBody @Valid PassengerDto passengerDto) {
        UUID uuid = passengerService.addPassenger(passengerDto);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Passenger added successfully");
        response.put("uuid", uuid.toString());
        return ResponseEntity.created(URI.create("/passengers/" + uuid.toString())).body(response);
    }


    @GetMapping("/passengers")
    public ResponseEntity<?> getAllPassengers() {
        List<Passenger> passengers = passengerService.getAllPassengers();
        return ResponseEntity.ok(passengers);
    }

    @GetMapping("/passengers/{id}")
    public ResponseEntity<?> getPassengerById(@PathVariable UUID id) {
        Passenger passenger = passengerService.getPassengerById(id);
        return ResponseEntity.ok(passenger);
    }

    @PutMapping("/passengers/{id}")
    public ResponseEntity<?> updatePassenger(@PathVariable UUID id, @RequestBody @Valid PassengerDto passengerDto) {
        passengerService.updatePassenger(id, passengerDto);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Passenger updated successfully");
        response.put("uuid", id.toString());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/passengers/{id}")
    public ResponseEntity<?> deletePassenger(@PathVariable UUID id) {
        passengerService.deletePassenger(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Passenger deleted successfully");
        response.put("uuid", id.toString());
        return ResponseEntity.ok(response);
    }
}
