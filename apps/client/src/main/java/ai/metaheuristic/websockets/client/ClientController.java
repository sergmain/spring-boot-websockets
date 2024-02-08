package ai.metaheuristic.websockets.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

/**
 * @author Sergio Lissner
 * Date: 2/6/2024
 * Time: 12:07 PM
 */
@Controller
public class ClientController {

    private final NotificationDispatcher dispatcher;

    @Autowired
    public ClientController(NotificationDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }


    @MessageMapping("/app")
    public void app(StompHeaderAccessor stompHeaderAccessor) {
        dispatcher.add(stompHeaderAccessor.getSessionId());
    }
}
