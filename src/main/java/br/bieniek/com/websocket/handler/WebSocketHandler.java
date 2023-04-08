package br.bieniek.com.websocket.handler;

import br.bieniek.com.websocket.services.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final TicketService ticketService;
    private final Map<String, WebSocketSession> sessions;

    public WebSocketHandler(TicketService ticketService, Map<String, WebSocketSession> sessions) {
        this.ticketService = ticketService;
        this.sessions = new ConcurrentHashMap<>();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("[afterConnectionEstablished] session id: {}", session.getId());
        Optional<String> ticket = ticketOf(session);
        if(ticket.isEmpty() || ticket.get().isBlank()) {
            log.warn("[afterConnectionEstablished] session: {}, ticket is required", session.getId());
            close(session, CloseStatus.POLICY_VIOLATION);
            return;
        }

        Optional<String> userId = ticketService.getUserIdByTicket(ticket.get());
        if(userId.isEmpty()) {
            log.warn("[afterConnectionEstablished] session: {}, invalid ticket", session.getId());
            close(session, CloseStatus.POLICY_VIOLATION);
            return;
        }

        sessions.put(userId.get(), session);
        log.info("[afterConnectionEstablished] session: {}, user: {} connected", session.getId(), userId.get());
    }

    private void close(WebSocketSession session, CloseStatus status) {
        try {
            session.close(status);
            log.info("[close] session: {}, status: {}", session.getId(), status);
        } catch (Exception e) {
            log.error("[close] session: {}, error: {}", session.getId(), e.getMessage());
            e.printStackTrace();
        }
    }

    private Optional<String> ticketOf(WebSocketSession session) {
        return Optional
                .ofNullable(session.getUri())
                .map(UriComponentsBuilder::fromUri)
                .map(UriComponentsBuilder::build)
                .map(UriComponents::getQueryParams)
                .map(params -> params.get("ticket"))
                .flatMap(it -> it.stream().findFirst())
                .map(String::trim);
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
