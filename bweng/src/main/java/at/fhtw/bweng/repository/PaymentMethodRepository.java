package at.fhtw.bweng.repository;

import at.fhtw.bweng.model.PaymentMethod;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentMethodRepository extends CrudRepository<PaymentMethod, UUID> {

    @Override
    List<PaymentMethod> findAll();

    Optional<PaymentMethod> findByName(String name);


}
