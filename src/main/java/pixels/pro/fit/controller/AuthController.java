package pixels.pro.fit.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
//    @Autowired
//    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public JwtAuthenticationResponse registration(@RequestBody @Valid UserRegistrationRequest data) throws ApiException {
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
        tokenService.save(token);

        return new JwtAuthenticationResponse(accessToken, refreshToken);
    }

    @PostMapping("/login")
    public JwtAuthenticationResponse login(@RequestBody @Valid UserLoginRequest data) throws ApiException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                data.getEmail(),
                data.getPassword()
        ));

        UserDetails user = userPrincipalService.loadUserByUsername(data.getEmail());
        String accessToken = accessTokenProvider.generateToken(user);
        String refreshToken = refreshTokenProvider.generateToken(user);

        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        tokenService.save(token);

        return new JwtAuthenticationResponse(accessToken, refreshToken);
    }

    @PostMapping("/refresh")
    public JwtAuthenticationResponse refresh(@RequestBody @Valid UserRefreshRequest data) throws ApiException {
        Token token = tokenService.findByRefreshToken(data.getRefreshToken())
                .orElseThrow(() -> new ApiException("Вашего токена нет в базе. Скорее всего, он не валиден или пользователь не существует", HttpStatus.UNAUTHORIZED));
        UserPrincipal userPrincipal = token.getUserPrincipal();
        UserDetails userDetails = userPrincipalService.loadUserByUsername(userPrincipal.getUsername());

        if(!refreshTokenProvider.isTokenValid(data.getRefreshToken(), userDetails)) throw new ApiException("Токен не валиден. Авторизируйтесь заново", HttpStatus.UNAUTHORIZED);

        String accessToken = accessTokenProvider.generateToken(userDetails);
        String refreshToken = refreshTokenProvider.generateToken(userDetails);

        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        tokenService.save(token);

        return new JwtAuthenticationResponse(accessToken, refreshToken);
    }
}
