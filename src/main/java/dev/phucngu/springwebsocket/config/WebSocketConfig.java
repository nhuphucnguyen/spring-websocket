package dev.phucngu.springwebsocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(simpleWebSocketHandler(), "/ws");
    }

    @Bean
    public SimpleWebSocketHandler simpleWebSocketHandler() {
        return new SimpleWebSocketHandler();
    }

    public static class SimpleWebSocketHandler extends TextWebSocketHandler {
        private final String SUB_PREFIX = "SUB:";
        Map<String, List<WebSocketSession>> topicSubscribers = new ConcurrentHashMap<>();

        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
            var textMsg = message.getPayload();
            if (textMsg.startsWith(SUB_PREFIX)) {
                var topic = textMsg.substring(SUB_PREFIX.length());
                topicSubscribers.computeIfAbsent(topic, k -> new CopyOnWriteArrayList<>()).add(session);
                System.out.println("Session " + session.getId() + " subscribed to " + textMsg);
                session.sendMessage(new TextMessage("Subscribed to " + topic));
            } else {
                String[] parts = textMsg.split("\\|\\|", 2);
                if (parts.length != 2) {
                    session.sendMessage(new TextMessage("Invalid message format"));
                    return;
                }

                broadcast(session, parts[0], parts[1]);
            }
        }

        public void broadcast(WebSocketSession fromSession, String topic, String message) {
            var subscribers = topicSubscribers.get(topic);
            if (subscribers == null) {
                return;
            }
            subscribers.stream().filter(s -> !s.equals(fromSession)).forEach(s -> {
                try {
                    s.sendMessage(new TextMessage(message));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
