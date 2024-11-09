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
        try{
            UUID uuid = airlineService.addAirline(airlineDto);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Airline added successfully");
            response.put("id", uuid.toString());
            return ResponseEntity.created(URI.create("/airlines/" + uuid.toString())).body(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Unable to create airline");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping(value = {"/airlines", "/airlines/{id}"})
    public ResponseEntity<?> getAirlines(@PathVariable(required = false) UUID id) {
       if(id != null){
        try {
            Airline airline = airlineService.getAirlineById(id);
            return ResponseEntity.ok(airline);

        } catch (NoSuchElementException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Airline not found");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Unable to get airline");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    } else {
           List<Airline> airlines = airlineService.getAllAirlines();
           return ResponseEntity.ok(airlines);
       }


    }

    @PutMapping("/airlines/{id}")
    public ResponseEntity<?> updateAirline(@PathVariable UUID id, @RequestBody @Valid AirlineDto airlineDto) {

        try {
            airlineService.updateAirline(id, airlineDto);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Airline updated successfully");
            response.put("id", id.toString());
            return ResponseEntity.ok(response);

        } catch (NoSuchElementException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Airline not found");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Unable to update airline");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    @DeleteMapping("/airlines/{id}")
    public ResponseEntity<?> deleteAirline(@PathVariable UUID id) {
        try {
            airlineService.deleteAirline(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Airline deleted successfully");
            response.put("id", id.toString());
            return ResponseEntity.ok(response);

        } catch (NoSuchElementException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Airline not found");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Unable to delete airline");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }

    }



}
