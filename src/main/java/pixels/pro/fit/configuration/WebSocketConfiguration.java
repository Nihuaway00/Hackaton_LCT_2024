package pixels.pro.fit.configuration;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.config.annotation.*;
import pixels.pro.fit.dao.entity.UserPrincipal;
import pixels.pro.fit.service.AccessTokenProvider;
import pixels.pro.fit.service.rest.UserPrincipalService;

import java.security.Principal;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE+99)
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";
    private final String[] brokerPrefixes = {"/messages"};
    private final String[] appPrefixes = {"/app"};

    @Autowired
    private AccessTokenProvider accessTokenProvider;
    @Autowired
    private UserPrincipalService userPrincipalService;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes(appPrefixes);
        registry.enableSimpleBroker(brokerPrefixes);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket"); //для браузера
        registry.addEndpoint("/sockjs")
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration){
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                //сработает при коннекте
                if(StompCommand.CONNECT.equals(accessor.getCommand())){
                    // Получаем токен из заголовка
                    var authHeader = accessor.getHeader(HEADER_NAME);
                    if (authHeader != null && StringUtils.startsWith(String.valueOf(authHeader), BEARER_PREFIX)) {
                        var jwt = String.valueOf(authHeader).substring(BEARER_PREFIX.length());
                        var username = accessTokenProvider.extractUserName(jwt);
                        if (StringUtils.isNotEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
                            UserDetails userDetails = userPrincipalService.loadUserByUsername(username);
                            accessor.setUser((Principal) userDetails);
                        }
                    }
                }

                return message;
            }
        });
    }
}
