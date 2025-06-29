package com.talk.backend.dto;

import com.talk.backend.model.FriendRequest;
import java.time.LocalDateTime;

public class FriendRequestDto {
    private Long id;
    private UserDto sender;
    private UserDto receiver;
    private FriendRequest.Status status;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public FriendRequestDto() {}
    
    public FriendRequestDto(Long id, UserDto sender, UserDto receiver, FriendRequest.Status status, String message, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.status = status;
        this.message = message;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public UserDto getSender() {
        return sender;
    }
    
    public void setSender(UserDto sender) {
        this.sender = sender;
    }
    
    public UserDto getReceiver() {
        return receiver;
    }
    
    public void setReceiver(UserDto receiver) {
        this.receiver = receiver;
    }
    
    public FriendRequest.Status getStatus() {
        return status;
    }
    
    public void setStatus(FriendRequest.Status status) {
        this.status = status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
} 