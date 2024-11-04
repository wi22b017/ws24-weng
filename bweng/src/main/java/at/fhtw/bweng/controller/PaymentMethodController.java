package at.fhtw.bweng.controller;

import at.fhtw.bweng.dto.PaymentMethodDto;
import at.fhtw.bweng.model.PaymentMethod;
import at.fhtw.bweng.service.PaymentMethodService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;


@RestController
public class PaymentMethodController {

    PaymentMethodService paymentMethodService;

    public PaymentMethodController(PaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

    @PostMapping("/paymentMethods")
    public ResponseEntity<PaymentMethod> addPaymentMethod(@RequestBody @Valid PaymentMethodDto paymentMethodDto) {
        UUID uuid = paymentMethodService.addPaymentMethod(paymentMethodDto);
        return ResponseEntity
                .created(URI.create("/users/" + uuid))
                .build();
    }

    @GetMapping("/paymentMethods")
    public List<PaymentMethod> getPaymentMethods() {
        return paymentMethodService.getAllPaymentMethods();
    }


}
