package pixels.pro.fit.controller;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import pixels.pro.fit.mapper.ErrorWrapper;
import pixels.pro.fit.mapper.ResponseWrapper;

@ControllerAdvice
public class ResponseWrapperController implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // If the body is already a ResponseEntity, extract the actual body
//        if (body instanceof ResponseEntity) {
//            ResponseEntity<?> responseEntity = (ResponseEntity<?>) body;
//            return ResponseEntity.status(responseEntity.getStatusCode())
//                    .headers(responseEntity.getHeaders())
//                    .body(new ResponseWrapper<>(responseEntity.getBody()));
//        }

        return new ResponseWrapper<>(body);
    }
}
