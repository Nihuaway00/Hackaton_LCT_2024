package pixels.pro.fit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
public class ApiException extends Exception{
    private HttpStatus status;

    public ApiException(String message, HttpStatus status){
        super(message, null, false, false);
        this.status = status;
    }
}
