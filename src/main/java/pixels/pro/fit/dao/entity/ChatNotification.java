package pixels.pro.fit.dao.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatNotification {
    private Long id;
    private UserPrincipal sender;
    private UserPrincipal recipient;
}
