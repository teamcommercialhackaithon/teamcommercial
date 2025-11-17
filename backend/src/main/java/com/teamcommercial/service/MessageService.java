package com.teamcommercial.service;

import com.teamcommercial.entity.Message;
import com.teamcommercial.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<Message> getMessageById(Long id) {
        return messageRepository.findById(id);
    }

    public List<Message> getMessagesByType(String messageType) {
        return messageRepository.findByMessageTypeOrderByCreatedAtDesc(messageType);
    }

    public List<Message> searchMessagesByText(String searchText) {
        return messageRepository.findByMessageTextContainingIgnoreCase(searchText);
    }

    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    public Message updateMessage(Long id, Message messageDetails) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + id));
        
        message.setMessageType(messageDetails.getMessageType());
        message.setMessageText(messageDetails.getMessageText());
        
        return messageRepository.save(message);
    }

    public void deleteMessage(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + id));
        messageRepository.delete(message);
    }
}

