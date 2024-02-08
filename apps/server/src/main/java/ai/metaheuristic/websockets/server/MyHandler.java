package ai.metaheuristic.websockets.server;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * @author Sergio Lissner
 * Date: 2/6/2024
 * Time: 12:11 PM
 */
public class MyHandler extends TextWebSocketHandler {

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        System.out.println("Message " );
    }

}