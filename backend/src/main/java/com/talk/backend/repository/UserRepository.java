package com.talk.backend.repository;

import com.talk.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByPhoneNumber(String phoneNumber);
    
    Optional<User> findByGoogleId(String googleId);
    
    @Query("SELECT u FROM User u WHERE u.username LIKE %:searchTerm% OR u.displayName LIKE %:searchTerm% OR u.email LIKE %:searchTerm%")
    List<User> searchUsers(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT u FROM User u WHERE u.id NOT IN (SELECT f.friend.id FROM Friend f WHERE f.user.id = :userId)")
    List<User> findUsersNotInFriendsList(@Param("userId") Long userId);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    boolean existsByPhoneNumber(String phoneNumber);
    
    boolean existsByGoogleId(String googleId);
} 