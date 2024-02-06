package ai.metaheuristic.websockets.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author Sergio Lissner
 * Date: 2/6/2024
 * Time: 12:17 PM
 */
@Configuration
@EnableWebSocket
public class Config {

    /**
     * @author Sergio Lissner
     * Date: 2/6/2024
     * Time: 12:12 PM
     */

    public static class WebSocketConfig implements WebSocketConfigurer {

        @Override
        public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
            registry.addHandler(myHandler(), "/myHandler");
        }

        @Bean
        public WebSocketHandler myHandler() {
            return new MyHandler();
        }

    }
}
