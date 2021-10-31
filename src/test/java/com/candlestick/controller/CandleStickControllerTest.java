package com.candlestick.controller;

import com.candlestick.domain.Quote;
import com.candlestick.model.dto.CandleStickDTO;
import com.candlestick.repository.QuoteRepository;
import com.candlestick.service.CandleSticksService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CandleStickControllerTest {
    public static final String ENDPOINT_URL = "/candlesticks?isin=%s&length=%d&from=%s&to=%s";
    public static final String ENDPOINT_URL_ISIN = "/candlesticks?isin=%s";
    public static final long DEFAULT_CANDLE_LENGTH = 30L;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Mock
    QuoteRepository quoteRepository;

    @Autowired
    CandleSticksService service;

    String isin;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service.setQuoteRepository(quoteRepository);
    }

    @Test
    void testHappyFlowWithParameters() throws Exception {
        LocalDateTime timeTo = LocalDateTime.now();
        LocalDateTime timeFrom = timeTo.minusMinutes(10000);

        List<Quote> quotes = generateQuotesBetween(isin, timeFrom, timeTo);
        doReturn(quotes).when(quoteRepository).findAllByIsinAndDateBetweenOrderByDate(any(), any(), any());
        service.setQuoteRepository(quoteRepository);

        String url = String.format(ENDPOINT_URL, isin, 60, timeFrom, timeTo);
        String output = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Deque<CandleStickDTO> outputCandlesticks = mapper.readValue(output, new TypeReference<ArrayDeque<CandleStickDTO>>() {
        });

        CandleStickDTO headElement = outputCandlesticks.pollFirst();
        while (!outputCandlesticks.isEmpty()) {
            CandleStickDTO nextElement = outputCandlesticks.pollFirst();
            Assertions.assertThat(headElement.getClosePrice()).isEqualTo(nextElement.getOpenPrice());
            Assertions.assertThat(headElement.getCloseDate()).isEqualTo(nextElement.getOpenDate());
            headElement = nextElement;
        }
    }

    @Test
    void testHappyFlowWithParameters_SomeCandlesWillBeGeneratedBasedOnPrecedentOnes() throws Exception {
        LocalDateTime timeTo = LocalDateTime.now();
        LocalDateTime timeFrom = timeTo.minusMinutes(10000);

        List<Quote> allQuotes = generateQuotesBetween(isin, timeFrom, timeTo);
        List<Quote> quotes = new ArrayList<>();
        quotes.addAll(allQuotes.subList(0, 100));
        quotes.addAll(allQuotes.subList(9500, 10000));

        doReturn(quotes).when(quoteRepository).findAllByIsinAndDateBetweenOrderByDate(any(), any(), any());
        service.setQuoteRepository(quoteRepository);

        String url = String.format(ENDPOINT_URL, isin, 60, timeFrom, timeTo);
        String output = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Deque<CandleStickDTO> outputCandlesticks = mapper.readValue(output, new TypeReference<ArrayDeque<CandleStickDTO>>() {
        });


        CandleStickDTO headElement = outputCandlesticks.pollFirst();
        while (!outputCandlesticks.isEmpty()) {
            CandleStickDTO nextElement = outputCandlesticks.pollFirst();
            Assertions.assertThat(headElement.getClosePrice()).isEqualTo(nextElement.getOpenPrice());
            Assertions.assertThat(headElement.getCloseDate()).isEqualTo(nextElement.getOpenDate());
            headElement = nextElement;
        }
    }

    @Test
    void testHappyFlowWithDefaultParameters_existingData_andOneCandleIsReturned() throws Exception {
        LocalDateTime timeTo = LocalDateTime.now();
        LocalDateTime timeFrom = timeTo.minusMinutes(DEFAULT_CANDLE_LENGTH);
        String url = String.format(ENDPOINT_URL_ISIN, isin);

        List<Quote> quotes = generateQuotesBetween(isin, timeFrom, timeTo);
        doReturn(quotes).when(quoteRepository).findAllByIsinAndDateBetweenOrderByDate(any(), any(), any());
        service.setQuoteRepository(quoteRepository);

        String output = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Deque<CandleStickDTO> outputCandlesticks = mapper.readValue(output, new TypeReference<ArrayDeque<CandleStickDTO>>() {
        });
        Assertions.assertThat(outputCandlesticks.size()).isOne();
    }

    @Test
    void testRequestWithNoISIN_andGetNotFoundError() throws Exception {
        String url = String.format(ENDPOINT_URL_ISIN, "");

        mockMvc.perform(get(url))
                .andExpect(status().isBadRequest());

    }

    private List<Quote> generateQuotesBetween(String isin, LocalDateTime timeFrom, LocalDateTime timeTo) {
        List<Quote> quotes = new ArrayList<>();

        double price = 0.001;

        while (timeFrom.isBefore(timeTo)) {
            Quote quote = new Quote(isin, price, timeFrom);
            quotes.add(quote);
            price++;
            timeFrom = timeFrom.plusMinutes(1);

        }

        Comparator<Quote> timeStampComparator = Comparator.comparing(Quote::getDate);
        quotes.sort(timeStampComparator);
        return quotes;
    }
}
