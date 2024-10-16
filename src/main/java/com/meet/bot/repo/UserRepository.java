package com.meet.bot.repo;

import com.meet.bot.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Find a user by username
    Optional<User> findByUsername(String username);

    // Check if a username already exists
    boolean existsByUsername(String username);

    // Find users by a partial username match
    List<User> findByUsernameContaining(String usernameSubstring);

    Optional<User> findByTelegramId(String telegramId);

    // Find users in a specific chat room
    @Query("SELECT u FROM User u JOIN u.chatRooms cr WHERE cr.id = :chatRoomId")
    List<User> findByChatRoomId(@Param("chatRoomId") Long chatRoomId);

    // Find users not in a specific chat room
    @Query("SELECT u FROM User u WHERE u NOT IN (SELECT u2 FROM User u2 JOIN u2.chatRooms cr WHERE cr.id = :chatRoomId)")
    List<User> findUsersNotInChatRoom(@Param("chatRoomId") Long chatRoomId);

    // Count users in a specific chat room
    @Query("SELECT COUNT(u) FROM User u JOIN u.chatRooms cr WHERE cr.id = :chatRoomId")
    long countByChatRoomId(@Param("chatRoomId") Long chatRoomId);


    @Query("SELECT DISTINCT u FROM User u " +
            "LEFT JOIN u.chatRooms cr " +
            "WHERE u.chatRooms IS EMPTY " +
            "OR (SIZE(cr.users) = 1)")
    List<User> findUsersWithoutChatRoomsOrInSingleUserChatRooms();
}
