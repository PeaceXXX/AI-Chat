package com.talk.backend.repository;

import com.talk.backend.model.FriendRequest;
import com.talk.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    
    List<FriendRequest> findByReceiverAndStatus(User receiver, FriendRequest.Status status);
    
    List<FriendRequest> findBySenderAndStatus(User sender, FriendRequest.Status status);
    
    @Query("SELECT fr FROM FriendRequest fr WHERE fr.receiver.id = :userId AND fr.status = 'PENDING'")
    List<FriendRequest> findPendingRequestsByReceiverId(@Param("userId") Long userId);
    
    @Query("SELECT fr FROM FriendRequest fr WHERE fr.sender.id = :userId AND fr.status = 'PENDING'")
    List<FriendRequest> findPendingRequestsBySenderId(@Param("userId") Long userId);
    
    @Query("SELECT fr FROM FriendRequest fr WHERE (fr.sender.id = :userId AND fr.receiver.id = :friendId) OR (fr.sender.id = :friendId AND fr.receiver.id = :userId)")
    List<FriendRequest> findRequestsBetweenUsers(@Param("userId") Long userId, @Param("friendId") Long friendId);
    
    @Query("SELECT fr FROM FriendRequest fr WHERE fr.sender.id = :senderId AND fr.receiver.id = :receiverId AND fr.status = 'PENDING'")
    Optional<FriendRequest> findPendingRequest(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);
    
    boolean existsBySenderAndReceiverAndStatus(User sender, User receiver, FriendRequest.Status status);
    
    @Query("SELECT COUNT(fr) > 0 FROM FriendRequest fr WHERE ((fr.sender.id = :userId AND fr.receiver.id = :friendId) OR (fr.sender.id = :friendId AND fr.receiver.id = :userId)) AND fr.status = 'PENDING'")
    boolean hasPendingRequest(@Param("userId") Long userId, @Param("friendId") Long friendId);
} 