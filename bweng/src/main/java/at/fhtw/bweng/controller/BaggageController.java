package at.fhtw.bweng.controller;

import at.fhtw.bweng.dto.BaggageDto;
import at.fhtw.bweng.model.Baggage;
import at.fhtw.bweng.service.BaggageService;
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
public class BaggageController {

    private BaggageService baggageService;

    public BaggageController(BaggageService baggageService) {
        this.baggageService = baggageService;
    }

    @PostMapping("/baggages")
    // protected in the Security Config
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
    // protected in the Security Config
    public ResponseEntity<?> getBaggages(
            @PathVariable(required = false) UUID id,
            @RequestParam(required = false) UUID baggageTypeId) {
        Object result = baggageService.getBaggages(id, baggageTypeId);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/baggages/{id}")
    // protected in the Security Config
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
    // protected in the Security Config
    public ResponseEntity<Map<String, String>> deleteBaggage(@PathVariable UUID id) {
        baggageService.deleteBaggage(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Baggage deleted successfully");
        response.put("id", id.toString());
        return ResponseEntity.ok(response);
    }

}
