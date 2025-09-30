package com.tradingbot.tradingbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tradingbot.tradingbot.model.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    
}
