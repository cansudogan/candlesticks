package com.candlestick;

import com.candlestick.configuration.WebSocketConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = {CandlestickApplication.class, WebSocketConfiguration.class})
@ActiveProfiles("test")
class CandlestickApplicationTests {

    @Test
    void contextLoads() {
    }

}
