package pixels.pro.fit.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
public class ApiException extends Exception{
    private String error;
    private HttpStatus status;
    public ApiException(String message, HttpStatus status){
        super(message, null, false, false);
        this.error = message;
        this.status = status;
    }
}
