package by.quantumquartet.quanthink.controllers;

import static by.quantumquartet.quanthink.services.AppLogger.*;

import by.quantumquartet.quanthink.models.Message;
import by.quantumquartet.quanthink.rest.requests.messages.PrivateMessageRequest;
import by.quantumquartet.quanthink.rest.requests.messages.PublicMessageRequest;
import by.quantumquartet.quanthink.rest.responses.messages.MessageDto;
import by.quantumquartet.quanthink.rest.responses.users.UserDto;
import by.quantumquartet.quanthink.services.VectorClockService;
import by.quantumquartet.quanthink.services.MessageService;

import by.quantumquartet.quanthink.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class ChatController {
    private final VectorClockService vectorClockService;
    private final MessageService messageService;
    private final UserService userService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public ChatController(VectorClockService vectorClockService,
                          MessageService messageService,
                          UserService userService,
                          SimpMessagingTemplate simpMessagingTemplate) {
        this.vectorClockService = vectorClockService;
        this.messageService = messageService;
        this.userService = userService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/publicMessage")
    public void receivePublicMessage(@Payload PublicMessageRequest publicMessageRequest) {
        try {
            MessageDto messageDto = messageService.writePublicMessage(publicMessageRequest);
            simpMessagingTemplate.convertAndSend("/chatroom/public", messageDto);

            logInfo(ChatController.class, "Received public message from user with id = "
                    + publicMessageRequest.getSenderId() + ": " + publicMessageRequest.getContent());
        } catch (Exception e) {
            logError(ChatController.class, e.getMessage());
        }
    }

    @MessageMapping("/privateMessage")
    public void receivePrivateMessage(@Payload PrivateMessageRequest privateMessageRequest) {
        try {
            MessageDto messageDto = messageService.writePrivateMessage(privateMessageRequest);

            long senderId = privateMessageRequest.getSenderId();
            long receiverId = privateMessageRequest.getReceiverId();

            Optional<UserDto> userData = userService.getUserById(receiverId);
            if (userData.isPresent()) {
                String userId = String.valueOf(receiverId);
                simpMessagingTemplate.convertAndSendToUser(userId, "/private", messageDto);
            } else {
                logError(ChatController.class, "User with id = "
                        + receiverId + " not found");
            }

            logInfo(ChatController.class, "Received private message from user with id = "
                    + senderId + " to user with id = "
                    + receiverId + ": " + privateMessageRequest.getContent());
        } catch (Exception e) {
            logError(ChatController.class, e.getMessage());
        }
    }

    @MessageMapping("/join")
    public void handleJoin(@Payload String email) {
        try {
            MessageDto messageDto = messageService.handleJoin(email);
            simpMessagingTemplate.convertAndSend("/chatroom/public", messageDto);

            List<Message> messages = vectorClockService.getHistory();
            Optional<UserDto> userDto = userService.getUserByEmail(email);
            if (userDto.isPresent()) {
                String userId = String.valueOf(userDto.get().getId());
                simpMessagingTemplate.convertAndSendToUser(userId, "/history", messages);
            }

            logInfo(ChatController.class, "User with email = " + email + " joined the chat");
        } catch (Exception e) {
            logError(ChatController.class, e.getMessage());
        }
    }

    @MessageMapping("/leave")
    public void handleLeave(@Payload String email) {
        try {
            MessageDto messageDto = messageService.handleLeave(email);
            simpMessagingTemplate.convertAndSend("/chatroom/public", messageDto);

            logInfo(ChatController.class, "User with email = " + email + " left the chat");
        } catch (Exception e) {
            logError(ChatController.class, e.getMessage());
        }
    }
}
