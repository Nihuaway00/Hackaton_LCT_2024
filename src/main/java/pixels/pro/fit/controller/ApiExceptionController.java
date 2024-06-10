package pixels.pro.fit.controller;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.ServletException;
import org.springframework.http.HttpStatus;
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

    @ExceptionHandler({ExpiredJwtException.class})
    protected ResponseEntity<ApiException> expiredJwtException(ExpiredJwtException ex){
        ApiException apiException = new ApiException("Срок действия токена истек", HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(apiException, apiException.getStatus());
    }
}
