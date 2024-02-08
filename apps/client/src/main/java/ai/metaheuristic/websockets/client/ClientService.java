package ai.metaheuristic.websockets.client;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Sergio Lissner
 * Date: 2/6/2024
 * Time: 12:07 PM
 */
@Service
@Slf4j
public class ClientService {
    public static final String URL1 = "ws://127.0.0.1:8080/dispatcher";
    public static final String URL2 = "ws://127.0.0.1:8081/dispatcher";

    static WebSocketClient webSocketClient = new StandardWebSocketClient();
    static WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
    final Map<String, List<StompSession>> sessions = new HashMap<>();
    public final StompSessionHandler sessionHandler;

    static AtomicBoolean inProcess = new AtomicBoolean();

    public ClientService() {
        this.sessionHandler = new MyStompSessionHandler(this::reConnectToServer);
    }

    public static class MyStompSessionHandler extends StompSessionHandlerAdapter {

        private final Function<StompSession, Boolean> reConnectToServerFunc;

        public MyStompSessionHandler(Function<StompSession, Boolean> reConnectToServerFunc) {
            this.reConnectToServerFunc = reConnectToServerFunc;
        }

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
                boolean status = false;
                while (true) {
                    if (inProcess.get()) {
                        try {
                            Thread.sleep(2000);
                        }
                        catch (InterruptedException e) {
                            //
                        }
                        continue;
                    }
                    inProcess.set(true);
                    try {
                        status = reConnectToServerFunc.apply(session);
                        if (status) {
                            return;
                        }

                    } catch (IllegalStateException th) {
                        log.error("047.270 IllegalStateException ", th);
                    } catch (Throwable th) {
                        // log.error("047.270 ProcessorEventBusService.interactWithFunctionRepository()", th);
                    }
                    finally {
                        inProcess.set(false);
                    }

                    try {
                        Thread.sleep(2000);
                    }
                    catch (InterruptedException e) {
                        //
                    }
                }
            });
            t.start();
        }
    }

    @PostConstruct
    public void post() {

        stompClient.setMessageConverter(new StringMessageConverter());
//        stompClient.setTaskScheduler(taskScheduler); // for heartbeats

        inProcess.set(true);
        try {
            connectToServer(URL1);
            connectToServer(URL2);
        }
        finally {
            inProcess.set(false);
        }
    }

    public static class MyStompFrameHandler implements StompFrameHandler  {

        private final String url;

        public MyStompFrameHandler(String url) {
            this.url = url;
        }

        @Override
        @NonNull
        public Type getPayloadType(StompHeaders headers) {
            return String.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, @Nullable Object payload) {

            System.out.println(url + ", payload: " + payload);

            // ...
        }
    }

    private Boolean reConnectToServer(StompSession session)  {
        String url = sessions.entrySet().stream().filter(e->e.getValue().contains(session)).findFirst().map(Map.Entry::getKey).orElse(null);
        if (url!=null) {
            sessions.get(url).remove(session);
            return connectToServer(url);
        }
        else {
            System.out.println("url is null");
        }
        return false;
    }

    private boolean connectToServer(String url)  {
        CompletableFuture<StompSession> future = stompClient.connectAsync(url, sessionHandler);

        try {
            StompSession session = future.get();
            if (session!=null) {
                StompHeaders headers = new StompHeaders();
                headers.add("url", url);
                headers.setDestination("/topic/events");
                session.subscribe(headers, new MyStompFrameHandler(url));
                sessions.computeIfAbsent(url, (o)->new ArrayList<>()).add(session);
            }
            return true;
        }
        catch (Throwable e) {
            log.error("Error", e);
        }
        return false;
    }
}
