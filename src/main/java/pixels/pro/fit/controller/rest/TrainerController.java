package pixels.pro.fit.controller.rest;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pixels.pro.fit.dto.user.RequestToBecomeTrainerRequest;

@RestController
@RequestMapping("/trainer")
public class TrainerController {
    @PostMapping("/")
    public void requestToBecomeTrainer(@RequestBody @Valid RequestToBecomeTrainerRequest body){

    }
}
