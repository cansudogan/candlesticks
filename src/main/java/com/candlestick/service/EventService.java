package com.candlestick.service;

import com.candlestick.repository.InstrumentRepository;
import com.candlestick.repository.QuoteRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class EventService {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    InstrumentRepository instrumentRepository;
    @Autowired
    QuoteRepository quoteRepository;


    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public InstrumentRepository getInstrumentRepository() {
        return instrumentRepository;
    }

    public void setInstrumentRepository(InstrumentRepository instrumentRepository) {
        this.instrumentRepository = instrumentRepository;
    }

    public QuoteRepository getQuoteRepository() {
        return quoteRepository;
    }

    public void setQuoteRepository(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    public abstract void handleEvent(String event) throws JsonProcessingException;


}
