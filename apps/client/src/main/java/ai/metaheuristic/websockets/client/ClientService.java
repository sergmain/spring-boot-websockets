package ai.metaheuristic.websockets.client;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
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
    final Map<String, MyStompSessionHandler> sessionHandlers = new HashMap<>();

    static AtomicBoolean inProcess = new AtomicBoolean();

    public ClientService() {
    }

    public static class MyStompSessionHandler extends StompSessionHandlerAdapter {

        private final String url;
        private final Function<String, Boolean> connectToServerFunc;
        private boolean initialized = false;

        public MyStompSessionHandler(String url, Function<String, Boolean> connectToServerFunc) {
            this.url = url;
            this.connectToServerFunc = connectToServerFunc;
        }

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            System.out.println("afterConnected()");
            // ...
        }

        @Override
        public void handleException(StompSession session, @Nullable StompCommand command,
                                    StompHeaders headers, byte[] payload, Throwable exception) {
            System.out.println("handleException(), " + url);
        }

        /**
         * This implementation is empty.
         */
        @Override
        public void handleTransportError(StompSession session, Throwable exception) {
            System.out.println("handleTransportError(), " + url);
//            Thread t = new Thread(() -> {
//                connectToServerFunc.apply(url);
//            });
//            t.start();
        }
    }

    private boolean connectTo(String url) {
//        while (true) {
//            if (inProcess.get()) {
//                try {
//                    Thread.sleep(2000);
//                }
//                catch (InterruptedException e) {
//                    //
//                }
//                continue;
////                    return;
//            }
//            inProcess.set(true);
            try {
//                        status = reConnectToServerFunc.apply(session);
                boolean status = connectToServer(url);
                if (status) {
                    return true;
                }

            } catch (IllegalStateException th) {
                log.error("047.270 IllegalStateException ", th);
            } catch (Throwable th) {
                // log.error("047.270 ProcessorEventBusService.interactWithFunctionRepository()", th);
            }
            finally {
//                inProcess.set(false);
            }

            try {
                Thread.sleep(2000);
            }
            catch (InterruptedException e) {
                //
            }
//                return;
//        }
        return true;
    }

    @PostConstruct
    public void post() {

        stompClient.setMessageConverter(new StringMessageConverter());
//        stompClient.setTaskScheduler(taskScheduler); // for heartbeats

//        inProcess.set(true);
        try {
            this.sessionHandlers.put(URL1, new MyStompSessionHandler(URL1, this::connectTo));
            this.sessionHandlers.put(URL2, new MyStompSessionHandler(URL2, this::connectTo));
            connectTo(URL1);
            connectTo(URL2);
        }
        finally {
//            inProcess.set(false);
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

    private boolean connectToServer(String url)  {
        MyStompSessionHandler sessionHandler = sessionHandlers.get(url);
        if (sessionHandler==null) {
            log.error("Wrong url: " + url);
            return true;
        }
        System.out.println("start processing CompletableFuture ");
        CompletableFuture<StompSession> future = stompClient.connectAsync(url, sessionHandler);

        try {
            System.out.println("\twaiting for completion");
            StompSession session = future.get();
            if (session!=null) {
                StompHeaders headers = new StompHeaders();
                headers.add("url", url);
                headers.setDestination("/topic/events");
                session.subscribe(headers, new MyStompFrameHandler(url));
                sessionHandler.initialized = true;
                System.out.println("\tinitialization of session was completed");
            }
            return true;
        }
        catch (Throwable e) {
            if (!"IOException: The remote computer refused the network connection".equals(ExceptionUtils.getRootCauseMessage(e))) {
                log.error("Error", e);
            }
        }
        return false;
    }
}
