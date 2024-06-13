package pixels.pro.fit.controller.rest;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pixels.pro.fit.dao.entity.UserPrincipal;
import pixels.pro.fit.dto.auth.JwtAuthenticationResponse;
import pixels.pro.fit.dto.auth.UserLoginRequest;
import pixels.pro.fit.dto.auth.UserRefreshRequest;
import pixels.pro.fit.dto.auth.UserRegistrationRequest;
import pixels.pro.fit.exception.ApiException;
import pixels.pro.fit.exception.NeedAuthorizeException;
import pixels.pro.fit.service.*;
import pixels.pro.fit.service.rest.UserPrincipalService;

import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserPrincipalService userPrincipalService;
    @Autowired
    private AccessTokenProvider accessTokenProvider;
    @Autowired
    private RefreshTokenProvider refreshTokenProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<JwtAuthenticationResponse> registration(@RequestBody @Valid UserRegistrationRequest data) throws ApiException {
        log.info("/registration -> email: " + data.getEmail());
        UserDetails user = User.builder()
                .username(data.getEmail())
                .password(passwordEncoder.encode(data.getPassword()))
                .build();

        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setUsername(user.getUsername());
        userPrincipal.setPassword(user.getPassword());
        userPrincipalService.save(userPrincipal);

        String refreshToken = refreshTokenProvider.generateToken(user);
        String accessToken = accessTokenProvider.generateToken(user);

        return new ResponseEntity<>(new JwtAuthenticationResponse(accessToken, refreshToken), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody @Valid UserLoginRequest data) throws ApiException {
        log.info("/login -> email: " + data.getEmail());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                data.getEmail(),
                data.getPassword()
        ));

        UserDetails user = userPrincipalService.loadUserByUsername(data.getEmail());
        String accessToken = accessTokenProvider.generateToken(user);
        String refreshToken = refreshTokenProvider.generateToken(user);

        return new ResponseEntity<>(new JwtAuthenticationResponse(accessToken, refreshToken), HttpStatus.CREATED);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody @Valid UserRefreshRequest data) throws NeedAuthorizeException {
        try{
            log.info("/refresh -> email: " + data.getRefreshToken());
            String username = refreshTokenProvider.extractUserName(data.getRefreshToken());

            UserPrincipal userPrincipal = userPrincipalService.findByUsername(username).orElseThrow(() -> new NoSuchElementException("Такого пользователя не существует"));
            if(!refreshTokenProvider.isTokenValid(data.getRefreshToken(), userPrincipal)) throw new NeedAuthorizeException();

            String accessToken = accessTokenProvider.generateToken(userPrincipal);
            String refreshToken = refreshTokenProvider.generateToken(userPrincipal);
            return new ResponseEntity<>(new JwtAuthenticationResponse(accessToken, refreshToken), HttpStatus.CREATED);
        }catch (ExpiredJwtException ex){
            throw new NeedAuthorizeException();
        }

    }
}
