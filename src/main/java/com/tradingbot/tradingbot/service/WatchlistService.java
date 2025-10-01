package com.tradingbot.tradingbot.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.tradingbot.tradingbot.model.Stock;
import com.tradingbot.tradingbot.model.StockAlert;
import com.tradingbot.tradingbot.model.User;
import com.tradingbot.tradingbot.model.Watchlist;
import com.tradingbot.tradingbot.model.dto.stock.StockAlertRequest;
import com.tradingbot.tradingbot.model.dto.watchlist.RemoveStockAlertRequest;
import com.tradingbot.tradingbot.model.dto.watchlist.WatchlistResponse;
import com.tradingbot.tradingbot.repository.WatchlistRepository;

@Service
public class WatchlistService {
    private final WatchlistRepository watchlistRepository;
    private StockService stockService;
    private StockAlertService stockAlertService;

    public WatchlistService(WatchlistRepository watchlistRepository, StockService stockService, StockAlertService stockAlertService) {
        this.watchlistRepository = watchlistRepository;
        this.stockService = stockService;
        this.stockAlertService = stockAlertService;
    }

    public Watchlist createWatchlist(User user, String name) {
        if(name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Watchlist name cannot be empty");
        }
        Watchlist watchlist = new Watchlist();
        watchlist.setUser(user);
        watchlist.setName(name);
        return watchlistRepository.save(watchlist);
    }

    public void deleteWatchlist(User user, Long id) {
        Watchlist watchlist = getWatchlist(id);
        if (watchlist == null) {
            throw new NoSuchElementException("Watchlist not found");
        }
        if (!watchlist.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Access denied to this watchlist");
        }
        watchlistRepository.delete(watchlist);
    }

    public Watchlist getWatchlist(Long id) {
        return watchlistRepository.findById(id).orElse(null);
    }

    public Watchlist addStockAlertToWatchlist(User user, Long watchlistId, StockAlertRequest stockAlertRequest) {
        Watchlist watchlist = getWatchlist(watchlistId);
        if (watchlist == null) {
            throw new NoSuchElementException("Watchlist not found");
        }
        if (!watchlist.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Access denied to this watchlist");
        }
        Stock stock = stockService.getStock(stockAlertRequest.getStockId());
        if(watchlist.getStockAlert().stream().anyMatch(alert -> alert.getStock().getId() == stock.getId())) {
            throw new IllegalArgumentException("Stock already in watchlist");
        }
        StockAlert stockAlert = new StockAlert();
        stockAlert.setStock(stock);
        stockAlert.setTargetPrice(stockAlertRequest.getTargetPrice());
        stockAlert.setCondition(stockAlertRequest.getCondition());
        List<StockAlert> stockAlertList = watchlist.getStockAlert();
        if (!stockAlertList.contains(stockAlert)) {
            stockAlertList.add(stockAlert);
            watchlist.setStockAlert(stockAlertList);
            watchlistRepository.save(watchlist);
        }
        return watchlist;
    }

    public Watchlist removeStockAlertFromWatchlist(User user, Long watchlistId, RemoveStockAlertRequest removeStockAlertRequest) {
        Watchlist watchlist = getWatchlist(watchlistId);
        if (watchlist == null) {
            throw new NoSuchElementException("Watchlist not found");
        }
        if (!watchlist.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Access denied to this watchlist");
        }
        StockAlert stockAlert = stockAlertService.getStockAlertById(removeStockAlertRequest.getStockAlertId());
        List<StockAlert> stockAlertList = watchlist.getStockAlert();
        if (stockAlertList.contains(stockAlert)) {
            stockAlertList.remove(stockAlert);
            watchlist.setStockAlert(stockAlertList);
            watchlistRepository.save(watchlist);
        } else {
            throw new IllegalArgumentException("Stock alert not found in watchlist");
        }
        return watchlist;
    }

    public Page<WatchlistResponse> getWatchlistByUser(User user, Pageable pageable) {
        Page<Watchlist> watchlistPage = watchlistRepository.findAllByUser(user, pageable);

        if (watchlistPage.isEmpty()) {
            throw new NoSuchElementException("No watchlist found");
        }

        return watchlistPage.map(this::convertToResponse);
    }

    private WatchlistResponse convertToResponse(Watchlist watchlist) {
        return new WatchlistResponse(watchlist);
    }
}
