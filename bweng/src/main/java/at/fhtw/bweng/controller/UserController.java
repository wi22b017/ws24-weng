package at.fhtw.bweng.controller;

import at.fhtw.bweng.dto.UserDto;
import at.fhtw.bweng.model.User;
import at.fhtw.bweng.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.SQLOutput;
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


    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable UUID id, @RequestBody @Valid UserDto userDto) {

            userService.updateUser(id, userDto);
            Map<String, String> response = new HashMap<>();

            response.put("message", "User updated successfully");
            response.put("id", id.toString());
            return ResponseEntity.ok(response);

    }

    @PatchMapping("/users/{id}/profile")
    @PreAuthorize("hasRole('ADMIN') or @userPermissionEvaluator.canModify(authentication, #id)")
    public ResponseEntity<?> updateUserProfile(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        if (updates.isEmpty()) {
            return ResponseEntity.badRequest().body("No fields provided to update");
        }

        userService.updateUserProfile(id, updates);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User profile updated successfully");
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
    public ResponseEntity<?> updateUserStatus(@PathVariable UUID id, @RequestBody Map<String, String> updates) {
        if (!updates.containsKey("status")) {
            return ResponseEntity.badRequest().body("Missing required field: status");
        }

        String status = updates.get("status");
        userService.updateUserStatus(id, status);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User status updated successfully");
        response.put("id", id.toString());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/users/test-patch")
    public ResponseEntity<?> testPatch() {
        return ResponseEntity.ok("PATCH method is working!");
    }

}
