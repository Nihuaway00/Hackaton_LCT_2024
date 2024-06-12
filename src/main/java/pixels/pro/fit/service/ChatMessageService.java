package pixels.pro.fit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pixels.pro.fit.dao.ChatMessageRepository;
import pixels.pro.fit.dao.entity.ChatMessage;

@Service
public class ChatMessageService {
    @Autowired
    private ChatMessageRepository repository;

    public ChatMessage save(ChatMessage entity){
        return this.repository.save(entity);
    }
}
