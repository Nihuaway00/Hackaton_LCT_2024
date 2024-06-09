package pixels.pro.fit.controller;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pixels.pro.fit.dao.entity.Password;
import pixels.pro.fit.dao.entity.Token;
import pixels.pro.fit.dao.entity.UserProfile;
import pixels.pro.fit.dto.*;
import pixels.pro.fit.security.AccessTokenProvider;
import pixels.pro.fit.security.RefreshTokenProvider;
import pixels.pro.fit.service.PasswordService;
import pixels.pro.fit.service.TokenService;
import pixels.pro.fit.service.UserService;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordService passwordService;
    @Autowired
    private TokenService tokenService;

    @Autowired
    private AccessTokenProvider accessTokenProvider;
    @Autowired
    private RefreshTokenProvider refreshTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<?> registration(@RequestBody @Valid UserRegistrationDto data) throws ApiException{
        Optional<UserProfile> optionalUser = userService.findByEmail(data.getEmail());
        if(optionalUser.isPresent()) throw new ApiException("Почта уже используется", HttpStatus.CONFLICT);

        UserProfile user = new UserProfile();
        user.setEmail(data.getEmail());
        user.setName(data.getName());
        user.setSurname(data.getSurname());
        userService.save(user);

        Password password = new Password();

        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

        password.setUser(user);
        password.setHashedPassword(bcrypt.encode(data.getPassword()));
        passwordService.save(password);
        return ResponseEntity.ok("You have registered");
    }

    @PostMapping("/login")
    public String login(@RequestBody @Valid UserLoginDto data, HttpServletRequest request, HttpServletResponse response) throws ApiException {
        UserProfile user = userService.findByEmail(data.getEmail()).orElseThrow(() -> new ApiException("Пользователя с такой почтой не существует", HttpStatus.BAD_REQUEST));
        Password password = passwordService.findById(user.getId()).orElseThrow(() -> new ApiException("Пользователь не имеет пароля. Если это вылетело то пиздец. Пиши мне", HttpStatus.BAD_REQUEST));

        Optional<Token> optionalToken = tokenService.findByUserId(user.getId());
        if(optionalToken.isPresent()){
            boolean isVerify = refreshTokenProvider.verifyToken(optionalToken.get().getRefreshToken());
            if(isVerify) throw new ApiException("You had already logged in", HttpStatus.CONFLICT);
            tokenService.deleteById(optionalToken.get().getId());
        }

        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

        if(!bcrypt.matches(data.getPassword(), password.getHashedPassword())){
            throw new ApiException("Password is incorrect", HttpStatus.BAD_REQUEST);
        }

        String accessToken = accessTokenProvider.generateToken(user.getId().toString());
        String refreshToken = refreshTokenProvider.generateToken(user.getId().toString());

        Cookie cookie = new Cookie("accessToken", accessToken);
        response.addCookie(cookie);

        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setUser(user);
        tokenService.save(token);

        return refreshToken;
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody @Valid UserRefreshDto data, HttpServletRequest request, HttpServletResponse response) throws ApiException {
        boolean isVerify = refreshTokenProvider.verifyToken(data.getRefreshToken());
        if(!isVerify) throw new ApiException("Токен не валиден", HttpStatus.BAD_REQUEST);

        Optional<Cookie> authCookie = Stream.of(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(cookie -> cookie.getName().equals("accessToken")).findFirst();

        authCookie.ifPresent(cookie -> System.out.println(cookie.getValue()));

        String userId = refreshTokenProvider.getClaims(data.getRefreshToken()).getSubject();
        UserProfile user = userService.findById(Long.valueOf(userId)).orElseThrow(() -> new ApiException("Такого пользователя не существует", HttpStatus.BAD_REQUEST));
        Optional<Token> optionalToken = tokenService.findByRefreshToken(data.getRefreshToken());
        optionalToken.ifPresent(token -> tokenService.deleteById(token.getId()));

        String accessToken = accessTokenProvider.generateToken(userId);
        String refreshToken = refreshTokenProvider.generateToken(userId);

        Cookie cookie = new Cookie("accessToken", accessToken);
        response.addCookie(cookie);

        Token newToken = new Token();
        newToken.setAccessToken(accessToken);
        newToken.setRefreshToken(refreshToken);
        newToken.setUser(user);
        tokenService.save(newToken);

        return ResponseEntity.ok(refreshToken);

    }

    public ResponseEntity<?> logout(@RequestBody @Valid UserLogoutDto data) throws ApiException {
        Token token = tokenService.findByRefreshToken(data.getRefreshToken()).orElseThrow(() -> new ApiException("Вы итак не вошли в аккаунт", HttpStatus.CONFLICT));
        tokenService.deleteById(token.getId());
        return ResponseEntity.ok("You had logged out");
    }
}
