package pixels.pro.fit.controller.rest;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pixels.pro.fit.dao.entity.UserPrincipal;
import pixels.pro.fit.service.rest.UserPrincipalService;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserPrincipalService userPrincipalService;
    @GetMapping("/{id}")
    public UserPrincipal findById(@PathVariable @NonNull Long id){
        return userPrincipalService.findById(id);
    }
}
