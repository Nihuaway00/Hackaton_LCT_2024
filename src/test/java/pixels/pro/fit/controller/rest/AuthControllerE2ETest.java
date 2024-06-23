package pixels.pro.fit.controller.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import pixels.pro.fit.dao.entity.UserPrincipal;
import pixels.pro.fit.dto.auth.UserRegistrationRequest;
import pixels.pro.fit.testutil.E2ETest;
import pixels.pro.fit.testutil.IntegrationSuite;
import pixels.pro.fit.testutil.TestDBFacade;
import pixels.pro.fit.testutil.TestRestFacade;

import static org.junit.jupiter.api.Assertions.*;

@E2ETest
class AuthControllerE2ETest extends IntegrationSuite {
    @Autowired
    private TestDBFacade db;
    @Autowired
    private TestRestFacade rest;

    @BeforeEach
    void beforeEach(){
        db.cleanDatabase();
    }

    @Test
    void shouldSucceed() throws JsonProcessingException {
        String content = new ObjectMapper()
                .writeValueAsString(new UserRegistrationRequest("name", "surname", "fsf@gmail.com", "f;kgowejgjwio"));
        ResponseEntity<?> response = rest.exchange("/auth/register", HttpMethod.POST, content, ResponseEntity.class);

        assertTrue(response.getStatusCode().is2xxSuccessful(), "response status: " + response.getStatusCode());
        assertNotNull(response.getBody());
        //assertEquals("name", db.findById(UserPrincipal.class, ));
    }
}