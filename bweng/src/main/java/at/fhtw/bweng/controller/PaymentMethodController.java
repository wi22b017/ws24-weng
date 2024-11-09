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
        try {
        UUID uuid = paymentMethodService.addPaymentMethod(paymentMethodDto);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Payment method added successfully");
            response.put("id", uuid.toString());
            return ResponseEntity.created(URI.create("/paymentMethods/" + uuid.toString())).body(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Unable to create payment method");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }

    @GetMapping(value = {"/paymentMethods", "/paymentMethods/{id}"})
    public ResponseEntity<?> getPaymentMethods(@PathVariable(required = false) UUID id) {
        if (id != null) {
            try {
                PaymentMethod paymentMethod = paymentMethodService.getPaymentMethodById(id);
                return ResponseEntity.ok(paymentMethod);

            } catch (NoSuchElementException e){
                Map<String, String> response = new HashMap<>();
                response.put("message", "Payment method not found");
                response.put("id", id.toString());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

            } catch (Exception e){
                Map<String, String> response = new HashMap<>();
                response.put("error", "Unable to get payment method");
                response.put("message", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } else {
            List<PaymentMethod> paymentMethods = paymentMethodService.getAllPaymentMethods();
            return ResponseEntity.ok(paymentMethods);
        }

    }



    @PutMapping("/paymentMethods/{id}")
    public ResponseEntity<?> updatePaymentMethod( @PathVariable UUID id,
                                                  @RequestBody @Valid PaymentMethodDto paymentMethodDto) {
        try{
            paymentMethodService.updatePaymentMethod(id, paymentMethodDto);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Payment method updated successfully");
            response.put("id", id.toString());
            return ResponseEntity.ok(response);

        } catch (NoSuchElementException e){
            Map<String, String> response = new HashMap<>();
            response.put("message", "Payment method not found");
            response.put("id", id.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (Exception e){
            Map<String, String> response = new HashMap<>();
            response.put("error", "Unable to update payment method");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/paymentMethods/{id}")
    public ResponseEntity<?> deletePaymentMethod(@PathVariable UUID id) {
        try{
            paymentMethodService.deletePaymentMethod(id);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Payment method deleted successfully");
            response.put("id", id.toString());
            return ResponseEntity.ok(response);

        } catch (NoSuchElementException e){
            Map<String, String> response = new HashMap<>();
            response.put("message", "Payment method not found");
            response.put("id", id.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (Exception e){
            Map<String, String> response = new HashMap<>();
            response.put("error", "Unable to delete payment method");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


}
