package ai.metaheuristic.websockets.server;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Sergio Lissner
 * Date: 2/6/2024
 * Time: 12:06 PM
 */
@Service
@Slf4j
public class ServerService {

    private final SimpMessagingTemplate template;

    private final Set<String> listeners = new HashSet<>();

    @Autowired
    public ServerService(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void add(String sessionId) {
        listeners.add(sessionId);
    }

    public void remove(String sessionId) {
        listeners.remove(sessionId);
    }

    @Scheduled(fixedDelay = 2000)
    public void dispatch() {
        for (String listener : listeners) {
            log.info("Sending notification to " + listener);

            SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
            headerAccessor.setSessionId(listener);
            headerAccessor.setLeaveMutable(true);

            int value = (int) Math.round(Math.random() * 100d);
            template.convertAndSendToUser(
                listener,
                "/topic/events",
                new Notification(Integer.toString(value)),
                headerAccessor.getMessageHeaders());
        }
    }

    @EventListener
    public void sessionDisconnectionHandler(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        log.info("Disconnecting " + sessionId + "!");
        remove(sessionId);
    }
}
