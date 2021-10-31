package com.candlestick.handler;

import com.candlestick.service.InstrumentService;
import org.springframework.stereotype.Component;

@Component
public class InstrumentWebSocketHandler extends WebSocketHandler {
    public InstrumentWebSocketHandler(InstrumentService instrumentService) {
        super(instrumentService);
    }
}
