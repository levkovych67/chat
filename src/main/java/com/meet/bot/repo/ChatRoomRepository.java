package com.meet.bot.repo;

import com.meet.bot.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT cr FROM ChatRoom cr JOIN cr.users u WHERE u.id = :userId")
    List<ChatRoom> findByUserId(@Param("userId") Long userId);

    @Query("SELECT cr FROM ChatRoom cr JOIN cr.users u GROUP BY cr.id HAVING COUNT(u) = 1")
    List<ChatRoom> findChatRoomsWithUserCountOne();

}