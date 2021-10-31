package com.candlestick.repository;

import com.candlestick.domain.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface InstrumentRepository extends JpaRepository<Instrument, Long> {
    List<Instrument> findAll();

    void deleteByIsin(String isin);

    boolean existsByIsin(String isin);
}
