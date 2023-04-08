package br.bieniek.com.websocket.controllers.impl;

import br.bieniek.com.websocket.controllers.TicketController;
import br.bieniek.com.websocket.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class TicketControllerImpl implements TicketController {

    private final TicketService ticketService;

    @Override
    public Map<String, String> buildTicket(String authorization) {
        String token = Optional.ofNullable(authorization)
                .map(auth -> auth.replace("Bearer ", ""))
                .orElseThrow(() -> new RuntimeException("Token is required"));

        String ticket = ticketService.buildAndSaveTicket(token);
        return Map.of("ticket", ticket);
    }
}
