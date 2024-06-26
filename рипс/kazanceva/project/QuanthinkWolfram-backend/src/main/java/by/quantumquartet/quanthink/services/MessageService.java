package by.quantumquartet.quanthink.services;

import by.quantumquartet.quanthink.models.*;
import by.quantumquartet.quanthink.repositories.MessageRepository;
import by.quantumquartet.quanthink.repositories.UserRepository;
import by.quantumquartet.quanthink.rest.requests.messages.PrivateMessageRequest;
import by.quantumquartet.quanthink.rest.requests.messages.PublicMessageRequest;
import by.quantumquartet.quanthink.rest.responses.messages.MessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static by.quantumquartet.quanthink.services.AppLogger.logError;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final VectorClockService vectorClockService;

    @Autowired
    public MessageService(MessageRepository messageRepository,
                          UserRepository userRepository,
                          VectorClockService vectorClockService,
                          SimpMessagingTemplate simpMessagingTemplate) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.vectorClockService = vectorClockService;
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(long id) {
        return messageRepository.findById(id);
    }

    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    public void deleteMessage(long id) {
        messageRepository.deleteById(id);
    }

    public MessageDto writePublicMessage(PublicMessageRequest publicMessageRequest) {
        long senderId = publicMessageRequest.getSenderId();
        Optional<User> senderData = userRepository.findById(senderId);
        if (senderData.isEmpty()) {
            logError(UserService.class, "Sender with id = " + senderId + " not found");
            throw new RuntimeException("Sender with id = " + senderId + " not found");
        }
        User sender = senderData.get();

        Message newMessage = new Message();
        newMessage.setType(EMessage.PUBLIC);
        newMessage.setContent(publicMessageRequest.getContent());
        newMessage.setDate(new Timestamp(System.currentTimeMillis()));
        newMessage.setSender(sender);
        newMessage.setTimestamp(vectorClockService.getLogicalTimestamp(sender.getEmail()));
        vectorClockService.incrementClock(sender.getEmail());

        messageRepository.save(newMessage);
        return convertToDto(newMessage);
    }

    public MessageDto writePrivateMessage(PrivateMessageRequest privateMessageRequest) {
        long senderId = privateMessageRequest.getSenderId();
        Optional<User> senderData = userRepository.findById(senderId);
        if (senderData.isEmpty()) {
            logError(UserService.class, "Sender with id = " + senderId + " not found");
            throw new RuntimeException("Sender with id = " + senderId + " not found");
        }

        long receiverId = privateMessageRequest.getReceiverId();
        Optional<User> receiverData = userRepository.findById(receiverId);
        if (receiverData.isEmpty()) {
            logError(UserService.class, "Receiver with id = " + receiverId + " not found");
            throw new RuntimeException("Receiver with id = " + receiverId + " not found");
        }

        Message newMessage = new Message();
        newMessage.setType(EMessage.PRIVATE);
        newMessage.setContent(privateMessageRequest.getContent());
        newMessage.setDate(new Timestamp(System.currentTimeMillis()));
        newMessage.setSender(senderData.get());
        newMessage.setReceiver(receiverData.get());

        messageRepository.save(newMessage);
        return convertToDto(newMessage);
    }

    public MessageDto handleJoin(String email) {
        if (!vectorClockService.isUserExists(email)) {
            vectorClockService.addUser(email);
        }

        vectorClockService.incrementClock(email);

        Message joinMessage = new Message();
        joinMessage.setType(EMessage.PUBLIC);
        joinMessage.setContent(email + " joined the chat");
        joinMessage.setDate(new Timestamp(System.currentTimeMillis()));
        joinMessage.setSender(userRepository.findByEmail(email).orElseThrow());
        joinMessage.setTimestamp(vectorClockService.getLogicalTimestamp(email));

        vectorClockService.addHistoryEntry(email, joinMessage);
        messageRepository.save(joinMessage);

        return convertToDto(joinMessage);
    }

    public MessageDto handleLeave(String email) {
        vectorClockService.incrementClock(email);

        Message leaveMessage = new Message();
        leaveMessage.setType(EMessage.PUBLIC);
        leaveMessage.setContent(email + " left the chat");
        leaveMessage.setDate(new Timestamp(System.currentTimeMillis()));
        leaveMessage.setSender(userRepository.findByEmail(email).orElseThrow());
        leaveMessage.setTimestamp(vectorClockService.getLogicalTimestamp(email));

        vectorClockService.addHistoryEntry(email, leaveMessage);
        messageRepository.save(leaveMessage);

        vectorClockService.removeUser(email);

        return convertToDto(leaveMessage);
    }

    private MessageDto convertToDto(Message message){
        MessageDto messageDto = new MessageDto();
        messageDto.setId(message.getId());
        messageDto.setType(message.getType());
        messageDto.setContent(message.getContent());
        messageDto.setDate((message.getDate()).toString());
        messageDto.setSenderId(message.getSender().getId());
        messageDto.setReceiverId(message.getReceiver().getId());
        return messageDto;
    }
}
