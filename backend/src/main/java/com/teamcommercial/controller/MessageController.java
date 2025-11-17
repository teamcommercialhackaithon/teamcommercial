package com.teamcommercial.controller;

import com.teamcommercial.entity.Message;
import com.teamcommercial.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "http://localhost:4200")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Long id) {
        return messageService.getMessageById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{messageType}")
    public ResponseEntity<List<Message>> getMessagesByType(@PathVariable String messageType) {
        List<Message> messages = messageService.getMessagesByType(messageType);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Message>> searchMessages(@RequestParam String text) {
        List<Message> messages = messageService.searchMessagesByText(text);
        return ResponseEntity.ok(messages);
    }

    @PostMapping
    public ResponseEntity<Message> createMessage(@Valid @RequestBody Message message) {
        Message createdMessage = messageService.createMessage(message);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMessage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Message> updateMessage(@PathVariable Long id, 
                                                  @Valid @RequestBody Message message) {
        try {
            Message updatedMessage = messageService.updateMessage(id, message);
            return ResponseEntity.ok(updatedMessage);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        try {
            messageService.deleteMessage(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

