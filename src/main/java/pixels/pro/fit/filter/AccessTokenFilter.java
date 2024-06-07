package pixels.pro.fit.filter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.GenericFilterBean;
import pixels.pro.fit.dto.ApiException;
import pixels.pro.fit.security.AccessTokenProvider;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class AccessTokenFilter extends GenericFilterBean  {
    @Autowired
    private AccessTokenProvider accessTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String accessToken = req.getHeader(AUTHORIZATION);
        if(accessToken.isEmpty() || !accessTokenProvider.verifyToken(accessToken)) throw new ServletException("Вы не авторизованы");
        filterChain.doFilter(request, response);
    }
}
