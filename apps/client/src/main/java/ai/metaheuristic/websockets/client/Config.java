package ai.metaheuristic.websockets.client;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @author Sergio Lissner
 * Date: 2/7/2024
 * Time: 3:00 PM
 */
@Configuration
public class Config {

/*    @Configuration
    @EnableWebSocketMessageBroker
    @EnableScheduling
    public static class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
        @Override
        public void registerStompEndpoints(StompEndpointRegistry registry) {
            registry.addEndpoint("/processor");
//            registry.addEndpoint("/processor").setAllowedOrigins("*").withSockJS();
        }
    }*/
}
