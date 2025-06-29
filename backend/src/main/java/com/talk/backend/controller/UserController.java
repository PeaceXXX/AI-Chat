package com.talk.backend.controller;

import com.talk.backend.dto.UserDto;
import com.talk.backend.model.User;
import com.talk.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        Optional<UserDto> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/username/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        Optional<UserDto> user = userService.getUserByUsername(username);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        Optional<UserDto> user = userService.getUserByEmail(email);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/google/{googleId}")
    public ResponseEntity<UserDto> getUserByGoogleId(@PathVariable String googleId) {
        Optional<UserDto> user = userService.getUserByGoogleId(googleId);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> searchUsers(@RequestParam String q) {
        List<UserDto> users = userService.searchUsers(q);
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/{userId}/not-friends")
    public ResponseEntity<List<UserDto>> getUsersNotInFriendsList(@PathVariable Long userId) {
        List<UserDto> users = userService.getUsersNotInFriendsList(userId);
        return ResponseEntity.ok(users);
    }
    
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody User user) {
        UserDto createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }
    
    @PostMapping("/google-login")
    public ResponseEntity<UserDto> googleLogin(@RequestBody GoogleLoginRequest request) {
        UserDto user = userService.createOrUpdateGoogleUser(
                request.getGoogleId(),
                request.getEmail(),
                request.getDisplayName(),
                request.getAvatarUrl()
        );
        return ResponseEntity.ok(user);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        Optional<UserDto> updatedUser = userService.updateUser(id, userDetails);
        return updatedUser.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/check/username/{username}")
    public ResponseEntity<Boolean> checkUsernameExists(@PathVariable String username) {
        boolean exists = userService.existsByUsername(username);
        return ResponseEntity.ok(exists);
    }
    
    @GetMapping("/check/email/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }
    
    @GetMapping("/check/phone/{phoneNumber}")
    public ResponseEntity<Boolean> checkPhoneExists(@PathVariable String phoneNumber) {
        boolean exists = userService.existsByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(exists);
    }
    
    @GetMapping("/check/google/{googleId}")
    public ResponseEntity<Boolean> checkGoogleIdExists(@PathVariable String googleId) {
        boolean exists = userService.existsByGoogleId(googleId);
        return ResponseEntity.ok(exists);
    }
    
    // Inner class for Google login request
    public static class GoogleLoginRequest {
        private String googleId;
        private String email;
        private String displayName;
        private String avatarUrl;
        
        // Getters and Setters
        public String getGoogleId() {
            return googleId;
        }
        
        public void setGoogleId(String googleId) {
            this.googleId = googleId;
        }
        
        public String getEmail() {
            return email;
        }
        
        public void setEmail(String email) {
            this.email = email;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }
        
        public String getAvatarUrl() {
            return avatarUrl;
        }
        
        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }
    }
} 