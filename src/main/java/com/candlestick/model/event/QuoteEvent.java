package com.candlestick.model.event;

import com.candlestick.domain.Quote;
import com.candlestick.model.enums.Type;

public class QuoteEvent {
    Quote data;
    Type type;

    public QuoteEvent(Quote data, Type type) {
        this.data = data;
        this.type = type;
    }

    public Quote getData() {
        return data;
    }

    public void setData(Quote data) {
        this.data = data;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
