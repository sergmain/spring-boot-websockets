package ai.metaheuristic.websockets.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author Sergio Lissner
 * Date: 2/6/2024
 * Time: 12:07 PM
 */
@Controller
public class ServerController {

    private final NotificationDispatcher dispatcher;

    @Autowired
    public ServerController(NotificationDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }


/*    @MessageMapping("/app")
    public void app(StompHeaderAccessor stompHeaderAccessor) {
        dispatcher.add(stompHeaderAccessor.getSessionId());
    }*/
}
