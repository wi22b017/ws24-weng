package at.fhtw.bweng.repository;

import at.fhtw.bweng.model.Baggage;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BaggageRepository extends CrudRepository<Baggage, UUID> {
    @Override
    List<Baggage> findAll();
    @Override
    Optional<Baggage> findById(UUID id);

    @Override
    boolean existsById(UUID id);
    @Override
    void deleteById(UUID uuid);
}
