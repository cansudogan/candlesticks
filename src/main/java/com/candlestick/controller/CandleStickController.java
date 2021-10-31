package com.candlestick.controller;

import com.candlestick.model.dto.CandleStickDTO;
import com.candlestick.service.CandleSticksService;
import com.candlestick.validation.InputValidation;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Collection;

@Controller
public class CandleStickController {
    public static final long DEFAULT_TIME_LENGTH = 30L;
    private final CandleSticksService service;
    private final InputValidation validation;

    public CandleStickController(CandleSticksService service, InputValidation validation) {
        this.service = service;
        this.validation = validation;
    }


    @GetMapping(path = "/candlesticks")
    public ResponseEntity<Collection<CandleStickDTO>> getCandlesticks(@RequestParam("isin") @NonNull String isin,
                                                                      @RequestParam("candleStickLength") @Nullable Long candlestickLength,
                                                                      @RequestParam("from") @Nullable String from,
                                                                      @RequestParam("to") @Nullable String to) {
        if (candlestickLength == null) {
            candlestickLength = DEFAULT_TIME_LENGTH;
        }
        LocalDateTime currentTime = LocalDateTime.now();
        if (to == null) {
            to = currentTime.toString();
        }
        if (from == null) {
            from = currentTime.minusMinutes(30).toString();
        }

        validation.validateInput(isin, candlestickLength, from, to);
        return ResponseEntity.ok(service.getCandlesticksForTimeInterval(isin, candlestickLength, from, to));
    }
}
