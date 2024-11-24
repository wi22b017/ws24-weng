package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.UserDto;
import at.fhtw.bweng.model.Address;
import at.fhtw.bweng.model.PaymentMethod;
import at.fhtw.bweng.model.User;
import at.fhtw.bweng.repository.AddressRepository;
import at.fhtw.bweng.repository.PaymentMethodRepository;
import at.fhtw.bweng.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UserService {

    UserRepository userRepository;
    AddressRepository addressRepository;
    PaymentMethodRepository paymentMethodRepository;

    public UserService(UserRepository userRepository, AddressRepository addressRepository, PaymentMethodRepository paymentMethodRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.paymentMethodRepository = paymentMethodRepository;
    }

    public UUID addUser(UserDto userDto) {

        // Find or create the address
        Address userAddress = addressRepository.findByStreetAndNumberAndZipAndCity(
                        userDto.address().street(), userDto.address().number(), userDto.address().zip(), userDto.address().city())
                .orElseGet(() -> {
                    Address newUserAddress = new Address(null, userDto.address().street(), userDto.address().number(), userDto.address().zip(), userDto.address().city(), userDto.address().country());
                    return addressRepository.save(newUserAddress);
                });

        // Find or create the paymentMethod
        PaymentMethod paymentMethod = paymentMethodRepository.findByName(userDto.paymentMethod().name())
                .orElseGet(() -> {
                    PaymentMethod newPaymentMethod = new PaymentMethod(null, userDto.paymentMethod().name());
                    return paymentMethodRepository.save(newPaymentMethod);
                });

        User user = new User(
                null,
                userDto.gender(),
                userDto.firstName(),
                userDto.lastName(),
                userDto.username(),
                userDto.password(),
                userDto.email(),
                LocalDate.parse(userDto.dateOfBirth()),
                userDto.role(),
                userDto.status(),
                userAddress,
                paymentMethod
        );

        try {
            return userRepository.save(user).getId();
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("User with the same username or email already exists");
        }
    }


    public List<User> getAllUsers(){
        return userRepository.findAll();
    }


    public User getUserById(UUID id) {
        return userRepository.findById(id).
                orElseThrow(() -> new NoSuchElementException("User with id " + id + " not found"));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).
                orElseThrow(() -> new NoSuchElementException("User with username " + username + " not found"));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).
                orElseThrow(() -> new NoSuchElementException("User with email " + email + " not found"));
    }

    public void updateUser(UUID id, UserDto userDto) {
        User user = getUserById(id);
        Address address = addressRepository.findByStreetAndNumberAndZipAndCity(
                userDto.address().street(),
                userDto.address().number(),
                userDto.address().zip(),
                userDto.address().city()
        ).orElseGet(() -> {
            Address newAddress = new Address(
                    null,
                    userDto.address().street(),
                    userDto.address().number(),
                    userDto.address().zip(),
                    userDto.address().city(),
                    userDto.address().country()
            );
            return addressRepository.save(newAddress);
        });

        PaymentMethod paymentMethod = paymentMethodRepository.findByName(userDto.paymentMethod().name())
                .orElseGet(() ->{
                    PaymentMethod newPaymentMethod = new PaymentMethod(null, userDto.paymentMethod().name());
                    return paymentMethodRepository.save(newPaymentMethod);
                });

        user.setGender(userDto.gender());
        user.setFirstName(userDto.firstName());
        user.setLastName(userDto.lastName());
        user.setUsername(userDto.username());
        user.setPassword(userDto.password());
        user.setEmail(userDto.email());
        user.setDateOfBirth(LocalDate.parse(userDto.dateOfBirth()));
        user.setRole(userDto.role());
        user.setStatus(userDto.status());
        user.setAddress(address);
        user.setPaymentMethod(paymentMethod);

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("User with the same username or email already exists");
        }
    }


    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("User with ID " + id + " not found");
        }
        userRepository.deleteById(id);

    }

    public void updateUserStatus(UUID id, String status) {
        User user = getUserById(id); // Reuse the existing method to fetch the user
        user.setStatus(status); // Update the status
        userRepository.save(user); // Save the updated user
    }

}
