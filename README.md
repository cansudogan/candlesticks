## System requirements

* Java 11
* Docker
* docker-compose

### Running partner service

To run a partner service you can either use docker-compose
``` 
docker-compose up -d
```

### Running the app
To run the app you can use the following gradle commands
```
./gradlew build -> compiles project 
./gradlew test -> run tests
./gradlew run -> run on port 8080
```

```
http://localhost:9000/candlesticks?isin={ISIN}
```

#### Request example 1.

```localhost:9000/candlesticks?isin=MF2131747423```

#### Output example 1.
```
   [
    {
        "openDate": "2021-10-25T08:27:28.886884",
        "closeDate": "2021-10-25T08:57:28.886884",
        "openPrice": 753.3721,
        "lowestPrice": 753.3721,
        "highestPrice": 753.3721,
        "closePrice": 753.3721
    }
]
```

#### Request example 2.

```localhost:9000/candlesticks?from=2021-10-20T12%3A55%3A49&to=2021-10-25T12%3A55%3A49&isin=RN7366217777```

#### Output example 2.
```
    [
    {
        "openDate": "2021-10-25T08:25:49",
        "closeDate": "2021-10-25T08:55:49",
        "openPrice": 965.1256,
        "lowestPrice": 955.2512,
        "highestPrice": 980.8792,
        "closePrice": 972.0048
    },
    {
        "openDate": "2021-10-25T08:55:49",
        "closeDate": "2021-10-25T09:25:49",
        "openPrice": 972.0048,
        "lowestPrice": 972.0048,
        "highestPrice": 972.0048,
        "closePrice": 972.0048
    },
    {
        "openDate": "2021-10-25T09:25:49",
        "closeDate": "2021-10-25T09:55:49",
        "openPrice": 972.0048,
        "lowestPrice": 972.0048,
        "highestPrice": 972.0048,
        "closePrice": 972.0048
    },
    {
        "openDate": "2021-10-25T09:55:49",
        "closeDate": "2021-10-25T10:25:49",
        "openPrice": 972.0048,
        "lowestPrice": 972.0048,
        "highestPrice": 972.0048,
        "closePrice": 972.0048
    },
    {
        "openDate": "2021-10-25T10:25:49",
        "closeDate": "2021-10-25T10:55:49",
        "openPrice": 972.0048,
        "lowestPrice": 972.0048,
        "highestPrice": 972.0048,
        "closePrice": 972.0048
    },
    {
        "openDate": "2021-10-25T10:55:49",
        "closeDate": "2021-10-25T11:25:49",
        "openPrice": 972.0048,
        "lowestPrice": 972.0048,
        "highestPrice": 972.0048,
        "closePrice": 972.0048
    },
    {
        "openDate": "2021-10-25T11:25:49",
        "closeDate": "2021-10-25T11:55:49",
        "openPrice": 972.0048,
        "lowestPrice": 972.0048,
        "highestPrice": 972.0048,
        "closePrice": 972.0048
    },
    {
        "openDate": "2021-10-25T11:55:49",
        "closeDate": "2021-10-25T12:25:49",
        "openPrice": 972.0048,
        "lowestPrice": 972.0048,
        "highestPrice": 972.0048,
        "closePrice": 972.0048
    },
    {
        "openDate": "2021-10-25T12:25:49",
        "closeDate": "2021-10-25T12:55:49",
        "openPrice": 972.0048,
        "lowestPrice": 972.0048,
        "highestPrice": 972.0048,
        "closePrice": 972.0048
    }
]
```

#### PartnerService
This coding challenge includes a runnable JAR (the partnerService) that provides the websockets mentioned above.

Once started, it provides two websocket streams (`ws://localhost:8080`) plus a website preview of how the stream look (http://localhost:8080)

* `/instruments` provides the currently available instruments with their ISIN and a Description

    * when connecting to the stream, it gives all currently available Instruments
    * once connected it streams the addition/removal of instruments
    * Our partners assured us, that ISINS are unique, but can in rare cases be reused once no Instrument with that ISIN is available anymore (has been deleted, etc.)

* `/quotes` provides the most current price for an instrument every few seconds per available instrument

If you restart the PartnerService, you will have to clean up any data you might have persisted, since it will generate new ISINs and does not retain state from any previous runs.

##### /instrument Specification
The `/instruments` websocket provides all currently active instruments `onConnect`, as well as a stream of add/delete events of instruments.
When you receive a `DELETE` event, the instrument is no longer available and will not receive any more quotes (beware out of order messages on the /quotes stream)
The instruments are uniquely identified by their isin. Beware, ISINs can be reused _after_ an instrument has been deleted.
In any case, you would see a regular ADD event for this new instrument, even when it reuses an ISIN.

```
{
    // The type of the event. ADD if an instrument is ADDED
    // DELETE if an instrument is deleted
    "type": "DELETE"
    {
        //The Payload
        "data": {
            //The Description of the instrument
            "description": "elementum eos accumsan orci constituto antiopam",
            //The ISIN of this instrument
            "isin": "LS342I184454"
        }
    }
}
```

##### /quotes Specification
The `/quotes` Websocket provides prices for available instruments at an arbitrary rate.
It only streams prices for available instruments (beware out of order messages)
```
{
    // The type of the event.
    // QUOTE if an new price is available for an instrument identified by the ISIN
    "type": "QUOTE"
    {
        //The Payload
        "data": {
            //The price of the instrument with arbitray precision
            "price": 1365.25,
            //The ISIN of this instrument
            "isin": "LS342I184454"
        }
    }
}
```