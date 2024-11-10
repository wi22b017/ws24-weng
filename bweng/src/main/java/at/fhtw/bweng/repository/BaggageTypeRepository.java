package at.fhtw.bweng.repository;

import at.fhtw.bweng.model.BaggageType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BaggageTypeRepository extends CrudRepository<BaggageType, UUID> {
    @Override
    List<BaggageType> findAll();
    @Override
    Optional<BaggageType> findById(UUID id);

  //find baggage type by its unique name
    Optional<BaggageType> findByName(String name);

}
