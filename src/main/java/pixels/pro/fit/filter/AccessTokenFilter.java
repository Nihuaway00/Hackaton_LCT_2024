package pixels.pro.fit.filter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.GenericFilterBean;
import pixels.pro.fit.dto.ApiException;
import pixels.pro.fit.security.AccessTokenProvider;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@AllArgsConstructor
public class AccessTokenFilter extends GenericFilterBean  {

    private AccessTokenProvider accessTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        Cookie[] cookies = req.getCookies();
        if(cookies == null) throw new ServletException("У вас нет access токена");
        String accessToken = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("accessToken")).toList().getFirst().getValue();
        if(accessToken != null && accessTokenProvider.verifyToken(accessToken)) filterChain.doFilter(request, response);
        else throw new ServletException("Вы не авторизованы");
    }
}
