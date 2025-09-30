package com.tradingbot.tradingbot.service;

import org.springframework.stereotype.Service;

import com.tradingbot.tradingbot.model.StockAlert;
import com.tradingbot.tradingbot.repository.StockAlertRepository;

@Service
public class StockAlertService {
    
    private StockAlertRepository stockAlertRepository;

    public StockAlertService(StockAlertRepository stockAlertRepository) {
        this.stockAlertRepository = stockAlertRepository;
    }

    public StockAlert getStockAlertById(Long id) {
        return stockAlertRepository.findById(id).orElse(null);
    }
}
