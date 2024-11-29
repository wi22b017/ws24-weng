package at.fhtw.bweng.controller;

import at.fhtw.bweng.dto.UserDto;
import at.fhtw.bweng.model.User;
import at.fhtw.bweng.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.*;


@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<?> addUser(@RequestBody @Valid UserDto userDto) {

            UUID uuid = userService.addUser(userDto);
            Map<String, String> response = new HashMap<>();
            response.put("message", "User added successfully");
            response.put("uuid", uuid.toString());
            return ResponseEntity.created(URI.create("/users/" + uuid.toString())).body(response);

    }

    @GetMapping(value = {"/users", "/users/{id}"})
    public ResponseEntity<?> getUsers(@PathVariable(required = false) UUID id) {
        if (id != null) {

                User user = userService.getUserById(id);
                return ResponseEntity.ok(user);

        } else {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        }
    }

    // Not used by the frontend
    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable UUID id, @RequestBody @Valid UserDto userDto) {

            userService.updateUser(id, userDto);
            Map<String, String> response = new HashMap<>();

            response.put("message", "User updated successfully");
            response.put("id", id.toString());
            return ResponseEntity.ok(response);

    }
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {

            userService.deleteUser(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "User deleted successfully");
            response.put("id", id.toString());
            return ResponseEntity.ok(response);

    }

    @PatchMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userPermissionEvaluator.canModify(authentication, #id)")
    public ResponseEntity<?> updateUser(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        if (updates.isEmpty()) {
            return ResponseEntity.badRequest().body("No fields provided to update");
        }
        if (updates.containsKey("status")) {
            String status = (String) updates.get("status");
            userService.updateUserStatus(id, status);
            Map<String, String> response = new HashMap<>();
            response.put("message", "User status updated successfully");
            response.put("id", id.toString());
            return ResponseEntity.ok(response);
        } else {
            userService.updateUserProfile(id, updates);
            Map<String, String> response = new HashMap<>();
            response.put("message", "User profile updated successfully");
            response.put("id", id.toString());
            return ResponseEntity.ok(response);
        }
    }

    @PatchMapping("/users/{id}/password")
    @PreAuthorize("hasRole('ADMIN') or @userPermissionEvaluator.canModify(authentication, #id)")
    public ResponseEntity<?> updateUserPassword(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        // Check if required fields are present
        if (!updates.containsKey("currentPassword") || !updates.containsKey("newPassword")) {
            return ResponseEntity.badRequest().body("Missing required fields: currentPassword and/or newPassword");
        }

        String currentPassword = (String) updates.get("currentPassword");
        String newPassword =(String) updates.get("newPassword");
        userService.updateUserPassword(id, currentPassword, newPassword);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Password updated successfully");
        response.put("id", id.toString());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/users/test-patch")
    public ResponseEntity<?> testPatch() {
        return ResponseEntity.ok("PATCH method is working!");
    }

}
