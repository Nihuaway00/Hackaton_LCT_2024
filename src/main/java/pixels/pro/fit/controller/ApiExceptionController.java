package pixels.pro.fit.controller;

import jakarta.servlet.ServletException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pixels.pro.fit.dto.ApiException;

import java.rmi.ServerException;

@RestControllerAdvice
public class ApiExceptionController {
    @ExceptionHandler({ApiException.class})
    protected ResponseEntity<ApiException> handleException(ApiException ex){
        return new ResponseEntity<>(ex, ex.getStatus());
    }
}
