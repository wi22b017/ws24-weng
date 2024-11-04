package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.PaymentMethodDto;
import at.fhtw.bweng.model.PaymentMethod;
import at.fhtw.bweng.repository.PaymentMethodRepository;
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
        return paymentMethodRepository.save(paymentMethod).getId();
    }

    public List<PaymentMethod> getAllPaymentMethods(){
        return paymentMethodRepository.findAll();
    }
}
