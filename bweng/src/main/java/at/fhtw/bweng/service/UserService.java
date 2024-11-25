package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.UserDto;
import at.fhtw.bweng.model.Address;
import at.fhtw.bweng.model.PaymentMethod;
import at.fhtw.bweng.model.User;
import at.fhtw.bweng.repository.AddressRepository;
import at.fhtw.bweng.repository.PaymentMethodRepository;
import at.fhtw.bweng.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private  final PaymentMethodRepository paymentMethodRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, AddressRepository addressRepository, PaymentMethodRepository paymentMethodRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.passwordEncoder = passwordEncoder;
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
        String hashedPassword = passwordEncoder.encode(userDto.password());

        User user = new User(
                null,
                userDto.gender(),
                userDto.firstName(),
                userDto.lastName(),
                userDto.username(),
                hashedPassword,
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

    public void updateUserProfile(UUID id, Map<String, Object> updates) {
        User user = getUserById(id);

        updates.forEach((key, value) -> {
            switch (key) {
                case "gender":
                    user.setGender((String) value);
                    break;
                case "firstName":
                    user.setFirstName((String) value);
                    break;
                case "lastName":
                    user.setLastName((String) value);
                    break;
                case "username":
                    user.setUsername((String) value);
                    break;
                case "email":
                    user.setEmail((String) value);
                    break;
                case "dateOfBirth":
                    if (value != null) {
                        user.setDateOfBirth(LocalDate.parse((String) value));
                    }
                    break;
                case "address":
                    Map<String, Object> addressMap = (Map<String, Object>) value;
                    Address address = addressRepository.findByStreetAndNumberAndZipAndCity(
                            (String) addressMap.get("street"),
                            (Integer) addressMap.get("number"),
                            (Integer) addressMap.get("zip"),
                            (String) addressMap.get("city")

                    ).orElseGet(() -> {
                        Address newAddress = new Address(
                                null,
                                (String) addressMap.get("street"),
                                (Integer) addressMap.get("number"),
                                (Integer) addressMap.get("zip"),
                                (String) addressMap.get("city"),
                                (String) addressMap.get("country")
                        );
                        return addressRepository.save(newAddress);
                    });
                    user.setAddress(address);
                    break;
                case "paymentMethodName":
                    PaymentMethod paymentMethod = paymentMethodRepository.findByName((String) value)
                            .orElseGet(() -> {
                                PaymentMethod newPaymentMethod = new PaymentMethod(null, (String) value);
                                return paymentMethodRepository.save(newPaymentMethod);
                            });
                            ;
                    user.setPaymentMethod(paymentMethod);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid field: " + key);
            }
        });

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("User with the same username or email already exists");
        }
    }


    public void updateUserStatus(UUID id, String status) {
        User user = getUserById(id); // Reuse the existing method to fetch the user
        user.setStatus(status); // Update the status
        userRepository.save(user); // Save the updated user
    }

    public void updateUserPassword(UUID id, String currentPassword,String newPassword) {
        User user = getUserById(id);
        if(!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        String hashedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(hashedNewPassword);
        userRepository.save(user);
    }

}
