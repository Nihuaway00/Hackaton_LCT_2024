package pixels.pro.fit.dao.entity;


import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;

import java.util.Date;

public class RequestTrainer {
    @Id
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private UserPrincipal userPrincipal;
    private Date requestDate;
}
