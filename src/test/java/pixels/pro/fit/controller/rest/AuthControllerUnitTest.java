package pixels.pro.fit.controller.rest;

import jakarta.servlet.Filter;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import pixels.pro.fit.dto.auth.UserRegistrationRequest;
import pixels.pro.fit.service.AccessTokenProvider;
import pixels.pro.fit.service.RefreshTokenProvider;
import pixels.pro.fit.service.rest.UserPrincipalService;
import pixels.pro.fit.testutil.IntegrationSuite;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerUnitTest extends IntegrationSuite {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @MockBean
    private UserPrincipalService userPrincipalService;
    @MockBean
    private AccessTokenProvider accessTokenProvider;
    @MockBean
    private RefreshTokenProvider refreshTokenProvider;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private AuthenticationManager authenticationManager;


    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    void shouldReturn400() throws Exception {
        String content = new ObjectMapper().writeValueAsString(new UserRegistrationRequest("name", "", "", ""));


        MockHttpServletRequestBuilder post = post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .characterEncoding("utf-8");


        mvc.perform(post)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn200() throws Exception {
        String content = new ObjectMapper().writeValueAsString(new UserRegistrationRequest("name", "gerger", "gwgg@gmail.com", "gerger"));

        MockHttpServletRequestBuilder post = post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .characterEncoding("utf-8");

        mvc.perform(post)
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }
}