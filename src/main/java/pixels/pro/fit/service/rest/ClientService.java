package pixels.pro.fit.service.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pixels.pro.fit.dao.ClientRepository;

@Service
public class ClientService {
    @Autowired
    private ClientRepository repository;

}
