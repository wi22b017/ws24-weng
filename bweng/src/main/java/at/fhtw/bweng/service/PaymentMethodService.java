package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.PaymentMethodDto;
import at.fhtw.bweng.model.PaymentMethod;
import at.fhtw.bweng.repository.PaymentMethodRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class PaymentMethodService {

    PaymentMethodRepository paymentMethodRepository;

    public PaymentMethodService(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    public UUID addPaymentMethod(PaymentMethodDto paymentMethodDto) {

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setName(paymentMethodDto.name());

        try {
            return paymentMethodRepository.save(paymentMethod).getId();
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("Payment method with the same data already exists.");
        }
    }

    public List<PaymentMethod> getAllPaymentMethods(){

        return paymentMethodRepository.findAll();
    }

    public PaymentMethod getPaymentMethodById(UUID id) {
        return paymentMethodRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Payment method with id " + id + " not found.") );

    }

    public void updatePaymentMethod(UUID id, PaymentMethodDto paymentMethodDto) {
        PaymentMethod paymentMethod = getPaymentMethodById(id);

        paymentMethod.setName(paymentMethodDto.name());


        try{
            paymentMethodRepository.save(paymentMethod);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("Payment method with the same name already exists.");
        }
    }


    public void deletePaymentMethod(UUID id) {
        PaymentMethod paymentMethod = getPaymentMethodById(id);

        try {
            paymentMethodRepository.delete(paymentMethod);
        } catch (DataIntegrityViolationException ex) {
            throw new NoSuchElementException("Payment method with id " + id + " not found.");
        }
    }

}
