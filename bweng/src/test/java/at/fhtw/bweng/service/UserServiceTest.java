package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.AddressDto;
import at.fhtw.bweng.dto.PaymentMethodDto;
import at.fhtw.bweng.dto.UserDto;
import at.fhtw.bweng.mapper.UserMapper;
import at.fhtw.bweng.model.Address;
import at.fhtw.bweng.model.PaymentMethod;
import at.fhtw.bweng.model.User;
import at.fhtw.bweng.repository.AddressRepository;
import at.fhtw.bweng.repository.PaymentMethodRepository;
import at.fhtw.bweng.repository.UserRepository;
import at.fhtw.bweng.util.IsoUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

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
                "1980-01-01",
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
    void getUsers_ById() {

    }

    @Test
    void getUsers_ReturnsAllUsers() {

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





}
