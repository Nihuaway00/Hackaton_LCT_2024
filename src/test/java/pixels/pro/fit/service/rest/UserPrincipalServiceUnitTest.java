package pixels.pro.fit.service.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pixels.pro.fit.dao.UserPrincipalRepository;
import pixels.pro.fit.dao.entity.UserPrincipal;
import pixels.pro.fit.testutil.IntegrationSuite;
import pixels.pro.fit.testutil.TestDBFacade;

import static org.junit.jupiter.api.Assertions.*;
import static pixels.pro.fit.entity.UserTestBuilder.aUser;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserPrincipalServiceUnitTest extends IntegrationSuite {

    @Autowired
    private TestDBFacade db;

    @Autowired
    private UserPrincipalService userService;
    @Autowired
    private UserPrincipalRepository userRepository;
    @BeforeEach
    void setUp() {
        db.cleanDatabase();
    }

    @Test
    void shouldSaveUser(){
        UserPrincipal newUser = aUser().withUsername("username").build();
        UserPrincipal savedUser = userService.save(newUser);

        assertNotNull(savedUser.getId());
        assertEquals(savedUser.getUsername(), "username");
        assertEquals(1, userRepository.count());
    }

    @Test
    void shouldDeleteUser(){
        UserPrincipal newUser = aUser().withUsername("username").build();
        UserPrincipal savedUser = userService.save(newUser);

        assertNotNull(savedUser.getId());
        assertEquals(savedUser.getUsername(), "username");
        assertEquals(1, userRepository.count());

        userService.deleteById(savedUser.getId());
        assertEquals(0, userRepository.count());
    }

}