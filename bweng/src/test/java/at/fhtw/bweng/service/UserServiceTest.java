package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.*;
import at.fhtw.bweng.mapper.UserMapper;
import at.fhtw.bweng.model.Address;
import at.fhtw.bweng.model.PaymentMethod;
import at.fhtw.bweng.model.Picture;
import at.fhtw.bweng.model.User;
import at.fhtw.bweng.repository.AddressRepository;
import at.fhtw.bweng.repository.PaymentMethodRepository;
import at.fhtw.bweng.repository.UserRepository;
import at.fhtw.bweng.util.IsoUtil;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private PaymentMethodRepository paymentMethodRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private PictureService pictureService;

    @Mock
    private MultipartFile file;
    @Mock
    private UserMapper userMapper;
    @Mock
    private IsoUtil isoUtil;

    @InjectMocks
    private UserService userService;

    private UserDto mockUserDto() {
        return new UserDto(
                "Male",
                "John",
                "Doe",
                "johndoe",
                "password123",
                "john.doe@example.com",
                "1990-01-01",
                "USER",
                "ACTIVE",
                new AddressDto("Main St", 1, 1010, "Vienna", "AT"),
                new PaymentMethodDto("Credit Card")
        );
    }

    private User mockUser() {
        User user = new User(
                UUID.randomUUID(),
                "Male",
                "John",
                "Doe",
                "johndoe",
                "password123",
                "john.doe@example.com",
                LocalDate.of(1990, 1, 1),
                "USER",
                "ACTIVE",
                mockAddress(),
                mockPaymentMethod()
        );

        return user;
    }

    private UserResponseDto mockUserResponseDto() {
        return new UserResponseDto(
                UUID.randomUUID(),
                "male",
                "John",
                "Doe",
                "johndoe",
                "john.doe@example.com",
                "1990-01-01",
                "USER",
                "ACTIVE",
                new AddressDto("Main St", 1, 1010, "Vienna", "AT"),
                new PaymentMethodDto("Credit Card"),
                "https://example.com/profile.jpg"
        );
    }

    private Address mockAddress() {
        return new Address(UUID.randomUUID(), "Main St", 1, 1010, "Vienna", "AT");
    }

    private PaymentMethod mockPaymentMethod() {
        PaymentMethod paymentMethod = new PaymentMethod(UUID.randomUUID(), "Credit Card");
        return paymentMethod;
    }

    @Test
    void getUserById_ReturnsUser() {
        //Arrange
        User user = mockUser();
        UUID userId = UUID.randomUUID();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        //Act
        User result = userService.getUserById(userId);

        //Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("johndoe", result.getUsername());
        verify(userRepository, times(1)).findById(userId);
    }
    @Test
    void getUserById_ThrowsExceptionWhenUserNotFound() {
        //Arrange
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //Act & Assert
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                ()->userService.getUserById(userId)
        );
        assertEquals("User with ID " + userId + " not found", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);

    }

    @Test
    void getUserByEmail_ReturnsUser() {
        //Arrange
        User user = mockUser();
        String email = "abcd@123.com";
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        //Act
        User result = userService.getUserByEmail(email);

        //Assert
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals("johndoe", result.getUsername());
        verify(userRepository, times(1)).findByEmail(email);
    }
    @Test
    void getUserByEmail_ThrowsExceptionWhenUserNotFound() {
        //Arrange
        String email = "abcd@123.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        //Act & Assert
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                ()->userService.getUserByEmail(email)
        );
        assertEquals("User with email " + email + " not found", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void getUserByUsername_ReturnsUser() {
        //Arrange
        User user = mockUser();
        String username = "johndoe";
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        //Act
        User result = userService.getUserByUsername(username);

        //Assert
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals("john.doe@example.com", result.getEmail());
        verify(userRepository, times(1)).findByUsername(username);
    }
    @Test
    void getUserByUsername_ThrowsExceptionWhenUserNotFound() {
        //Arrange
        String username = "johndoe";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        //Act & Assert
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                ()->userService.getUserByUsername(username)
        );
        assertEquals("User with username " + username + " not found", exception.getMessage());
        verify(userRepository, times(1)).findByUsername(username);

    }

    @Test
    void getUsers_ById_ReturnsUserResponseDto() {
        //Arrange
        User user = mockUser();
        UUID userId = user.getId();
        UserResponseDto userResponseDto = new UserResponseDto(
                userId,
                "male",
                "John",
                "Doe",
                "johndoe",
                "john.doe@example.com",
                "1990-01-01",
                "USER",
                "ACTIVE",
                new AddressDto("Main St", 1, 1010, "Vienna", "AT"),
                new PaymentMethodDto("Credit Card"),
                "https://example.com/profile.jpg");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userService.getUserResponseDtoById(userId)).thenReturn(userResponseDto);

        //Act
       Object result = userService.getUsers(userResponseDto.id());

        //Assert
        assertNotNull(result);
        assertInstanceOf(UserResponseDto.class, result);
        assertEquals("johndoe", ((UserResponseDto) result).username());
    }

    @Test
    void getUsers_WithoutId_ReturnsAllUsers() {
        //Arrange
        User user1 = mockUser();
        User user2 = mockUser();
        UserResponseDto userResponseDto1 = new UserResponseDto(
               user1.getId(),
                "male",
                "John",
                "Doe",
                "johndoe",
                "john.doe@example.com",
                "1990-01-01",
                "USER",
                "ACTIVE",
                new AddressDto("Main St", 1, 1010, "Vienna", "AT"),
                new PaymentMethodDto("Credit Card"),
                "https://example.com/profile.jpg");
        UserResponseDto userResponseDto2 = new UserResponseDto(
                user2.getId(),
                "male",
                "John",
                "Doe",
                "johndoe",
                "john.doe@example.com",
                "1990-01-01",
                "USER",
                "ACTIVE",
                new AddressDto("Main St", 1, 1010, "Vienna", "AT"),
                new PaymentMethodDto("Credit Card"),
                "https://example.com/profile.jpg");
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));
        when(userService.getUserResponseDtoById(user1.getId())).thenReturn(userResponseDto1);
        when(userService.getUserResponseDtoById(user2.getId())).thenReturn(userResponseDto2);

        //Act
        Object result = userService.getUsers(null);

        //Assert
        assertNotNull(result);
        assertInstanceOf(List.class, result);
        List<UserResponseDto> resultList = (List<UserResponseDto>) result;
        assertEquals(2, resultList.size());
    }

    @Test
    void deleteUser_Success() {
        //Arrange
        UUID userId = UUID.randomUUID();
        when(userRepository.existsById(userId)).thenReturn(true);

        //Act
        userService.deleteUser(userId);

        //Assert
        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, times(1)).deleteById(userId);

    }

    @Test
    void deleteUser_UserNotFound() {
        //Arrange
        UUID userId = UUID.randomUUID();
        when(userRepository.existsById(userId)).thenReturn(false);

        //Act & Assert
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () ->  userService.deleteUser(userId)
        );

        assertEquals("User with ID " + userId + " not found", exception.getMessage());
        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, times(0)).deleteById(userId);

    }

    @Test
    void addUser_Success() {
        // Arrange
        UserDto userDto = mockUserDto();
        Address mockAddress = mockAddress();
        PaymentMethod mockPaymentMethod = mockPaymentMethod();
        User mockUser = mockUser();
        UUID expectedUserId = UUID.randomUUID();

        when(addressRepository.findByStreetAndNumberAndZipAndCityAndCountry(
                userDto.address().street(),
                userDto.address().number(),
                userDto.address().zip(),
                userDto.address().city(),
                userDto.address().country()
        )).thenReturn(Optional.of(mockAddress));

        when(paymentMethodRepository.findByName(userDto.paymentMethod().name()))
                .thenReturn(Optional.of(mockPaymentMethod));

        when(passwordEncoder.encode(userDto.password())).thenReturn("hashedPassword");

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(expectedUserId);
            return user;
        });

        // Act
        UUID result = userService.addUser(userDto);

        // Assert
        assertNotNull(result);
        verify(addressRepository, times(1)).findByStreetAndNumberAndZipAndCityAndCountry(
                userDto.address().street(),
                userDto.address().number(),
                userDto.address().zip(),
                userDto.address().city(),
                userDto.address().country()
        );
        verify(paymentMethodRepository, times(1)).findByName(userDto.paymentMethod().name());
        verify(passwordEncoder, times(1)).encode(userDto.password());
        verify(userRepository, times(1)).save(any(User.class));
        assertEquals(expectedUserId,result);
    }

    @Test
    void addUser_ThrowsDataIntegrityViolationExceptionWhenDuplicate() {
        // Arrange
        UserDto userDto = mockUserDto();
        Address mockAddress = mockAddress();
        PaymentMethod mockPaymentMethod = mockPaymentMethod();

        when(addressRepository.findByStreetAndNumberAndZipAndCityAndCountry(
                userDto.address().street(),
                userDto.address().number(),
                userDto.address().zip(),
                userDto.address().city(),
                userDto.address().country()
        )).thenReturn(Optional.of(mockAddress));

        when(paymentMethodRepository.findByName(userDto.paymentMethod().name()))
                .thenReturn(Optional.of(mockPaymentMethod));

        when(passwordEncoder.encode(userDto.password())).thenReturn("hashedPassword");

        when(userRepository.save(any(User.class))).thenThrow(new DataIntegrityViolationException("User with the same username or email already exists"));

        // Act & Assert
        DataIntegrityViolationException exception = assertThrows(
                DataIntegrityViolationException.class,
                () -> userService.addUser(userDto)
        );

        assertEquals("User with the same username or email already exists", exception.getMessage());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void addUser_InvalidCountryCode_ShouldThrowException() {
        // Arrange
        UserDto userDto = mockUserDto();
        try (MockedStatic<IsoUtil> mockedStatic = mockStatic(IsoUtil.class)) {
            mockedStatic.when(() -> IsoUtil.isValidISOCountry(userDto.address().country()))
                    .thenReturn(false);

            // Act & Assert
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->
                    userService.addUser(userDto));

            assertEquals("Invalid country code: " + userDto.address().country(), thrown.getMessage());
            verify(addressRepository, never()).findByStreetAndNumberAndZipAndCityAndCountry(
                    anyString(), anyInt(), anyInt(), anyString(), anyString() // Use correct matchers
            );
            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Test
    void addUser_NoAddressFound_ShouldCreateNewAddress() {
        UserDto userDto = mockUserDto();
        PaymentMethod paymentMethod = mockPaymentMethod();
        UUID expectedUserId = UUID.randomUUID();
        try (MockedStatic<IsoUtil> mockedStatic = mockStatic(IsoUtil.class)) {
            // Arrange
            mockedStatic.when(() -> IsoUtil.isValidISOCountry(userDto.address().country()))
                    .thenReturn(true);

            when(addressRepository.findByStreetAndNumberAndZipAndCityAndCountry(
                    anyString(), anyInt(), anyInt(), anyString(), anyString()))
                    .thenReturn(Optional.empty()); // No address found

            when(addressRepository.save(any(Address.class))).thenAnswer(invocation -> {
                Address newAddress = invocation.getArgument(0);
                newAddress.setId(UUID.randomUUID());
                return newAddress;
            });

            when(paymentMethodRepository.findByName(anyString())).thenReturn(Optional.of(paymentMethod));
            when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");

            when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
                User user = invocation.getArgument(0);
                user.setId(expectedUserId);
                return user;
            });

            // Act
            UUID result = userService.addUser(userDto);

            // Assert
            assertNotNull(result);
            verify(addressRepository).save(any(Address.class)); // Address should be created
            verify(userRepository).save(any(User.class));
            assertEquals(expectedUserId, result);
        }
    }

    @Test
    void addUser_NoPaymentMethodFound_ShouldCreateNewPaymentMethod() {
        UserDto userDto = mockUserDto();
        Address address = mockAddress();
        UUID expectedUserId = UUID.randomUUID();
        try (MockedStatic<IsoUtil> mockedStatic = mockStatic(IsoUtil.class)) {
            // Arrange
            mockedStatic.when(() -> IsoUtil.isValidISOCountry(userDto.address().country()))
                    .thenReturn(true);

            when(addressRepository.findByStreetAndNumberAndZipAndCityAndCountry(
                    anyString(), anyInt(), anyInt(), anyString(), anyString()))
                    .thenReturn(Optional.of(address));

            when(paymentMethodRepository.findByName(anyString())).thenReturn(Optional.empty()); // No payment method found

            when(paymentMethodRepository.save(any(PaymentMethod.class))).thenAnswer(invocation -> {
                PaymentMethod newPaymentMethod = invocation.getArgument(0);
                newPaymentMethod.setId(UUID.randomUUID());
                return newPaymentMethod;
            });

            when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");

            when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
                User user = invocation.getArgument(0);
                user.setId(expectedUserId);
                return user;
            });

            // Act
            UUID result = userService.addUser(userDto);

            // Assert
            assertNotNull(result);
            verify(paymentMethodRepository).save(any(PaymentMethod.class)); // Payment method should be created
            verify(userRepository).save(any(User.class));
            assertEquals(expectedUserId, result);
        }
    }

    @Test
    void updateUser_Success() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UserDto userDto = mockUserDto();
        User existingUser = mockUser();
        Address mockAddress = mockAddress();
        PaymentMethod mockPaymentMethod = mockPaymentMethod();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(addressRepository.findByStreetAndNumberAndZipAndCityAndCountry(
                userDto.address().street(),
                userDto.address().number(),
                userDto.address().zip(),
                userDto.address().city(),
                userDto.address().country()
        )).thenReturn(Optional.of(mockAddress));
        when(paymentMethodRepository.findByName(userDto.paymentMethod().name()))
                .thenReturn(Optional.of(mockPaymentMethod));

        // Act
        userService.updateUser(userId, userDto);

        // Assert
        verify(userRepository, times(1)).findById(userId);
        verify(addressRepository, times(1)).findByStreetAndNumberAndZipAndCityAndCountry(
                userDto.address().street(),
                userDto.address().number(),
                userDto.address().zip(),
                userDto.address().city(),
                userDto.address().country()
        );
        verify(paymentMethodRepository, times(1)).findByName(userDto.paymentMethod().name());
        verify(userRepository, times(1)).save(existingUser);

    }

    @Test
    void updateUser_UserNotFound() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UserDto userDto = mockUserDto();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> userService.updateUser(userId, userDto)
        );

        assertEquals("User with ID " + userId + " not found", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verifyNoInteractions(addressRepository, paymentMethodRepository);

    }

    @Test
    void updateUser_InvalidCountryCode_ShouldThrowException() {
        // Arrange
        UserDto userDto = mockUserDto();
        UUID userId = UUID.randomUUID();

        try (MockedStatic<IsoUtil> mockedStatic = mockStatic(IsoUtil.class)) {
            mockedStatic.when(() -> IsoUtil.isValidISOCountry(userDto.address().country()))
                    .thenReturn(false);

            // Act & Assert
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->
                    userService.updateUser(userId, userDto));

            assertEquals("Invalid country code: " + userDto.address().country(), thrown.getMessage());

            verify(addressRepository, never()).findByStreetAndNumberAndZipAndCityAndCountry(
                    anyString(), anyInt(), anyInt(), anyString(), anyString()
            );
            verify(userRepository, never()).save(any(User.class));
        }
    }
    @Test
    void updateUser_NoAddressFound_ShouldCreateNewAddress() {
        UUID userId = UUID.randomUUID();
        UserDto userDto = mockUserDto();
        User user = mockUser();
        user.setId(userId);
        Address newAddress = new Address(null, "Main St", 1, 1010, "Vienna", "AT");

        try (MockedStatic<IsoUtil> mockedStatic = mockStatic(IsoUtil.class)) {
            mockedStatic.when(() -> IsoUtil.isValidISOCountry("AT")).thenReturn(true);

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(addressRepository.findByStreetAndNumberAndZipAndCityAndCountry(
                    "Main St", 1, 1010, "Vienna", "AT"
            )).thenReturn(Optional.empty()); // No existing address found
            when(addressRepository.save(any(Address.class))).thenReturn(newAddress);

            userService.updateUser(userId, userDto);

            assertEquals("Main St", user.getAddress().getStreet());
            assertEquals("Vienna", user.getAddress().getCity());

            verify(addressRepository).save(any(Address.class));
            verify(userRepository).save(user);
        }
    }

    @Test
    void updateUser_NoPaymentMethodFound_ShouldCreateNewPaymentMethod() {
        UUID userId = UUID.randomUUID();
        UserDto userDto = mockUserDto();
        User user = mockUser();
        user.setId(userId);
        PaymentMethod newPaymentMethod = new PaymentMethod(null, "New Payment");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(addressRepository.findByStreetAndNumberAndZipAndCityAndCountry(
                "Main St", 1, 1010, "Vienna", "AT"
        )).thenReturn(Optional.of(new Address(null, "Main St", 1, 1010, "Vienna", "AT")));
        when(paymentMethodRepository.findByName("Credit Card")).thenReturn(Optional.empty()); // No existing payment method
        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenReturn(newPaymentMethod);

        userService.updateUser(userId, userDto);

        assertEquals("New Payment", user.getPaymentMethod().getName());

        verify(paymentMethodRepository).save(any(PaymentMethod.class)); // Ensure new payment method is saved
        verify(userRepository).save(user);
    }

    @Test
    void updateUser_DataIntegrityViolation_ShouldThrowException() {
        UUID userId = UUID.randomUUID();
        UserDto userDto = mockUserDto();
        User user = mockUser();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(addressRepository.findByStreetAndNumberAndZipAndCityAndCountry(
                "Main St", 1, 1010, "Vienna", "AT"
        )).thenReturn(Optional.of(new Address(null, "Main St", 1, 1010, "Vienna", "AT")));
        when(paymentMethodRepository.findByName("Credit Card")).thenReturn(Optional.of(new PaymentMethod(null, "Credit Card")));

        doThrow(new DataIntegrityViolationException("User with the same username or email already exists"))
                .when(userRepository).save(any(User.class));

        DataIntegrityViolationException thrown = assertThrows(DataIntegrityViolationException.class, () ->
                userService.updateUser(userId, userDto));

        assertEquals("User with the same username or email already exists", thrown.getMessage());
    }

    @Test
    void updateUserProfile_ValidUpdates_ShouldUpdateUser() {
        // Arrange
        User user = mockUser();
        UUID userId = user.getId();
        Map<String, Object> updates = new HashMap<>();
        updates.put("gender", "female");
        updates.put("firstName", "Jane");
        updates.put("lastName", "Smith");
        updates.put("username", "janesmith");
        updates.put("email", "jane.smith@example.com");
        updates.put("dateOfBirth", "1995-05-10");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        userService.updateUserProfile(userId, updates);

        // Assert
        assertEquals("female", user.getGender());
        assertEquals("Jane", user.getFirstName());
        assertEquals("Smith", user.getLastName());
        assertEquals("janesmith", user.getUsername());
        assertEquals("jane.smith@example.com", user.getEmail());
        assertEquals(LocalDate.of(1995, 5, 10), user.getDateOfBirth());

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUserProfile_InvalidField_ShouldThrowException() {
        // Arrange
        User user = mockUser();
        UUID userId = user.getId();
        Map<String, Object> updates = new HashMap<>();
        updates.put("invalidField", "someValue");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->
                userService.updateUserProfile(userId, updates));

        assertEquals("Invalid field: invalidField", thrown.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUserProfile_NewAddress_ShouldBeCreated() {
        //Arrange
        User user = mockUser();
        UUID userId = user.getId();
        Map<String, Object> updates = new HashMap<>();
        Map<String, Object> addressMap = new HashMap<>();
        addressMap.put("street", "New St");
        addressMap.put("number", 2);
        addressMap.put("zip", 1234);
        addressMap.put("city", "Berlin");
        addressMap.put("country", "DE");

        updates.put("address", addressMap);

        Address newAddress = new Address(null, "New St", 2, 1234, "Berlin", "DE");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(addressRepository.findByStreetAndNumberAndZipAndCityAndCountry(
                "New St", 2, 1234, "Berlin", "DE")).thenReturn(Optional.empty());
        when(addressRepository.save(any(Address.class))).thenReturn(newAddress);

        // Mock the static method using try-with-resources
        try (MockedStatic<IsoUtil> mockedStatic = mockStatic(IsoUtil.class)) {
            mockedStatic.when(() -> IsoUtil.isValidISOCountry("DE")).thenReturn(true);

            // Act
            userService.updateUserProfile(userId, updates);

            // Assert
            assertEquals("New St", user.getAddress().getStreet());
            assertEquals("Berlin", user.getAddress().getCity());
            verify(addressRepository).save(any(Address.class));
            verify(userRepository).save(user);
        }
    }
    @Test
    void updateUserProfile_NewPaymentMethod_ShouldBeCreated() {
        // Arrange
        User user = mockUser();
        UUID userId = user.getId();
        Map<String, Object> updates = new HashMap<>();
        updates.put("paymentMethodName", "PayPal");

        PaymentMethod newPaymentMethod = new PaymentMethod(null, "PayPal");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(paymentMethodRepository.findByName("PayPal")).thenReturn(Optional.empty());
        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenReturn(newPaymentMethod);

        // Act
        userService.updateUserProfile(userId, updates);

        // Assert
        assertEquals("PayPal", user.getPaymentMethod().getName());
        verify(paymentMethodRepository).save(any(PaymentMethod.class));
        verify(userRepository).save(user);
    }

    @Test
    void updateUserProfile_DuplicateEmailOrUsername_ShouldThrowException() {
        // Arrange
        User user = mockUser();
        UUID userId = user.getId();
        Map<String, Object> updates = new HashMap<>();
        updates.put("email", "john.doe@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doThrow(new DataIntegrityViolationException("User with the same username or email already exists"))
                .when(userRepository).save(any(User.class));

        // Act & Assert
        DataIntegrityViolationException thrown = assertThrows(DataIntegrityViolationException.class, () ->
                userService.updateUserProfile(userId, updates));

        assertEquals("User with the same username or email already exists", thrown.getMessage());
    }

    @Test
    void updateUserProfile_InvalidCountryCode_ShouldThrowException() {
        // Arrange
        User user = mockUser();
        UUID userId = user.getId();
        Map<String, Object> updates = new HashMap<>();
        Map<String, Object> addressMap = new HashMap<>();
        addressMap.put("street", "New St");
        addressMap.put("number", 2);
        addressMap.put("zip", 1234);
        addressMap.put("city", "Berlin");
        addressMap.put("country", "INVALID"); // Invalid country code

        updates.put("address", addressMap);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Mock the static method
        try (MockedStatic<IsoUtil> mockedStatic = mockStatic(IsoUtil.class)) {
            mockedStatic.when(() -> IsoUtil.isValidISOCountry("INVALID")).thenReturn(false);

            // Act & Assert
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->
                    userService.updateUserProfile(userId, updates));

            assertEquals("Invalid country code: INVALID", thrown.getMessage());
            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Test
    void updateUserProfilePicture_Success() {
        // Arrange
        User user = mockUser();
        UUID userId = user.getId();
        UUID pictureId = UUID.randomUUID();
        PictureDto pictureDto = new PictureDto(pictureId);
        Picture picture = new Picture(pictureId, "external id", "profile.jpg", "content type");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(pictureService.upload(file)).thenReturn(pictureDto);
        when(pictureService.findById(pictureDto.id())).thenReturn(picture);

        // Act
        userService.updateUserProfilePicture(userId, file);

        // Assert
        assertEquals(picture, user.getProfilePicture());
        verify(userRepository).save(user);
    }

    @Test
    void updateUserProfilePicture_UserNotFound() {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () ->
                userService.updateUserProfilePicture(userId, file));

        assertEquals("User with ID " + userId + " not found", thrown.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUserProfilePicture_PictureNotFound() {
        // Arrange
        User user = mockUser();
        UUID userId = user.getId();
        UUID pictureId = UUID.randomUUID();
        PictureDto pictureDto = new PictureDto(pictureId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(pictureService.upload(file)).thenReturn(pictureDto);
        when(pictureService.findById(pictureDto.id()))
                .thenThrow(new EntityNotFoundException("Picture with ID " + pictureDto.id() + " not found"));

        // Act & Assert
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () ->
                userService.updateUserProfilePicture(userId, file));

        assertEquals("Picture with ID " + pictureDto.id() + " not found", thrown.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }


}
