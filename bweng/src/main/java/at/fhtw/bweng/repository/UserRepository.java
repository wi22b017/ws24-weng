package at.fhtw.bweng.repository;

import at.fhtw.bweng.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {

    @Override
    List<User> findAll();

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Override
    Optional<User> findById(UUID id);

    @Override
    boolean existsById(UUID id);

    @Override
    void delete(User user);



}
