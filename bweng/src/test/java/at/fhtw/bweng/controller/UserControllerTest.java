package at.fhtw.bweng.controller;

import at.fhtw.bweng.dto.UserDto;
import at.fhtw.bweng.dto.AddressDto;
import at.fhtw.bweng.dto.PaymentMethodDto;
import at.fhtw.bweng.model.Address;
import at.fhtw.bweng.model.PaymentMethod;
import at.fhtw.bweng.model.User;
import at.fhtw.bweng.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private static final UUID USER_ID = UUID.randomUUID();
    private static final AddressDto ADDRESS_DTO = new AddressDto("Street", 1, 1050, "Vienna", "Austria");
    private static final PaymentMethodDto PAYMENT_METHOD_DTO = new PaymentMethodDto("Credit Card");
    private static final UserDto USER_DTO = new UserDto(
            "Male",
            "John",
            "Doe",
            "johndoe",
            "Password1",
            "john.doe@example.com",
            "1990-01-01",
            "USER",
            "ACTIVE",
            ADDRESS_DTO,
            PAYMENT_METHOD_DTO
    );
    private static final User USER = new User(
            USER_ID,
            "Male",
            "John",
            "Doe",
            "johndoe",
            "Password1",
            "john.doe@example.com",
            LocalDate.of(1990, 1, 1),
            "USER",
            "ACTIVE",
            new Address(UUID.randomUUID(), "Main Street", 5, 1060, "Vienna", "Austria"),
            new PaymentMethod(UUID.randomUUID(), "Bank transfer")
    );

    @Test
    void addUser_returnsCreatedResponse() {
        // Arrange
        when(userService.addUser(USER_DTO)).thenReturn(USER_ID);

        // Act
        ResponseEntity<?> response = userController.addUser(USER_DTO);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(201);
        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertThat(responseBody).containsEntry("message", "User added successfully");
        assertThat(responseBody).containsEntry("uuid", USER_ID.toString());
    }

    @Test
    void getUsers_withoutId_returnsAllUsers() {
        // Arrange
        when(userService.getUsers(null)).thenReturn(List.of(USER));

        // Act
        ResponseEntity<?> response = userController.getUsers(null);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(List.of(USER));
    }

    @Test
    void getUsers_withId_returnsSpecificUser() {
        // Arrange
        when(userService.getUsers(USER_ID)).thenReturn(USER);

        // Act
        ResponseEntity<?> response = userController.getUsers(USER_ID);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(USER);
    }

    @Test
    void updateUser_returnsOkResponse() {
        // Arrange
        doNothing().when(userService).updateUser(USER_ID, USER_DTO);

        // Act
        ResponseEntity<?> response = userController.updateUser(USER_ID, USER_DTO);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertThat(responseBody).containsEntry("message", "User updated successfully");
        assertThat(responseBody).containsEntry("id", USER_ID.toString());
    }

    @Test
    void deleteUser_returnsOkResponse() {
        // Arrange
        doNothing().when(userService).deleteUser(USER_ID);

        // Act
        ResponseEntity<?> response = userController.deleteUser(USER_ID);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertThat(responseBody).containsEntry("message", "User deleted successfully");
        assertThat(responseBody).containsEntry("id", USER_ID.toString());
    }

    @Test
    void updateUserProfile_returnsOkResponse() {
        // Arrange
        Map<String, Object> updates = new HashMap<>();
        updates.put("firstName", "Jane");
        doNothing().when(userService).updateUserProfile(USER_ID, updates);

        // Act
        ResponseEntity<?> response = userController.updateUserProfile(USER_ID, updates);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertThat(responseBody).containsEntry("message", "User profile updated successfully");
        assertThat(responseBody).containsEntry("id", USER_ID.toString());
    }

    @Test
    void updateUserPassword_returnsOkResponse() {
        // Arrange
        Map<String, Object> updates = new HashMap<>();
        updates.put("currentPassword", "currentPassword1234");
        updates.put("newPassword", "newPassword1234");
        doNothing().when(userService).updateUserPassword(USER_ID, updates);

        // Act
        ResponseEntity<?> response = userController.updateUserPassword(USER_ID, updates);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertThat(responseBody).containsEntry("message", "User password updated successfully");
        assertThat(responseBody).containsEntry("id", USER_ID.toString());
    }

    @Test
    void uploadProfilePicture_returnsOkResponse() {
        // Arrange
        MockMultipartFile file = new MockMultipartFile("file", "image.jpg", "image/jpeg", "mock image content".getBytes());
        doNothing().when(userService).updateUserProfilePicture(USER_ID, file);

        // Act
        ResponseEntity<?> response = userController.uploadProfilePicture(USER_ID, file);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertThat(responseBody).containsEntry("message", "Profile picture updated successfully");
        assertThat(responseBody).containsEntry("id", USER_ID.toString());
    }
}