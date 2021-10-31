package com.candlestick.model.event;

import com.candlestick.domain.Instrument;
import com.candlestick.model.enums.Type;

import java.io.Serializable;

public class InstrumentEvent implements Serializable {
    Instrument data;
    Type type;

    public InstrumentEvent(Instrument data, Type type) {
        this.data = data;
        this.type = type;
    }

    public Instrument getData() {
        return data;
    }

    public void setData(Instrument data) {
        this.data = data;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
