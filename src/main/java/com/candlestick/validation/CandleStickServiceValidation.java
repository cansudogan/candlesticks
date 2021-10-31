package com.candlestick.validation;

import com.candlestick.domain.Quote;
import com.candlestick.model.dto.CandleStickDTO;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class CandleStickServiceValidation {
    @NotNull
    public static Deque<LocalDateTime> generateCandlestickTimePeriod(Long candleStickLength, LocalDateTime from, LocalDateTime to) {
        Deque<LocalDateTime> candleStickTimePeriod = new LinkedList<>();
        while (to.isEqual(from) || to.isAfter(from)) {
            candleStickTimePeriod.push(to);
            to = to.minusMinutes(candleStickLength);
        }

        return candleStickTimePeriod;
    }

    public static Collection<CandleStickDTO> generateCandlestickListFromQuotes(List<Quote> orderedQuoteList, Deque<LocalDateTime> candleStickTimePeriod) {
        Deque<Quote> quotes = new LinkedList<>(orderedQuoteList);
        Deque<CandleStickDTO> candleSticks = new LinkedList<>();

        LocalDateTime from = candleStickTimePeriod.pop();
        while (!candleStickTimePeriod.isEmpty()) {
            LocalDateTime to = candleStickTimePeriod.pop();

            CandleStickDTO candleStickDTO = null;
            List<Quote> quotesForCandleSticks = getQuotesForNextCandlestick(to, quotes);

            final boolean thereAreNoPrecedentCandlesticks_And_ThereAreNoQuotesForNextIntervalCandlestick
                    = quotesForCandleSticks.isEmpty() && candleSticks.isEmpty();
            final boolean thereArePrecedentCandlesticks_And_ThereAreNoQuotesForTheTimeInterval
                    = quotesForCandleSticks.isEmpty() && !candleSticks.isEmpty();
            boolean thereAreQuotesAvailableToBuildACandlestick = !quotesForCandleSticks.isEmpty();

            if (thereAreNoPrecedentCandlesticks_And_ThereAreNoQuotesForNextIntervalCandlestick) {
                from = to;
                continue;
            }

            if (thereArePrecedentCandlesticks_And_ThereAreNoQuotesForTheTimeInterval) {
                candleStickDTO = createCandleSticksClosedPrice(candleSticks);
                setCandlestickTimeInterval(from, to, candleStickDTO);
            }

            if (thereAreQuotesAvailableToBuildACandlestick) {
                CandleStickDTO precedent = candleSticks.peekLast();
                candleStickDTO = convertQuotesToCandleStick(quotesForCandleSticks, precedent);
                setCandlestickTimeInterval(from, to, candleStickDTO);
            }
            candleSticks.add(candleStickDTO);
            from = to;
        }
        return candleSticks;
    }

    private static CandleStickDTO convertQuotesToCandleStick(List<Quote> quotes, CandleStickDTO precedent) {
        CandleStickDTO candleStickDTO = new CandleStickDTO();

        Double openPrice = getCandlestickOpenPrice(quotes, precedent);
        candleStickDTO.setOpenPrice(openPrice);

        candleStickDTO.setClosePrice(getPriceOfTheLastQuote(quotes));
        setCandleStickHighestAndLowestPrice(quotes, candleStickDTO);

        return candleStickDTO;
    }

    private static void setCandleStickHighestAndLowestPrice(List<Quote> quotes, CandleStickDTO candleStickDTO) {
        Comparator<Quote> priceComparator = Comparator.comparing(Quote::getPrice);
        quotes.sort(priceComparator);

        Quote first = quotes.get(0);
        Quote last = quotes.get(quotes.size() - 1);

        candleStickDTO.setHighestPrice(last.getPrice());
        candleStickDTO.setLowestPrice(first.getPrice());
    }

    private static Double getPriceOfTheLastQuote(List<Quote> quotes) {
        Quote lastQuote = quotes.get(quotes.size() - 1);

        return lastQuote.getPrice();
    }

    private static Double getCandlestickOpenPrice(List<Quote> quotes, CandleStickDTO candleStickDTO) {
        Double openPrice;
        if (candleStickDTO != null)
            openPrice = candleStickDTO.getClosePrice();
        else {
            openPrice = quotes.get(0).getPrice();
        }

        return openPrice;
    }

    private static void setCandlestickTimeInterval(LocalDateTime from, LocalDateTime to, CandleStickDTO candleStickDTO) {
        candleStickDTO.setOpenDate(from);
        candleStickDTO.setCloseDate(to);
    }

    @NotNull
    private static CandleStickDTO createCandleSticksClosedPrice(Deque<CandleStickDTO> candleSticks) {
        CandleStickDTO candleStickDTO = new CandleStickDTO();
        Double price = candleSticks.getLast().getClosePrice();

        candleStickDTO.setOpenPrice(price);
        candleStickDTO.setHighestPrice(price);
        candleStickDTO.setLowestPrice(price);
        candleStickDTO.setClosePrice(price);

        return candleStickDTO;
    }

    private static List<Quote> getQuotesForNextCandlestick(LocalDateTime to, Deque<Quote> quotes) {
        List<Quote> quotesSublist = new LinkedList<>();
        while (quotes.peekFirst() != null) {
            Quote quote = quotes.pollFirst();
            if (quote.getDate().isAfter(to)) {
                quotes.push(quote);
                break;
            } else {
                quotesSublist.add(quote);
            }
        }

        return quotesSublist;
    }
}
