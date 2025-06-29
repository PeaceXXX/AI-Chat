package com.talk.backend.repository;

import com.talk.backend.model.Friend;
import com.talk.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    
    List<Friend> findByUserOrderByIsFavoriteDescLastInteractionDesc(User user);
    
    List<Friend> findByUserAndIsFavoriteTrue(User user);
    
    @Query("SELECT f FROM Friend f WHERE f.user.id = :userId AND f.friend.id = :friendId")
    Optional<Friend> findByUserIdAndFriendId(@Param("userId") Long userId, @Param("friendId") Long friendId);
    
    @Query("SELECT f FROM Friend f WHERE (f.user.id = :userId AND f.friend.id = :friendId) OR (f.user.id = :friendId AND f.friend.id = :userId)")
    List<Friend> findFriendshipBetweenUsers(@Param("userId") Long userId, @Param("friendId") Long friendId);
    
    boolean existsByUserAndFriend(User user, User friend);
    
    @Query("SELECT COUNT(f) > 0 FROM Friend f WHERE (f.user.id = :userId AND f.friend.id = :friendId) OR (f.user.id = :friendId AND f.friend.id = :userId)")
    boolean areFriends(@Param("userId") Long userId, @Param("friendId") Long friendId);
    
    @Query("SELECT f.friend FROM Friend f WHERE f.user.id = :userId ORDER BY f.isFavorite DESC, f.lastInteraction DESC")
    List<User> findFriendsByUserId(@Param("userId") Long userId);
} 