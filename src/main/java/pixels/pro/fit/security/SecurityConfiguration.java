package pixels.pro.fit.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import pixels.pro.fit.filter.AccessTokenFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    private AccessTokenProvider accessTokenProvide;
    private static final String[] AUTH_WHITELIST = {
            "/auth/**",
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };

    private AccessTokenFilter jwtFilter()
    {
        return new AccessTokenFilter(accessTokenProvide);
    }
    @Bean
    @Order(1)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http
                .securityMatcher(AUTH_WHITELIST)
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(Customizer.withDefaults())
                .build();
    }

    @Bean
    public SecurityFilterChain authFilterChain(HttpSecurity http) throws Exception{
        return http
                .securityMatcher("/users/**")
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtFilter(), BasicAuthenticationFilter.class)
                .sessionManagement(Customizer.withDefaults())
                .build();
    }
}
