package pixels.pro.fit.controller;

import io.swagger.v3.oas.annotations.headers.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pixels.pro.fit.dao.entity.UserProfile;
import pixels.pro.fit.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService service;

    @GetMapping("/")
    public @ResponseBody List<UserProfile> findAll(){
        return this.service.findAll();
    }
}
