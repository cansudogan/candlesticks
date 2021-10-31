package com.candlestick.configuration;

import com.candlestick.handler.InstrumentWebSocketHandler;
import com.candlestick.handler.QuoteWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.net.URI;
import java.util.concurrent.ExecutionException;


@Configuration
@ConfigurationProperties(prefix = "websocket")
public class WebSocketConfiguration {
    private final String quotesWebSocketURL = "ws://localhost:8080/quotes";
    private final String instrumentsWebSocketURL = "ws://localhost:8080/instruments";


    @Bean
    @Autowired
    WebSocketSession instrumentsWebSocket(InstrumentWebSocketHandler handler) throws ExecutionException, InterruptedException {
        WebSocketClient webSocketClient = new StandardWebSocketClient();

        return webSocketClient.doHandshake(handler,
                new WebSocketHttpHeaders(), URI.create(instrumentsWebSocketURL)).get();
    }

    @Bean
    @Autowired
    WebSocketSession quotesWebSocket(QuoteWebSocketHandler handler) throws ExecutionException, InterruptedException {
        WebSocketClient webSocketClient = new StandardWebSocketClient();

        return webSocketClient.doHandshake(handler,
                new WebSocketHttpHeaders(), URI.create(quotesWebSocketURL)).get();
    }
}
