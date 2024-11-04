package at.fhtw.bweng.repository;

import at.fhtw.bweng.model.Address;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends CrudRepository<Address, UUID> {

    @Override
    List<Address> findAll();

    Optional<Address> findByStreetAndNumberAndZipAndCity(String street, int number, int zip, String city);

}
