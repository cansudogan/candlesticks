package com.candlestick.configuration;

import com.candlestick.handler.InstrumentWebSocketHandler;
import com.candlestick.handler.QuoteWebSocketHandler;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ExecutionException;

@Configuration
@Profile("test")
public class WebSocketConfiguration {
    @Bean
    @Autowired
    @Profile("test")
    WebSocketSession instrumentStream(InstrumentWebSocketHandler handler) throws ExecutionException, InterruptedException {
        WebSocketSession webSocketSession = Mockito.mock(WebSocketSession.class);
        return webSocketSession;
    }

    @Bean
    @Profile("test")
    @Autowired
    WebSocketSession quoteStream(QuoteWebSocketHandler handler) throws ExecutionException, InterruptedException {
        WebSocketSession webSocketSession = Mockito.mock(WebSocketSession.class);
        return webSocketSession;
    }
}
