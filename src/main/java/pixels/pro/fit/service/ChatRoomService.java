package pixels.pro.fit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pixels.pro.fit.dao.ChatRoomRepository;
import pixels.pro.fit.dao.entity.ChatRoom;
import pixels.pro.fit.dao.entity.UserPrincipal;

import java.util.Optional;

@Service
public class ChatRoomService {
    @Autowired
    private ChatRoomRepository repository;

    public Optional<ChatRoom> findBySenderIdAndRecipientId(UserPrincipal sender, UserPrincipal recipient){
        return this.repository.findBySenderIdAndRecipientId(sender, recipient);
    }
}
