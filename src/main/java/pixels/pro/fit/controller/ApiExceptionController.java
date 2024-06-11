package pixels.pro.fit.controller;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pixels.pro.fit.exception.ApiException;
import pixels.pro.fit.exception.NeedAuthorizeException;

import java.security.SignatureException;

@RestControllerAdvice
public class ApiExceptionController {
    @ExceptionHandler({IllegalAccessError.class, MethodArgumentNotValidException.class})
    protected ResponseEntity<ApiException> badRequest(Exception ex){
        return new ResponseEntity<>(new ApiException(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    protected ResponseEntity<ApiException> notFound(Exception ex){
        return new ResponseEntity<>(new ApiException(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    protected ResponseEntity<ApiException> conflict(Exception ex){
        return new ResponseEntity<>(new ApiException(ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({BadCredentialsException.class,AccessDeniedException.class})
    protected ResponseEntity<ApiException> forbidden(Exception ex){
        return new ResponseEntity<>(new ApiException(ex.getMessage()), HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler({ExpiredJwtException.class,  SignatureException.class})
    protected ResponseEntity<ApiException> unauthorized(Exception ex){
        return new ResponseEntity<>(new ApiException(ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({NeedAuthorizeException.class})
    protected ResponseEntity<ApiException> needAuthorize(Exception ex){
        return new ResponseEntity<>(new ApiException(ex.getMessage()), HttpStatus.I_AM_A_TEAPOT);
    }
}
