package br.bieniek.com.websocket.config;

import br.bieniek.com.websocket.handler.WebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    // inject the handler
    private final WebSocketHandler webSocketHandler;
    @Override
    // register the handler and map it to the /chat endpoint
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/chat") // endpoint
                .setAllowedOrigins("*"); // allow all origins
    }
}
