package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Message;
import com.example.exception.UserNotInDbException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
@Transactional (rollbackFor = UserNotInDbException.class)
public class MessageService {
    AccountRepository accountRepository;
    MessageRepository messageRepository;

    @Autowired
    public MessageService(AccountRepository accountRepository, MessageRepository messageRepository) {
        this.accountRepository = accountRepository;
        this.messageRepository = messageRepository;
    }

    /**
     * Persist new message object into database, with validation of posted by an account exist in database,
     * and validation of input field restrictions.
     * @param message the message object to be persisted.
     * @return The persisted message object if succesfull, null otherwise.
     * @throws UserNotInDbException if the postedBy id does not match any accounts in database.
     */
    public Message postMessage(Message message) throws UserNotInDbException{
        if (accountRepository.findAccountByAccountId(message.getPostedBy()) == null) throw new UserNotInDbException();
        if (message.getMessageText().length()<1 || message.getMessageText().length()>255) return null;
        else return messageRepository.save(message);
    }

    /**
     * Retrive all messages from the database.
     * @return All messages in database.
     */
    public List<Message> getAllMessage() {
        return messageRepository.findAll();
    }

    /**
     * Retrive the message object identified by messageId.
     * @param messageId the identifier of the requested message.
     * @return A message object if exists, null otherwise.
     */
    public Message getMessageByMessageId(Integer messageId) {
        return messageRepository.findMessageByMessageId(messageId);
    }

    /**
     * Delete the message object identified by the messageId.
     * @param messageId the indentifier of the requested message.
     * @return The number of rows changed in database in its body. null if no rows changed.
     */
    public Integer deleteMessage(Integer messageId) {
        Message message = getMessageByMessageId(messageId);
        if (message != null) {
             return messageRepository.deleteMessageByMessageId(messageId);
        }
        return null;
    }

    /**
     * Update the message object identified by the messageId with the contents in the new message object.
     * Verifies the message object exists, and the new message object's text complies with input restrictions.
     * @param messageId the indentifier of the requested message.
     * @param message the message object with values the original message will be changed to
     * @return The number of rows changed in database in its body. null if no rows changed.
     */
    public Integer updateMessage(Integer messageId, Message message) {
        if (message.getMessageText().length()<1 || message.getMessageText().length()>255) return null;
        Message check = getMessageByMessageId(messageId);
        if (check != null) {
            return messageRepository.updateMessageByMessageId(message.getMessageText(), messageId);
        }
        return null;
    }

    /**
     * Retrive all messages posted by account associated with the acountId from database.
     * @param accountId the identifier of the requested account.
     * @return All messages with the postedBy id equal to accountId.
     */
    public List<Message> getMessagesByAccountId(Integer accountId) {
        return messageRepository.findMessagesByAccountId(accountId);
    }
}
