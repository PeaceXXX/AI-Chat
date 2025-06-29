package com.talk.backend.service;

import com.talk.backend.dto.UserDto;
import com.talk.backend.model.User;
import com.talk.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::convertToDto);
    }
    
    public Optional<UserDto> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::convertToDto);
    }
    
    public Optional<UserDto> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::convertToDto);
    }
    
    public Optional<UserDto> getUserByGoogleId(String googleId) {
        return userRepository.findByGoogleId(googleId)
                .map(this::convertToDto);
    }
    
    public List<UserDto> searchUsers(String searchTerm) {
        return userRepository.searchUsers(searchTerm).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<UserDto> getUsersNotInFriendsList(Long userId) {
        return userRepository.findUsersNotInFriendsList(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public UserDto createUser(User user) {
        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }
    
    public UserDto createOrUpdateGoogleUser(String googleId, String email, String displayName, String avatarUrl) {
        Optional<User> existingUser = userRepository.findByGoogleId(googleId);
        
        if (existingUser.isPresent()) {
            // Update existing user
            User user = existingUser.get();
            user.setDisplayName(displayName);
            user.setAvatarUrl(avatarUrl);
            user.setEmail(email);
            user.setIsOnline(true);
            user.setLastSeen(java.time.LocalDateTime.now());
            return convertToDto(userRepository.save(user));
        } else {
            // Create new user
            User newUser = new User(googleId, email, displayName, avatarUrl);
            newUser.setIsOnline(true);
            newUser.setLastSeen(java.time.LocalDateTime.now());
            return convertToDto(userRepository.save(newUser));
        }
    }
    
    public Optional<UserDto> updateUser(Long id, User userDetails) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setUsername(userDetails.getUsername());
                    user.setEmail(userDetails.getEmail());
                    user.setDisplayName(userDetails.getDisplayName());
                    user.setAvatarUrl(userDetails.getAvatarUrl());
                    user.setPhoneNumber(userDetails.getPhoneNumber());
                    user.setIsOnline(userDetails.getIsOnline());
                    user.setLastSeen(userDetails.getLastSeen());
                    return convertToDto(userRepository.save(user));
                });
    }
    
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    public boolean existsByPhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }
    
    public boolean existsByGoogleId(String googleId) {
        return userRepository.existsByGoogleId(googleId);
    }
    
    // Helper method to convert User entity to UserDto
    private UserDto convertToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getDisplayName(),
                user.getAvatarUrl(),
                user.getPhoneNumber(),
                user.getCreatedAt(),
                user.getIsOnline(),
                user.getLastSeen(),
                user.getGoogleId(),
                user.getAuthProvider(),
                user.getEmailVerified()
        );
    }
    
    // Helper method to get User entity by ID
    public Optional<User> getUserEntityById(Long id) {
        return userRepository.findById(id);
    }
    
    // Helper method to get User entity by Google ID
    public Optional<User> getUserEntityByGoogleId(String googleId) {
        return userRepository.findByGoogleId(googleId);
    }
} 