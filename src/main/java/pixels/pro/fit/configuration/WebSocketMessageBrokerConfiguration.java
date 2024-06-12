package pixels.pro.fit.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.config.annotation.*;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketMessageBrokerConfiguration implements WebSocketMessageBrokerConfigurer {
    private final String[] brokerPrefixes = {"/messages"};
    private final String[] appPrefixes = {"/app"};

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(brokerPrefixes);
        registry.setApplicationDestinationPrefixes(appPrefixes);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket"); //для браузера
        registry.addEndpoint("/sockjs")
                .withSockJS();
    }
}
