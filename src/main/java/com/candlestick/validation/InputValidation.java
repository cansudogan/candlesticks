package com.candlestick.validation;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Component
public class InputValidation {
    public void validateInput(String isin, Long candlestickLen, String from, String to) {
        LocalDateTime tempFrom;
        LocalDateTime tempTo;

        try {
            tempFrom = LocalDateTime.parse(from);
            tempTo = LocalDateTime.parse(to);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Given time range cannot be parsed");
        }

        if (isin.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ISIN cannot be empty");
        }

        if (candlestickLen <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid CandleStick length");
        }

        if (!tempFrom.isBefore(tempTo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Time range is invalid");
        }

        if (tempFrom.plusMinutes(candlestickLen).isAfter(tempTo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Candle length is smaller than the input time range");
        }
    }
}
