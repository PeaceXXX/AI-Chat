package com.talk.backend.controller;

import com.talk.backend.dto.FriendRequestDto;
import com.talk.backend.service.FriendRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/friend-requests")
@CrossOrigin(origins = "*")
public class FriendRequestController {
    
    @Autowired
    private FriendRequestService friendRequestService;
    
    @GetMapping("/received/{receiverId}")
    public ResponseEntity<List<FriendRequestDto>> getPendingRequestsByReceiverId(@PathVariable Long receiverId) {
        List<FriendRequestDto> requests = friendRequestService.getPendingRequestsByReceiverId(receiverId);
        return ResponseEntity.ok(requests);
    }
    
    @GetMapping("/sent/{senderId}")
    public ResponseEntity<List<FriendRequestDto>> getPendingRequestsBySenderId(@PathVariable Long senderId) {
        List<FriendRequestDto> requests = friendRequestService.getPendingRequestsBySenderId(senderId);
        return ResponseEntity.ok(requests);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<FriendRequestDto> getRequestById(@PathVariable Long id) {
        Optional<FriendRequestDto> request = friendRequestService.getRequestById(id);
        return request.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/{senderId}/{receiverId}")
    public ResponseEntity<FriendRequestDto> sendFriendRequest(
            @PathVariable Long senderId,
            @PathVariable Long receiverId,
            @RequestParam(required = false) String message) {
        try {
            FriendRequestDto request = friendRequestService.sendFriendRequest(senderId, receiverId, message);
            return ResponseEntity.ok(request);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{requestId}/accept")
    public ResponseEntity<FriendRequestDto> acceptFriendRequest(@PathVariable Long requestId) {
        try {
            FriendRequestDto request = friendRequestService.acceptFriendRequest(requestId);
            return ResponseEntity.ok(request);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{requestId}/reject")
    public ResponseEntity<FriendRequestDto> rejectFriendRequest(@PathVariable Long requestId) {
        try {
            FriendRequestDto request = friendRequestService.rejectFriendRequest(requestId);
            return ResponseEntity.ok(request);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{requestId}/cancel")
    public ResponseEntity<FriendRequestDto> cancelFriendRequest(
            @PathVariable Long requestId,
            @RequestParam Long userId) {
        try {
            FriendRequestDto request = friendRequestService.cancelFriendRequest(requestId, userId);
            return ResponseEntity.ok(request);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{requestId}")
    public ResponseEntity<Void> deleteFriendRequest(@PathVariable Long requestId) {
        boolean deleted = friendRequestService.deleteFriendRequest(requestId);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/{userId}/{friendId}/history")
    public ResponseEntity<List<FriendRequestDto>> getRequestsBetweenUsers(
            @PathVariable Long userId,
            @PathVariable Long friendId) {
        List<FriendRequestDto> requests = friendRequestService.getRequestsBetweenUsers(userId, friendId);
        return ResponseEntity.ok(requests);
    }
    
    @GetMapping("/{userId}/{friendId}/pending")
    public ResponseEntity<Boolean> hasPendingRequest(@PathVariable Long userId, @PathVariable Long friendId) {
        boolean hasPending = friendRequestService.hasPendingRequest(userId, friendId);
        return ResponseEntity.ok(hasPending);
    }
} 