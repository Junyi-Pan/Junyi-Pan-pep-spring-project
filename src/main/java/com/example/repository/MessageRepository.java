package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long>{
    //Query for message based on messageId
    @Query("SELECT m FROM Message m WHERE m.messageId = ?1")
    Message findMessageByMessageId(Integer messageId);

    //Deleting a message based on messagedId
    @Modifying
    @Query("DELETE FROM Message WHERE messageId = ?1")
    int deleteMessageByMessageId(Integer messageId);

    //Updating a message based on messageId
    @Modifying
    @Query("UPDATE Message SET messageText = ?1 WHERE messageId = ?2")
    int updateMessageByMessageId(String messageText, Integer messageId);

    //Query for messages based on posted account
    @Query("SELECT m FROM Message m INNER JOIN Account a ON m.postedBy = a.accountId WHERE accountId = ?1")
    List<Message> findMessagesByAccountId(Integer accountId);
}
