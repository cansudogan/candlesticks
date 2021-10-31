package com.candlestick.service;

import com.candlestick.domain.Quote;
import com.candlestick.model.dto.CandleStickDTO;
import com.candlestick.repository.QuoteRepository;
import com.candlestick.validation.CandleStickServiceValidation;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Deque;
import java.util.List;

@Service
public class CandleSticksService {

    private QuoteRepository quoteRepository;

    private CandleStickServiceValidation candleStickServiceValidation;

    public CandleSticksService(QuoteRepository quoteRepository, CandleStickServiceValidation candleStickServiceValidation) {
        this.quoteRepository = quoteRepository;
        this.candleStickServiceValidation = candleStickServiceValidation;
    }

    public QuoteRepository getQuoteRepository() {
        return quoteRepository;
    }

    public void setQuoteRepository(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    public CandleStickServiceValidation getCandleStickServiceValidation() {
        return candleStickServiceValidation;
    }

    public void setCandleStickServiceValidation(CandleStickServiceValidation candleStickServiceValidation) {
        this.candleStickServiceValidation = candleStickServiceValidation;
    }

    public Collection<CandleStickDTO> getCandlesticksForTimeInterval(String isin, Long candlestickLength, String from, String to) {
        LocalDateTime startDate = LocalDateTime.parse(from);
        LocalDateTime endDate = LocalDateTime.parse(to);

        List<Quote> orderedQuoteList = quoteRepository.findAllByIsinAndDateBetweenOrderByDate(isin, startDate, endDate);

        Deque<LocalDateTime> candlestickTimePeriod = CandleStickServiceValidation.generateCandlestickTimePeriod(candlestickLength, startDate, endDate);

        return CandleStickServiceValidation.generateCandlestickListFromQuotes(orderedQuoteList, candlestickTimePeriod);

    }
}
