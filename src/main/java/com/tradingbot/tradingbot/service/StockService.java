package com.tradingbot.tradingbot.service;

import org.springframework.stereotype.Service;

import com.tradingbot.tradingbot.model.Stock;
import com.tradingbot.tradingbot.repository.StockRepository;

@Service
public class StockService {

    private StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public Stock getStock(Long id) {
        return stockRepository.findById(id).orElse(null);
    }
    
}
