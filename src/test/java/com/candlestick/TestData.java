package com.candlestick;

public class TestData {
    public static final String QUOTE_MESSAGE = "{\n" +
            "  \"data\": {\n" +
            "    \"price\": 1234.564,\n" +
            "    \"isin\": \"%s\"\n" +
            "  },\n" +
            "  \"type\": \"QUOTE\"\n" +
            "}";

    public static final String INSTRUMENT_ADD_EVENT_MESSAGE = "{\n" +
            "  \"data\": {\n" +
            "    \"description\": \"%s\",\n" +
            "    \"isin\": \"%s\"\n" +
            "  },\n" +
            "  \"type\": \"ADD\"\n" +
            "}";

    public static final String INSTRUMENT_DELETE_EVENT_MESSAGE = "{\n" +
            "  \"data\": {\n" +
            "    \"description\": \"dolce far niente\",\n" +
            "    \"isin\": \"%s\"\n" +
            "  },\n" +
            "  \"type\": \"DELETE\"\n" +
            "}";

}
