package com.rookies3.myspringbootlab.controller;

import com.rookies3.myspringbootlab.controller.dto.UserDTO;
import com.rookies3.myspringbootlab.entity.User;
import com.rookies3.myspringbootlab.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping
    public UserDTO.UserResponse create(@Valid @RequestBody UserDTO.UserCreateRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        
        User savedUser = userService.createUser(user);
        return new UserDTO.UserResponse(savedUser);
    }

    @GetMapping("/{id}")
    public UserDTO.UserResponse getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return new UserDTO.UserResponse(user);
    }

    @GetMapping
    public List<UserDTO.UserResponse> getUsers() {
        return userService.getAllUsers().stream()
                //.map(user -> new UserDTO.UserResponse(user))
                .map(UserDTO.UserResponse::new)
                .toList();
                //.collect(Collectors.toList());
    }

    @PatchMapping("/email/{email}/")
    public UserDTO.UserResponse updateUser(@PathVariable String email,
                                           @Valid @RequestBody UserDTO.UserUpdateRequest request) {
        User userDetail = new User();
        userDetail.setName(request.getName());
        
        User updatedUser = userService.updateUserByEmail(email, userDetail);
        return new UserDTO.UserResponse(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("ID = " + id + " User Deleted OK");
    }
}