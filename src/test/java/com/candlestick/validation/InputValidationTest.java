package com.candlestick.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

public class InputValidationTest {
    InputValidation validation;

    @BeforeEach
    void setUp() {
        validation = new InputValidation();
    }

    @Test
    void testEmptyISIN() {
        Assertions.assertThrows(ResponseStatusException.class,
                () -> validation.validateInput("", 30L, "2021-10-23T12:25:49", "2021-10-25T12:25:49")
        );
    }

    @Test
    void testFrom_isAfter_To() {
        Assertions.assertThrows(ResponseStatusException.class,
                () -> validation.validateInput("C12345", 30L, "2021-10-27T12:25:49", "2021-10-23T12:25:49")
        );
    }

    @Test
    void testCandleStickLength_isBiggerThanTimeInterval() {
        Assertions.assertThrows(ResponseStatusException.class,
                () -> validation.validateInput("C12345", 600L, "2021-10-23T11:25:49", "2021-10-23T12:25:49")
        );
    }

    @Test
    void testNegativeCandleStickLength() {
        Assertions.assertThrows(ResponseStatusException.class,
                () -> validation.validateInput("C12345", -10L, "2021-10-12T11:25:49", "2021-10-12T12:25:49")
        );
    }

    @Test
    void testUnparsableTimeFrom() {
        Assertions.assertThrows(ResponseStatusException.class,
                () -> validation.validateInput("C12345", 10L, "invalid", "2021-10-12T12:25:49")
        );
    }
}
