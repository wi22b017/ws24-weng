package at.fhtw.bweng.controller;

import at.fhtw.bweng.dto.BaggageTypeDto;
import at.fhtw.bweng.model.BaggageType;
import at.fhtw.bweng.service.BaggageTypeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class BaggageTypeController {
    private BaggageTypeService baggageTypeService;

    public BaggageTypeController(BaggageTypeService baggageTypeService) {
        this.baggageTypeService = baggageTypeService;
    }

    @PostMapping("/baggageTypes")
    public ResponseEntity<Map<String, String>> addBaggageType(@RequestBody @Valid BaggageTypeDto baggageTypeDto) {
        UUID uuid = baggageTypeService.addBaggageType(baggageTypeDto);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Baggage type created successfully");
        response.put("id", uuid.toString());

        return ResponseEntity
                .created(URI.create("/baggageTypes/" + uuid))
                .body(response);
    }

    @GetMapping(value = { "/baggageTypes", "/baggageTypes/{id}" })
    public ResponseEntity<?> getBaggageTypes(
            @PathVariable(required = false) UUID id,
            @RequestParam(required = false) String name) {

        if (name != null) {
            // Fetch baggage type by name if query parameter is provided
            BaggageType baggageType = baggageTypeService.getBaggageTypeByName(name);
            return ResponseEntity.ok(baggageType);
        } else if (id != null) {
            // Fetch baggage type by ID if path variable is provided
            BaggageType baggageType = baggageTypeService.getBaggageTypeById(id);
            return ResponseEntity.ok(baggageType);
        } else {
            // Fetch all baggage types if neither name nor ID is provided
            List<BaggageType> baggageTypes = baggageTypeService.getAllBaggageTypes();
            return ResponseEntity.ok(baggageTypes);
        }
    }
    @PutMapping("/baggageTypes/{id}")
    public ResponseEntity<Map<String, String>> updateBaggageType(
            @PathVariable UUID id,
            @RequestBody @Valid BaggageTypeDto baggageTypeDto) {
        baggageTypeService.updateBaggageType(id, baggageTypeDto);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Baggage type updated successfully");
        response.put("id", id.toString());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/baggageTypes/{id}")
    public ResponseEntity<Map<String, String>> deleteBaggageType(@PathVariable UUID id) {
        baggageTypeService.deleteBaggageType(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Baggage type deleted successfully");
        response.put("id", id.toString());
        return ResponseEntity.ok(response);
    }

}
