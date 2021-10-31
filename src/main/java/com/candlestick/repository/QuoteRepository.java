package com.candlestick.repository;

import com.candlestick.domain.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public interface QuoteRepository extends JpaRepository<Quote, Long> {
    List<Quote> findAllByIsin(String isin);

    void deleteByIsin(String isin);

    List<Quote> findAllByIsinAndDateBetweenOrderByDate(String isin, LocalDateTime startDate, LocalDateTime endDate);
}
