package online.thinhtran.psyconnect.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
@EnableWebSocket
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:5500", "http://127.0.0.1:5500")
                .setAllowedOrigins("*");

//        registry.addEndpoint("/ws")
//                .setAllowedOriginPatterns("*")
//                .setAllowedOrigins("http://localhost:5500", "http://127.0.0.1:5500")
//                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/app"); // subscribe
        config.enableSimpleBroker("/chatroom", "/user"); // publish
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) { // set converter for message broker
        messageConverters.add(messageConverter());
        return true;
    }


    @Bean
    public MappingJackson2MessageConverter messageConverter() {
        return new MappingJackson2MessageConverter();
    }
}
