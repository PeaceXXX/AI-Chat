package com.talk.backend.service;

import com.talk.backend.dto.FriendDto;
import com.talk.backend.dto.UserDto;
import com.talk.backend.model.Friend;
import com.talk.backend.model.User;
import com.talk.backend.repository.FriendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FriendService {
    
    @Autowired
    private FriendRepository friendRepository;
    
    @Autowired
    private UserService userService;
    
    public List<FriendDto> getFriendsByUserId(Long userId) {
        return friendRepository.findByUserOrderByIsFavoriteDescLastInteractionDesc(
                userService.getUserEntityById(userId).orElse(null)
        ).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<UserDto> getFriendsAsUsers(Long userId) {
        return friendRepository.findFriendsByUserId(userId).stream()
                .map(user -> new UserDto(
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
                ))
                .collect(Collectors.toList());
    }
    
    public List<FriendDto> getFavoriteFriends(Long userId) {
        return friendRepository.findByUserAndIsFavoriteTrue(
                userService.getUserEntityById(userId).orElse(null)
        ).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public Optional<FriendDto> getFriendById(Long id) {
        return friendRepository.findById(id)
                .map(this::convertToDto);
    }
    
    public Optional<FriendDto> getFriendship(Long userId, Long friendId) {
        return friendRepository.findByUserIdAndFriendId(userId, friendId)
                .map(this::convertToDto);
    }
    
    public boolean areFriends(Long userId, Long friendId) {
        return friendRepository.areFriends(userId, friendId);
    }
    
    public FriendDto addFriend(Long userId, Long friendId) {
        User user = userService.getUserEntityById(userId).orElse(null);
        User friend = userService.getUserEntityById(friendId).orElse(null);
        
        if (user == null || friend == null) {
            throw new IllegalArgumentException("User or friend not found");
        }
        
        if (user.getId().equals(friend.getId())) {
            throw new IllegalArgumentException("Cannot add yourself as a friend");
        }
        
        if (friendRepository.existsByUserAndFriend(user, friend)) {
            throw new IllegalArgumentException("Friendship already exists");
        }
        
        Friend newFriend = new Friend(user, friend);
        Friend savedFriend = friendRepository.save(newFriend);
        
        // Create reverse friendship
        Friend reverseFriend = new Friend(friend, user);
        friendRepository.save(reverseFriend);
        
        return convertToDto(savedFriend);
    }
    
    public boolean removeFriend(Long userId, Long friendId) {
        Optional<Friend> friendship = friendRepository.findByUserIdAndFriendId(userId, friendId);
        Optional<Friend> reverseFriendship = friendRepository.findByUserIdAndFriendId(friendId, userId);
        
        if (friendship.isPresent()) {
            friendRepository.delete(friendship.get());
        }
        
        if (reverseFriendship.isPresent()) {
            friendRepository.delete(reverseFriendship.get());
        }
        
        return friendship.isPresent() || reverseFriendship.isPresent();
    }
    
    public Optional<FriendDto> updateFriendNickname(Long userId, Long friendId, String nickname) {
        return friendRepository.findByUserIdAndFriendId(userId, friendId)
                .map(friend -> {
                    friend.setNickname(nickname);
                    return convertToDto(friendRepository.save(friend));
                });
    }
    
    public Optional<FriendDto> toggleFavorite(Long userId, Long friendId) {
        return friendRepository.findByUserIdAndFriendId(userId, friendId)
                .map(friend -> {
                    friend.setIsFavorite(!friend.getIsFavorite());
                    return convertToDto(friendRepository.save(friend));
                });
    }
    
    public Optional<FriendDto> updateLastInteraction(Long userId, Long friendId) {
        return friendRepository.findByUserIdAndFriendId(userId, friendId)
                .map(friend -> {
                    friend.setLastInteraction(LocalDateTime.now());
                    return convertToDto(friendRepository.save(friend));
                });
    }
    
    // Helper method to convert Friend entity to FriendDto
    private FriendDto convertToDto(Friend friend) {
        UserDto friendUser = new UserDto(
                friend.getFriend().getId(),
                friend.getFriend().getUsername(),
                friend.getFriend().getEmail(),
                friend.getFriend().getDisplayName(),
                friend.getFriend().getAvatarUrl(),
                friend.getFriend().getPhoneNumber(),
                friend.getFriend().getCreatedAt(),
                friend.getFriend().getIsOnline(),
                friend.getFriend().getLastSeen(),
                friend.getFriend().getGoogleId(),
                friend.getFriend().getAuthProvider(),
                friend.getFriend().getEmailVerified()
        );
        
        return new FriendDto(
                friend.getId(),
                friendUser,
                friend.getCreatedAt(),
                friend.getNickname(),
                friend.getIsFavorite(),
                friend.getLastInteraction()
        );
    }
} 