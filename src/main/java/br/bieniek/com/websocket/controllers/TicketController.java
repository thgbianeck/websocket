package br.bieniek.com.websocket.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequestMapping("v1/ticket")
@CrossOrigin
public interface TicketController {

    @PostMapping
    public Map<String, String> buildTicket(
            @RequestHeader(AUTHORIZATION) String authorization);
}
