package com.candlestick.handler;

import com.candlestick.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import javax.validation.constraints.NotNull;

public class WebSocketHandler extends AbstractWebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger("WebSocketHandler.class");
    public static final String MESSAGE_HANDLING_EXCEPTION_MESSAGE =
            "Error occurred when handling message {}. Exception: {}. Cause: {}.";


    final EventService eventService;

    public WebSocketHandler(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public void handleTextMessage(@NotNull WebSocketSession session, TextMessage message) {
        try {
            eventService.handleEvent(message.getPayload());
        } catch (Exception exception) {
            logger.error(MESSAGE_HANDLING_EXCEPTION_MESSAGE, message.getPayload(), exception.getClass().getSimpleName(), exception.getCause());
        }
    }

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) {
        logger.info("established connection - " + session);
    }
}
