package com.talk.backend.dto;

import java.time.LocalDateTime;

public class FriendDto {
    private Long id;
    private UserDto friend;
    private LocalDateTime createdAt;
    private String nickname;
    private Boolean isFavorite;
    private LocalDateTime lastInteraction;
    
    // Constructors
    public FriendDto() {}
    
    public FriendDto(Long id, UserDto friend, LocalDateTime createdAt, String nickname, Boolean isFavorite, LocalDateTime lastInteraction) {
        this.id = id;
        this.friend = friend;
        this.createdAt = createdAt;
        this.nickname = nickname;
        this.isFavorite = isFavorite;
        this.lastInteraction = lastInteraction;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public UserDto getFriend() {
        return friend;
    }
    
    public void setFriend(UserDto friend) {
        this.friend = friend;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getNickname() {
        return nickname;
    }
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public Boolean getIsFavorite() {
        return isFavorite;
    }
    
    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }
    
    public LocalDateTime getLastInteraction() {
        return lastInteraction;
    }
    
    public void setLastInteraction(LocalDateTime lastInteraction) {
        this.lastInteraction = lastInteraction;
    }
} 