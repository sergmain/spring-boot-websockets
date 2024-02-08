package ai.metaheuristic.websockets.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;


/**
 * @author Sergio Lissner
 * Date: 2/6/2024
 * Time: 12:17 PM
 */
@Configuration
public class Config {

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(8192);
        container.setMaxBinaryMessageBufferSize(8192);
        return container;
    }

    @Configuration
    @EnableWebSocketMessageBroker
    public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

        @Override
        public void registerStompEndpoints(StompEndpointRegistry registry) {
            registry.addEndpoint("/dispatcher");
        }

        @Override
        public void configureMessageBroker(MessageBrokerRegistry config) {
            config.setApplicationDestinationPrefixes("/dispatcher");
            config.enableSimpleBroker("/topic", "/queue");
        }

        @Override
        public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
            registry.setMessageSizeLimit(4 * 8192);
            registry.setTimeToFirstMessage(30000);
        }
    }

/*
    @EnableWebSocket
    public static class WebSocketConfig implements WebSocketConfigurer {

        @Override
        public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
            registry.addHandler(myHandler(), "/myHandler");
//                .setHandshakeHandler(handshakeHandler());
        }

        @Bean
        public WebSocketHandler myHandler() {
            return new MyHandler();
        }

    }
*/

/*
    @Bean
    public DefaultHandshakeHandler handshakeHandler() {
        TomcatRequestUpgradeStrategy strategy = new TomcatRequestUpgradeStrategy();
        strategy.addWebSocketConfigurer(configurable -> {
            policy.setInputBufferSize(8192);
            policy.setIdleTimeout(600000);
        });
        return new DefaultHandshakeHandler(strategy);
    }
*/

}
