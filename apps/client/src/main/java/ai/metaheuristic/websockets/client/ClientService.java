package ai.metaheuristic.websockets.client;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Sergio Lissner
 * Date: 2/6/2024
 * Time: 12:07 PM
 */
@Service
@Slf4j
public class ClientService {
    static WebSocketClient webSocketClient = new StandardWebSocketClient();
    static WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
    static StompSession session;
    public static final String url = "ws://127.0.0.1:8080/dispatcher";
    public static final StompSessionHandler sessionHandler = new MyStompSessionHandler();

    public static class MyStompSessionHandler extends StompSessionHandlerAdapter {

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            System.out.println("afterConnected()");
            // ...
        }

        @Override
        public void handleException(StompSession session, @Nullable StompCommand command,
                                    StompHeaders headers, byte[] payload, Throwable exception) {
            System.out.println("handleException()");
        }

        /**
         * This implementation is empty.
         */
        @Override
        public void handleTransportError(StompSession session, Throwable exception) {
            System.out.println("handleTransportError()");
            Thread t = new Thread(() -> {
                try {
                    connectToServer();
                } catch (Throwable th) {
                    log.error("047.270 ProcessorEventBusService.interactWithFunctionRepository()", th);
                }
            });
            t.start();
        }
    }

    @PostConstruct
    public void post() throws ExecutionException, InterruptedException {

        stompClient.setMessageConverter(new StringMessageConverter());
//        stompClient.setTaskScheduler(taskScheduler); // for heartbeats


        connectToServer();
    }

    private static void connectToServer() throws InterruptedException, ExecutionException {
        CompletableFuture<StompSession> future = stompClient.connectAsync(url, sessionHandler);

        session = future.get();

        session.subscribe("/topic/new-task", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {

                System.out.println("payload: " + payload);

                // ...
            }

        });
    }
}
