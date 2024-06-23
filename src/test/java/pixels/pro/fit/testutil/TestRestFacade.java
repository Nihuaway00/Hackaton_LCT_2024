package pixels.pro.fit.testutil;

import org.springdoc.core.configuration.SpringDocSecurityConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;

import static org.springframework.boot.test.web.client.TestRestTemplate.HttpClientOption.ENABLE_COOKIES;

@TestComponent
public class TestRestFacade {


    private final TestRestTemplate rest;
    @Autowired
    private Environment environment;

    public TestRestFacade(){
        RestTemplateBuilder builder = new RestTemplateBuilder();
        this.rest = new TestRestTemplate(builder,null, null, ENABLE_COOKIES);
    }

    public <T> ResponseEntity<T> exchange(String url, HttpMethod method, Object body, Class<T> responseType){
        return rest.exchange(
                "http://localhost:" + environment.getProperty("local.server.port") + url,
                method,
                new HttpEntity<>(body),
                responseType
        );
    }

}
