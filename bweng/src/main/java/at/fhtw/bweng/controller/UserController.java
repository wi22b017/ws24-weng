package at.fhtw.bweng.controller;

import at.fhtw.bweng.dto.UserDto;
import at.fhtw.bweng.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.*;


@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    // protected in the Security Config
    public ResponseEntity<?> addUser(@RequestBody @Valid UserDto userDto) {
            UUID uuid = userService.addUser(userDto);
            Map<String, String> response = new HashMap<>();
            response.put("message", "User added successfully");
            response.put("uuid", uuid.toString());
            return ResponseEntity.created(URI.create("/users/" + uuid.toString())).body(response);
    }

    @GetMapping(value = {"/users", "/users/{id}"})
    @PreAuthorize("hasPermission(#id, 'at.fhtw.bweng.model.User','read')") // Relies on custom UserPermission logic
    public ResponseEntity<?> getUsers(@PathVariable(required = false) UUID id) {
        Object result = userService.getUsers(id);
        return ResponseEntity.ok(result);
    }

    // Not used by the frontend
    @PutMapping("/users/{id}")
    @PreAuthorize("hasPermission(#id, 'at.fhtw.bweng.model.User','update')") // Relies on custom UserPermission logic
    public ResponseEntity<?> updateUser(@PathVariable UUID id, @RequestBody @Valid UserDto userDto) {
            userService.updateUser(id, userDto);
            Map<String, String> response = new HashMap<>();
            response.put("message", "User updated successfully");
            response.put("id", id.toString());
            return ResponseEntity.ok(response);
    }

    @DeleteMapping("/users/{id}")
    // protected in the Security Config
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
            userService.deleteUser(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "User deleted successfully");
            response.put("id", id.toString());
            return ResponseEntity.ok(response);
    }

    @PatchMapping("/users/{id}")
    @PreAuthorize("hasPermission(#id, 'at.fhtw.bweng.model.User','update')") // Relies on custom UserPermission logic
    public ResponseEntity<?> updateUserProfile(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        userService.updateUserProfile(id, updates);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User profile updated successfully");
        response.put("id", id.toString());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/users/{id}/password")
    @PreAuthorize("hasPermission(#id, 'at.fhtw.bweng.model.User','update')") // Relies on custom UserPermission logic
    public ResponseEntity<?> updateUserPassword(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        userService.updateUserPassword(id, updates);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User password updated successfully");
        response.put("id", id.toString());
        return ResponseEntity.ok(response);
    }

    @PostMapping("users/{id}/picture")
    @PreAuthorize("hasPermission(#id, 'at.fhtw.bweng.model.User','update')") // Relies on custom UserPermission logic
    public ResponseEntity<?> uploadProfilePicture(@PathVariable UUID id, @RequestParam("file") MultipartFile file) {
        userService.updateUserProfilePicture(id, file);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Profile picture updated successfully");
        response.put("id", id.toString());
        return ResponseEntity.ok(response);
    }
}
