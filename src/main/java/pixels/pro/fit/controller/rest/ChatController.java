package pixels.pro.fit.controller.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import pixels.pro.fit.dao.entity.ChatMessage;
import pixels.pro.fit.dao.entity.ChatRoom;
import pixels.pro.fit.dao.entity.UserPrincipal;
import pixels.pro.fit.exception.NoChatFoundException;
import pixels.pro.fit.service.rest.ChatMessageService;
import pixels.pro.fit.service.rest.ChatRoomService;
import pixels.pro.fit.service.rest.UserPrincipalService;

import java.util.List;

@Slf4j
@Controller
public class ChatController {
    @Autowired
    private UserPrincipalService userPrincipalService;
    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private ChatMessageService chatMessageService;
    @MessageMapping("/send")
    @SendToUser("/messages/send")
    public ChatMessage sendMessage(ChatMessage message){
        log.info("sent message: {}", message);
        UserPrincipal recipient = userPrincipalService.findById(message.getRecipientId());
        UserPrincipal sender = userPrincipalService.getCurrentUser();
        List<UserPrincipal> members = List.of(sender, recipient);

        ChatRoom chatRoom = chatRoomService.findByMembers(members).orElseGet(() -> chatRoomService.create(members));
        chatMessageService.save(message);
        return message;
    }
}
