package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.UserDto;
import at.fhtw.bweng.model.Address;
import at.fhtw.bweng.model.PaymentMethod;
import at.fhtw.bweng.model.User;
import at.fhtw.bweng.repository.AddressRepository;
import at.fhtw.bweng.repository.PaymentMethodRepository;
import at.fhtw.bweng.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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
        Address userAddress = addressRepository.findByStreetAndNumberAndZipAndCity(userDto.address().street(), userDto.address().number(), userDto.address().zip(), userDto.address().city())
                .orElseGet(() -> {
                    Address newUserAddress = new Address(null, userDto.address().street(), userDto.address().number(), userDto.address().zip(),userDto.address().city(), userDto.address().country());
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
                userDto.role(),
                userDto.status(),
                userAddress,
                paymentMethod
        );
        return userRepository.save(user).getId();
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

}
