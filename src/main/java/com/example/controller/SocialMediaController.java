package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.DuplicateUsernameException;
import com.example.exception.UserNotInDbException;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;

    // @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    /**
     * Persist new account object into database, with validation of no duplicate username in database
     * and validate input fields restrictions.
     * @param account the new account object to be persisted.
     * @return A ResponseEntity with the persisted account object in its body if successful. Otherwise error status.
     */
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody Account account) {
        try {
            Account res = accountService.register(account);
            if (res != null) return ResponseEntity.status(200).body(res);
            else return ResponseEntity.status(400).body("Client error");
        } catch (DuplicateUsernameException d) {
            return ResponseEntity.status(409).body("Conflict");
        }
    }

    /**
     * Verifies an account object's username is in the database, and the password associated with the account
     * matches the password in the database record.
     * @param account the account object to be verified.
     * @return A ResponseEntity with the verified account object in its body if successful. Otherwise error status.
     */
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Account account) {
        Account res = accountService.login(account);
        if (res != null) return ResponseEntity.status(200).body(res);
        else return ResponseEntity.status(401).body("Unauthorized");
    }

    /**
     * Persists a new message object into database, with validation of the user exists in database,
     * and validate input fields restrictions.
     * @param message the new message object to be persisted.
     * @return A ResponseEntity with the persisted message object in its body if successful. Otherwise error status.
     */
    @PostMapping("/messages")
    public ResponseEntity postMessage(@RequestBody Message message) {
        try {
            Message res = messageService.postMessage(message);
            if (res != null) return ResponseEntity.status(200).body(res);
            else return ResponseEntity.status(400).body("Client error");
        } catch (UserNotInDbException e) {
            return ResponseEntity.status(400).body("Client error");
        }
        
    }

    /**
     * Retrive all messages from database.
     * @return A ResponseEntity with all the meassages in its body. Empty body if no message exist.
     */
    @GetMapping("/messages")
    public ResponseEntity getMessages() {
        return ResponseEntity.status(200).body(messageService.getAllMessage());
    }

    /**
     * Retrive the message object identified by the messageId.
     * @param messageId the indentifier of the requested message.
     * @return A ResponseEntity with the retrived message object in its body. Empty body if such message does not exist.
     */
    @GetMapping("/messages/{messageId}")
    public ResponseEntity getMessageByMessageId(@PathVariable Integer messageId) {
        return ResponseEntity.status(200).body(messageService.getMessageByMessageId(messageId));
    }

    /**
     * Delete the message object identified by the messageId.
     * @param messageId the indentifier of the requested message.
     * @return A ResponseEntity with the number of rows changed in database in its body. Empty body if no rows changed.
     */
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity deleteMessage(@PathVariable Integer messageId) {
        return ResponseEntity.status(200).body(messageService.deleteMessage(messageId));
    }

    /**
     * Update the message object identified by the messageId with the contents in the new message object.
     * Verifies the message object exists, and the new message object's text complies with input restrictions.
     * @param messageId the indentifier of the requested message.
     * @param message the message object with values the original message will be changed to
     * @return A ResponseEntity with the number of rows changed in database in its body. Empty body if no rows changed.
     */
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity updateMessage(@PathVariable Integer messageId, @RequestBody Message message) {
        Integer res = messageService.updateMessage(messageId, message);
        if (res != null) return ResponseEntity.status(200).body(res);
        return ResponseEntity.status(400).body("Client error");
    }
    
    /**
     * Retrive all messages posted by account associated with the acountId from database.
     * @param accountId the identifier of the requested account.
     * @return A ResponseEntity with all the meassages in its body. Empty body if no message exist.
     */
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity getMessagesByAccountId(@PathVariable Integer accountId) {
        return ResponseEntity.status(200).body(messageService.getMessagesByAccountId(accountId));
    }
}
