package com.candlestick.handler;

import com.candlestick.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuoteWebSocketHandler extends WebSocketHandler {

    @Autowired
    public QuoteWebSocketHandler(QuoteService quoteService) {
        super(quoteService);
    }
}
