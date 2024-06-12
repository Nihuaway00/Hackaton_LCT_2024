package pixels.pro.fit.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import pixels.pro.fit.dao.entity.Message;

@Slf4j
@Controller
public class ChatController {
    @MessageMapping("/send")
    @SendTo("/messages/send")
    public String sendMessage(String message){
        log.info("sent message: {}", message);
        return message;
    }
}
