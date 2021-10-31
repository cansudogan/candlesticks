package com.candlestick.repository;

import com.candlestick.domain.Instrument;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

@DataJpaTest
public class InstrumentRepositoryTest {
    @Autowired
    InstrumentRepository repository;

    @MockBean
    WebSocketSession instrumentStream;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void saveOneInstrumentToEmptyDb_ok() {
        Instrument instrument = new Instrument("C12345", "description");

        List<Instrument> instruments = repository.findAll();
        Assertions.assertThat(instruments).isEmpty();

        repository.save(instrument);
        instruments = repository.findAll();
        Assertions.assertThat(instruments.size()).isEqualTo(1);
    }
}
