package at.fhtw.bweng.controller;

import at.fhtw.bweng.dto.UserDto;
import at.fhtw.bweng.model.User;
import at.fhtw.bweng.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;


@RestController
public class UserController {

    private UserService userService;

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

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {

            userService.deleteUser(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "User deleted successfully");
            response.put("id", id.toString());
            return ResponseEntity.ok(response);

    }

}
