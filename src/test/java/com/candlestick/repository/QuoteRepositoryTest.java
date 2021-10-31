package com.candlestick.repository;

import com.candlestick.domain.Quote;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
public class QuoteRepositoryTest {
    @Autowired
    QuoteRepository repository;

    @MockBean
    WebSocketSession instrumentStream;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void saveOneInstrumentToEmptyDb_ok() {
        Quote quote = new Quote("C1234", 1.2345, LocalDateTime.now());

        List<Quote> quotes = repository.findAll();
        Assertions.assertThat(quotes).isEmpty();

        repository.save(quote);
        quotes = repository.findAll();
        Assertions.assertThat(quotes.size()).isEqualTo(1);
    }

    @Test
    void deleteAllByIsin_ok() {
        String fistIsin = "C12345";
        String secondIsin = "C54321";

        Quote quoteOne = new Quote(fistIsin, 1.111, LocalDateTime.now());
        Quote quoteTwo = new Quote(secondIsin, 1.222, LocalDateTime.now());
        Quote quoteThree = new Quote(secondIsin, 1.333, LocalDateTime.now());

        List<Quote> quotes = repository.findAll();
        Assertions.assertThat(quotes).isEmpty();

        repository.saveAll(List.of(quoteOne, quoteTwo, quoteThree));
        quotes = repository.findAll();
        Assertions.assertThat(quotes.size()).isEqualTo(3);

        repository.deleteByIsin(secondIsin);
        quotes = repository.findAll();
        Assertions.assertThat(quotes.size()).isEqualTo(1);
        Assertions.assertThat(quotes.get(0).getIsin()).isEqualTo(fistIsin);
    }

    @Test
    void findAllByIsin_ok() {
        String firstIsin = "C12345";
        String secondIsin = "C54321";

        Quote quoteOne = new Quote(firstIsin, 1.111, LocalDateTime.now());
        Quote quoteTwo = new Quote(secondIsin, 1.222, LocalDateTime.now());
        Quote quoteThree = new Quote(secondIsin, 1.333, LocalDateTime.now());

        List<Quote> quotes = repository.findAll();
        Assertions.assertThat(quotes).isEmpty();

        repository.saveAll(List.of(quoteOne, quoteTwo, quoteThree));
        quotes = repository.findAll();
        Assertions.assertThat(quotes.size()).isEqualTo(3);

        quotes = repository.findAllByIsin(secondIsin);
        Assertions.assertThat(quotes.size()).isEqualTo(2);
        Assertions.assertThat(quotes.get(0).getIsin()).isEqualTo(secondIsin);
        Assertions.assertThat(quotes.get(1).getIsin()).isEqualTo(secondIsin);
    }

    @Test
    void findAllByIsin_ok_filter() {
        String firstIsin = "C12345";

        Quote quoteOne = new Quote(firstIsin, 1.111, LocalDateTime.now());
        Quote quoteTwo = new Quote(firstIsin, 1.222, LocalDateTime.now());
        Quote quoteThree = new Quote(firstIsin, 1.333, LocalDateTime.now().minusMinutes(700));
        Quote quoteFour = new Quote(firstIsin, 1.444, LocalDateTime.now().minusMinutes(650));
        Quote quoteFive = new Quote(firstIsin, 1.555, LocalDateTime.now().minusMinutes(600));

        List<Quote> quotes = repository.findAll();
        Assertions.assertThat(quotes).isEmpty();

        repository.saveAll(List.of(quoteOne, quoteTwo, quoteThree, quoteFour, quoteFive));
        quotes = repository.findAll();
        Assertions.assertThat(quotes.size()).isEqualTo(5);

        quotes = repository.findAllByIsinAndDateBetweenOrderByDate(
                firstIsin,
                LocalDateTime.now().minusMinutes(625),
                LocalDateTime.now());
        Assertions.assertThat(quotes.size()).isEqualTo(3);
    }
}
