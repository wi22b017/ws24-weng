package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.PaymentMethodDto;
import at.fhtw.bweng.model.PaymentMethod;
import at.fhtw.bweng.repository.PaymentMethodRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentMethodService {

    PaymentMethodRepository paymentMethodRepository;

    public PaymentMethodService(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    public UUID addPaymentMethod(PaymentMethodDto paymentMethodDto) {

        PaymentMethod paymentMethod = new PaymentMethod(null, paymentMethodDto.name());

        try {
            return paymentMethodRepository.save(paymentMethod).getId();
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("Payment method with the same data already exists.");
        }
    }

    public List<PaymentMethod> getAllPaymentMethods(){
        return paymentMethodRepository.findAll();
    }
}
