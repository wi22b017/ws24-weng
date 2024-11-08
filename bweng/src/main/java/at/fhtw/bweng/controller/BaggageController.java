package at.fhtw.bweng.controller;

import at.fhtw.bweng.dto.BaggageDto;
import at.fhtw.bweng.model.Baggage;
import at.fhtw.bweng.service.BaggageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class BaggageController {

    private BaggageService baggageService;

    public BaggageController(BaggageService baggageService) {
        this.baggageService = baggageService;
    }

    @PostMapping("/baggages")
    public ResponseEntity<Map<String, String>> addBaggage(@RequestBody @Valid BaggageDto baggageDto) {
        UUID uuid = baggageService.addBaggage(baggageDto);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Baggage created successfully");
        response.put("id", uuid.toString());

        return ResponseEntity
                .created(URI.create("/baggages/" + uuid))
                .body(response);
    }

    @GetMapping(value = {"/baggages", "/baggages/{id}"})
    public ResponseEntity<?> getBaggages(@PathVariable(required = false) UUID id) {
        if (id != null) {
            Baggage baggage = baggageService.getBaggageById(id)
                    .orElseThrow(() -> new RuntimeException("Baggage with ID " + id + " not found."));
            return ResponseEntity.ok(baggage);
        } else {
            List<Baggage> baggages = baggageService.getAllBaggages();
            return ResponseEntity.ok(baggages);
        }
    }

    @PutMapping("/baggages/{id}")
    public ResponseEntity<Map<String, String>> updateBaggage(
            @PathVariable UUID id,
            @RequestBody @Valid BaggageDto baggageDto) {

        baggageService.updateBaggage(id, baggageDto);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Baggage updated successfully");
        response.put("id", id.toString());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/baggages/{id}")
    public ResponseEntity<Map<String, String>> deleteBaggage(@PathVariable UUID id) {

        baggageService.deleteBaggage(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Baggage deleted successfully");
        response.put("id", id.toString());
        return ResponseEntity.ok(response);
    }

}
