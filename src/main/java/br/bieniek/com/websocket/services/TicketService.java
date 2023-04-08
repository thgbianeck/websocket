package br.bieniek.com.websocket.services;

import java.util.Optional;

public interface TicketService {

    String buildAndSaveTicket(String token);
    Optional<String> getUserIdByTicket(String ticket);
}
