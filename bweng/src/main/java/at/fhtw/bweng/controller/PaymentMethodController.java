package at.fhtw.bweng.controller;

import at.fhtw.bweng.dto.PaymentMethodDto;
import at.fhtw.bweng.model.PaymentMethod;
import at.fhtw.bweng.service.PaymentMethodService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;


@RestController
public class PaymentMethodController {

    PaymentMethodService paymentMethodService;

    public PaymentMethodController(PaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

    @PostMapping("/paymentMethods")
    public ResponseEntity<?> addPaymentMethod(@RequestBody @Valid PaymentMethodDto paymentMethodDto) {
        UUID uuid = paymentMethodService.addPaymentMethod(paymentMethodDto);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Payment method added successfully");
            response.put("id", uuid.toString());
            return ResponseEntity.created(URI.create("/paymentMethods/" + uuid.toString())).body(response);
    }

    @GetMapping(value = {"/paymentMethods", "/paymentMethods/{id}"})
    public ResponseEntity<?> getPaymentMethods(@PathVariable(required = false) UUID id) {
        Object result = paymentMethodService.getPaymentMethods(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/paymentMethods/{id}")
    public ResponseEntity<?> updatePaymentMethod( @PathVariable UUID id,
                                                  @RequestBody @Valid PaymentMethodDto paymentMethodDto) {
            paymentMethodService.updatePaymentMethod(id, paymentMethodDto);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Payment method updated successfully");
            response.put("id", id.toString());
            return ResponseEntity.ok(response);
    }

    @DeleteMapping("/paymentMethods/{id}")
    public ResponseEntity<?> deletePaymentMethod(@PathVariable UUID id) {

            paymentMethodService.deletePaymentMethod(id);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Payment method deleted successfully");
            response.put("id", id.toString());
            return ResponseEntity.ok(response);
    }
}
