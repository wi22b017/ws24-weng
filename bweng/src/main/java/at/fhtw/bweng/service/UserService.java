package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.PictureDto;
import at.fhtw.bweng.dto.UserDto;
import at.fhtw.bweng.dto.UserResponseDto;
import at.fhtw.bweng.mapper.UserMapper;
import at.fhtw.bweng.model.Address;
import at.fhtw.bweng.model.PaymentMethod;
import at.fhtw.bweng.model.Picture;
import at.fhtw.bweng.model.User;
import at.fhtw.bweng.repository.AddressRepository;
import at.fhtw.bweng.repository.PaymentMethodRepository;
import at.fhtw.bweng.repository.UserRepository;
import at.fhtw.bweng.util.IsoUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private  final PaymentMethodRepository paymentMethodRepository;
    private final PasswordEncoder passwordEncoder;
    private final PictureService pictureService;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, AddressRepository addressRepository, PaymentMethodRepository paymentMethodRepository, PasswordEncoder passwordEncoder, PictureService pictureService, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.passwordEncoder = passwordEncoder;
        this.pictureService = pictureService;
        this.userMapper = userMapper;
    }

    public UUID addUser(UserDto userDto) {

        // if country code ist not a valid ISOCountry, throw IllegalArgumentException
        if (!IsoUtil.isValidISOCountry(userDto.address().country())) {
            throw new IllegalArgumentException("Invalid country code: " + userDto.address().country());
        }

        // Find or create the address
        Address userAddress = addressRepository.findByStreetAndNumberAndZipAndCityAndCountry(
                        userDto.address().street(),
                        userDto.address().number(),
                        userDto.address().zip(),
                        userDto.address().city(),
                        userDto.address().country())
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

    public Object getUsers(UUID id) {
        if (id != null) {
            return getUserResponseDtoById(id);
        } else {
            return getAllUsers();
        }
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapWithProfilePicture)
                .collect(Collectors.toList());
    }

    public UserResponseDto getUserResponseDtoById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User with ID " + id + " not found"));

        return mapWithProfilePicture(user);
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User with ID " + id + " not found"));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User with username " + username + " not found"));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User with email " + email + " not found"));
    }

    public void updateUser(UUID id, UserDto userDto) {

        // if country code ist not a valid ISOCountry, throw IllegalArgumentException
        if (!IsoUtil.isValidISOCountry(userDto.address().country())) {
            throw new IllegalArgumentException("Invalid country code: " + userDto.address().country());
        }

        User user = getUserById(id);
        Address address = addressRepository.findByStreetAndNumberAndZipAndCityAndCountry(
                userDto.address().street(),
                userDto.address().number(),
                userDto.address().zip(),
                userDto.address().city(),
                userDto.address().country()
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

                    // if country code ist not a valid ISOCountry, throw IllegalArgumentException
                    if (!IsoUtil.isValidISOCountry( (String) addressMap.get("country"))) {
                    throw new IllegalArgumentException("Invalid country code: " + (String) addressMap.get("country"));
                }

                    Address address = addressRepository.findByStreetAndNumberAndZipAndCityAndCountry(
                            (String) addressMap.get("street"),
                            (Integer) addressMap.get("number"),
                            (Integer) addressMap.get("zip"),
                            (String) addressMap.get("city"),
                            (String) addressMap.get("country")

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
                case "status":
                    ensureAdminRole(); // Check if the current user has the ADMIN role
                    user.setStatus((String) value);
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

    // Helper method to ensure the current user has the ADMIN role
    private void ensureAdminRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            throw new SecurityException("Only administrators are allowed to change the status field.");
        }
    }

    public void updateUserPassword(UUID id, Map<String, Object> passwords) {
        // Validate input
        String currentPassword = (String) passwords.get("currentPassword");
        String newPassword = (String) passwords.get("newPassword");

        if (currentPassword == null || newPassword == null) {
            throw new IllegalArgumentException("Missing required fields: currentPassword and/or newPassword");
        }

        User user = getUserById(id);

        // Verify current password
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        // Encode and update the new password
        String hashedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(hashedNewPassword);

        userRepository.save(user);
    }

    public void updateUserProfilePicture(UUID userId, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with ID " + userId + " not found"));

        PictureDto pictureDto = pictureService.upload(file);
        Picture picture = pictureService.findById(pictureDto.id());

        user.setProfilePicture(picture);
        userRepository.save(user);
    }

    private UserResponseDto mapWithProfilePicture(User user) {
        // Use MapStruct to map fields
        UserResponseDto userResponseDto = userMapper.toDto(user);

        // Add profile picture URL manually
        if (user.getProfilePicture() != null) {
            String profilePictureUrl = pictureService.generatePresignedUrl(user.getProfilePicture().getExternalId());
            userResponseDto = new UserResponseDto(
                    userResponseDto.id(),
                    userResponseDto.gender(),
                    userResponseDto.firstName(),
                    userResponseDto.lastName(),
                    userResponseDto.username(),
                    userResponseDto.email(),
                    userResponseDto.dateOfBirth(),
                    userResponseDto.role(),
                    userResponseDto.status(),
                    userResponseDto.address(),
                    userResponseDto.paymentMethod(),
                    profilePictureUrl
            );
        }
        return userResponseDto;
    }

}
