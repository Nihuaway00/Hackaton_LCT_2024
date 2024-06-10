package pixels.pro.fit.controller;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.Valid;
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
import pixels.pro.fit.dto.*;
import pixels.pro.fit.service.*;

import java.security.SignatureException;
import java.util.NoSuchElementException;

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
