package com.tradingbot.tradingbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tradingbot.tradingbot.model.StockAlert;

@Repository
public interface StockAlertRepository extends JpaRepository<StockAlert, Long> {
}
