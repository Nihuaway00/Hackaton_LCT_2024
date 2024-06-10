package pixels.pro.fit.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import pixels.pro.fit.dao.entity.Token;
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
    private TokenService tokenService;
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

        Token token = new Token();
        token.setRefreshToken(refreshToken);
        token.setAccessToken(accessToken);
        token.setUserPrincipal(userPrincipal);
        tokenService.save(token);

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

        UserPrincipal userPrincipal = userPrincipalService.loadUserByUsername(user.getUsername());

        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setUserPrincipal(userPrincipal);
        tokenService.save(token);

        return new ResponseEntity<>(new JwtAuthenticationResponse(accessToken, refreshToken), HttpStatus.CREATED);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody @Valid UserRefreshRequest data) throws SignatureException {
        Token token = tokenService.findByRefreshToken(data.getRefreshToken())
                .orElseThrow(() -> new SignatureException("Токен не валиден. Авторизируйтесь заново"));
        UserPrincipal userPrincipal = token.getUserPrincipal();
        UserDetails userDetails = userPrincipalService.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new SignatureException("Токен не валиден. Авторизируйтесь заново"));

        if(!refreshTokenProvider.isTokenValid(data.getRefreshToken(), userDetails)) throw new SignatureException("Токен не валиден. Авторизируйтесь заново");

        String accessToken = accessTokenProvider.generateToken(userDetails);
        String refreshToken = refreshTokenProvider.generateToken(userDetails);

        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setUserPrincipal(userPrincipal);
        tokenService.save(token);

        return new ResponseEntity<>(new JwtAuthenticationResponse(accessToken, refreshToken), HttpStatus.CREATED);
    }
}
