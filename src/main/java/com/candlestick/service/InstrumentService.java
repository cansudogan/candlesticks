package com.candlestick.service;

import com.candlestick.domain.Instrument;
import com.candlestick.model.enums.Type;
import com.candlestick.model.event.InstrumentEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class InstrumentService extends EventService {
    @Override
    public void handleEvent(String event) throws JsonProcessingException {
        InstrumentEvent instrumentEvent = objectMapper.readValue(event, InstrumentEvent.class);

        Type type = instrumentEvent.getType();
        Instrument instrument = instrumentEvent.getData();

        final String isin = instrument.getIsin();


        if (type == Type.ADD) {
            System.out.println(instrument.getIsin());
            instrumentRepository.save(instrument);
        } else if (type == Type.DELETE) {
            instrumentRepository.deleteByIsin(isin);
            quoteRepository.deleteByIsin(isin);
        }
    }

}
