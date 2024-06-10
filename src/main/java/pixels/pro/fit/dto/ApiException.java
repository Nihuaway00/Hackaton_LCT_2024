package pixels.pro.fit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
public class ApiException extends Exception{
    private String error;
    public ApiException(String message){
        super(message, null, false, false);
        this.error = message;
    }
}
