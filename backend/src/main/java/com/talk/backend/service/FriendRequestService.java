package com.talk.backend.service;

import com.talk.backend.dto.FriendRequestDto;
import com.talk.backend.dto.UserDto;
import com.talk.backend.model.FriendRequest;
import com.talk.backend.model.User;
import com.talk.backend.repository.FriendRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FriendRequestService {
    
    @Autowired
    private FriendRequestRepository friendRequestRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private FriendService friendService;
    
    public List<FriendRequestDto> getPendingRequestsByReceiverId(Long receiverId) {
        return friendRequestRepository.findPendingRequestsByReceiverId(receiverId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<FriendRequestDto> getPendingRequestsBySenderId(Long senderId) {
        return friendRequestRepository.findPendingRequestsBySenderId(senderId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public Optional<FriendRequestDto> getRequestById(Long id) {
        return friendRequestRepository.findById(id)
                .map(this::convertToDto);
    }
    
    public FriendRequestDto sendFriendRequest(Long senderId, Long receiverId, String message) {
        User sender = userService.getUserEntityById(senderId).orElse(null);
        User receiver = userService.getUserEntityById(receiverId).orElse(null);
        
        if (sender == null || receiver == null) {
            throw new IllegalArgumentException("Sender or receiver not found");
        }
        
        if (sender.getId().equals(receiver.getId())) {
            throw new IllegalArgumentException("Cannot send friend request to yourself");
        }
        
        if (friendService.areFriends(senderId, receiverId)) {
            throw new IllegalArgumentException("Users are already friends");
        }
        
        if (friendRequestRepository.hasPendingRequest(senderId, receiverId)) {
            throw new IllegalArgumentException("Friend request already exists");
        }
        
        FriendRequest friendRequest = new FriendRequest(sender, receiver, message);
        FriendRequest savedRequest = friendRequestRepository.save(friendRequest);
        
        return convertToDto(savedRequest);
    }
    
    public FriendRequestDto acceptFriendRequest(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Friend request not found"));
        
        if (request.getStatus() != FriendRequest.Status.PENDING) {
            throw new IllegalArgumentException("Friend request is not pending");
        }
        
        request.setStatus(FriendRequest.Status.ACCEPTED);
        FriendRequest savedRequest = friendRequestRepository.save(request);
        
        // Add as friends
        friendService.addFriend(request.getSender().getId(), request.getReceiver().getId());
        
        return convertToDto(savedRequest);
    }
    
    public FriendRequestDto rejectFriendRequest(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Friend request not found"));
        
        if (request.getStatus() != FriendRequest.Status.PENDING) {
            throw new IllegalArgumentException("Friend request is not pending");
        }
        
        request.setStatus(FriendRequest.Status.REJECTED);
        FriendRequest savedRequest = friendRequestRepository.save(request);
        
        return convertToDto(savedRequest);
    }
    
    public FriendRequestDto cancelFriendRequest(Long requestId, Long userId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Friend request not found"));
        
        if (!request.getSender().getId().equals(userId)) {
            throw new IllegalArgumentException("Only sender can cancel the request");
        }
        
        if (request.getStatus() != FriendRequest.Status.PENDING) {
            throw new IllegalArgumentException("Friend request is not pending");
        }
        
        request.setStatus(FriendRequest.Status.CANCELLED);
        FriendRequest savedRequest = friendRequestRepository.save(request);
        
        return convertToDto(savedRequest);
    }
    
    public boolean deleteFriendRequest(Long requestId) {
        if (friendRequestRepository.existsById(requestId)) {
            friendRequestRepository.deleteById(requestId);
            return true;
        }
        return false;
    }
    
    public List<FriendRequestDto> getRequestsBetweenUsers(Long userId, Long friendId) {
        return friendRequestRepository.findRequestsBetweenUsers(userId, friendId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public boolean hasPendingRequest(Long userId, Long friendId) {
        return friendRequestRepository.hasPendingRequest(userId, friendId);
    }
    
    // Helper method to convert FriendRequest entity to FriendRequestDto
    private FriendRequestDto convertToDto(FriendRequest friendRequest) {
        UserDto sender = new UserDto(
                friendRequest.getSender().getId(),
                friendRequest.getSender().getUsername(),
                friendRequest.getSender().getEmail(),
                friendRequest.getSender().getDisplayName(),
                friendRequest.getSender().getAvatarUrl(),
                friendRequest.getSender().getPhoneNumber(),
                friendRequest.getSender().getCreatedAt(),
                friendRequest.getSender().getIsOnline(),
                friendRequest.getSender().getLastSeen(),
                friendRequest.getSender().getGoogleId(),
                friendRequest.getSender().getAuthProvider(),
                friendRequest.getSender().getEmailVerified()
        );
        
        UserDto receiver = new UserDto(
                friendRequest.getReceiver().getId(),
                friendRequest.getReceiver().getUsername(),
                friendRequest.getReceiver().getEmail(),
                friendRequest.getReceiver().getDisplayName(),
                friendRequest.getReceiver().getAvatarUrl(),
                friendRequest.getReceiver().getPhoneNumber(),
                friendRequest.getReceiver().getCreatedAt(),
                friendRequest.getReceiver().getIsOnline(),
                friendRequest.getReceiver().getLastSeen(),
                friendRequest.getReceiver().getGoogleId(),
                friendRequest.getReceiver().getAuthProvider(),
                friendRequest.getReceiver().getEmailVerified()
        );
        
        return new FriendRequestDto(
                friendRequest.getId(),
                sender,
                receiver,
                friendRequest.getStatus(),
                friendRequest.getMessage(),
                friendRequest.getCreatedAt(),
                friendRequest.getUpdatedAt()
        );
    }
} 