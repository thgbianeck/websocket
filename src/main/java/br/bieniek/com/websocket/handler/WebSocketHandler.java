package br.bieniek.com.websocket.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

@Slf4j
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("[afterConnectionEstablished] session: {}", session);
        log.info("[afterConnectionEstablished] session id: {}", session.getId());

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    log.info("[afterConnectionEstablished] sending message to session: {}", session);
                    session.sendMessage(new TextMessage("Hello from server UUID: " + UUID.randomUUID() + "!"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 2000L, 2000L);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("[handleTextMessage] session: {}, message: {}", session, message);
        log.info("[handleTextMessage] message payload: {}", message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("[afterConnectionClosed] session: {}, status: {}", session, status);
        log.info("[afterConnectionClosed] session id: {}", session.getId());
    }
}
