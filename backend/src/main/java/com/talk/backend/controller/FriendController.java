package com.talk.backend.controller;

import com.talk.backend.dto.FriendDto;
import com.talk.backend.dto.UserDto;
import com.talk.backend.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/friends")
@CrossOrigin(origins = "*")
public class FriendController {
    
    @Autowired
    private FriendService friendService;
    
    @GetMapping("/{userId}")
    public ResponseEntity<List<FriendDto>> getFriendsByUserId(@PathVariable Long userId) {
        List<FriendDto> friends = friendService.getFriendsByUserId(userId);
        return ResponseEntity.ok(friends);
    }
    
    @GetMapping("/{userId}/users")
    public ResponseEntity<List<UserDto>> getFriendsAsUsers(@PathVariable Long userId) {
        List<UserDto> friends = friendService.getFriendsAsUsers(userId);
        return ResponseEntity.ok(friends);
    }
    
    @GetMapping("/{userId}/favorites")
    public ResponseEntity<List<FriendDto>> getFavoriteFriends(@PathVariable Long userId) {
        List<FriendDto> favorites = friendService.getFavoriteFriends(userId);
        return ResponseEntity.ok(favorites);
    }
    
    @GetMapping("/{userId}/{friendId}")
    public ResponseEntity<FriendDto> getFriendship(@PathVariable Long userId, @PathVariable Long friendId) {
        Optional<FriendDto> friendship = friendService.getFriendship(userId, friendId);
        return friendship.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{userId}/{friendId}/check")
    public ResponseEntity<Boolean> areFriends(@PathVariable Long userId, @PathVariable Long friendId) {
        boolean areFriends = friendService.areFriends(userId, friendId);
        return ResponseEntity.ok(areFriends);
    }
    
    @PostMapping("/{userId}/{friendId}")
    public ResponseEntity<FriendDto> addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        try {
            FriendDto newFriend = friendService.addFriend(userId, friendId);
            return ResponseEntity.ok(newFriend);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{userId}/{friendId}")
    public ResponseEntity<Void> removeFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        boolean removed = friendService.removeFriend(userId, friendId);
        return removed ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{userId}/{friendId}/nickname")
    public ResponseEntity<FriendDto> updateNickname(
            @PathVariable Long userId,
            @PathVariable Long friendId,
            @RequestParam String nickname) {
        Optional<FriendDto> updatedFriend = friendService.updateFriendNickname(userId, friendId, nickname);
        return updatedFriend.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{userId}/{friendId}/favorite")
    public ResponseEntity<FriendDto> toggleFavorite(@PathVariable Long userId, @PathVariable Long friendId) {
        Optional<FriendDto> updatedFriend = friendService.toggleFavorite(userId, friendId);
        return updatedFriend.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{userId}/{friendId}/interaction")
    public ResponseEntity<FriendDto> updateLastInteraction(@PathVariable Long userId, @PathVariable Long friendId) {
        Optional<FriendDto> updatedFriend = friendService.updateLastInteraction(userId, friendId);
        return updatedFriend.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
} 