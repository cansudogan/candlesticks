package com.candlestick.service;

import com.candlestick.domain.Quote;
import com.candlestick.model.event.QuoteEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
public class QuoteService extends EventService {
    @Override
    public void handleEvent(String event) throws JsonProcessingException {
        QuoteEvent quoteEvent = objectMapper.readValue(event, QuoteEvent.class);

        Quote quote = quoteEvent.getData();

        if (instrumentRepository.existsByIsin(quote.getIsin())) {
            quote.setDate(LocalDateTime.now());
            quoteRepository.save(quote);
        }
    }
}
