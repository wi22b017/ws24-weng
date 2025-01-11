package at.fhtw.bweng.controller;

import at.fhtw.bweng.dto.BaggageTypeDto;
import at.fhtw.bweng.service.BaggageTypeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class BaggageTypeController {
    private BaggageTypeService baggageTypeService;

    public BaggageTypeController(BaggageTypeService baggageTypeService) {
        this.baggageTypeService = baggageTypeService;
    }

    @PostMapping("/baggageTypes")
    @PreAuthorize("hasPermission(#id, 'at.fhtw.bweng.model.BaggageType', 'create')")
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
    @PreAuthorize("#id == null ? hasPermission(null, 'at.fhtw.bweng.model.BaggageType', 'read') : hasPermission(#id, 'at.fhtw.bweng.model.BaggageType', 'read')")
    public ResponseEntity<?> getBaggageTypes(
            @PathVariable(required = false) UUID id,
            @RequestParam(required = false) String name) {
        Object result = baggageTypeService.getBaggageTypes(id, name);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/baggageTypes/{id}")
    @PreAuthorize("hasPermission(#id, 'at.fhtw.bweng.model.BaggageType', 'update')")
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
    @PreAuthorize("hasPermission(#id, 'at.fhtw.bweng.model.BaggageType', 'delete')")
    public ResponseEntity<Map<String, String>> deleteBaggageType(@PathVariable UUID id) {
        baggageTypeService.deleteBaggageType(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Baggage type deleted successfully");
        response.put("id", id.toString());
        return ResponseEntity.ok(response);
    }

}
