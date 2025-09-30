package com.tradingbot.tradingbot.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.tradingbot.tradingbot.config.ApiException;
import com.tradingbot.tradingbot.model.StockAlert;
import com.tradingbot.tradingbot.model.User;
import com.tradingbot.tradingbot.model.Watchlist;
import com.tradingbot.tradingbot.repository.WatchlistRepository;

@Service
public class WatchlistService {
    private final WatchlistRepository watchlistRepository;

    public WatchlistService(WatchlistRepository watchlistRepository) {
        this.watchlistRepository = watchlistRepository;
    }

    public Watchlist createWatchlist(User user, String name) {
        Watchlist watchlist = new Watchlist();
        watchlist.setUser(user);
        watchlist.setName(name);
        return watchlistRepository.save(watchlist);
    }

    public Watchlist getWatchlist(Long id) {
        return watchlistRepository.findById(id).orElse(null);
    }

    public Watchlist addStockAlertToWatchlist(Watchlist watchlist, StockAlert stockAlert) {
        List<StockAlert> stockAlertList = watchlist.getStockAlert();
        if (!stockAlertList.contains(stockAlert)) {
            stockAlertList.add(stockAlert);
            watchlist.setStockAlert(stockAlertList);
            watchlistRepository.save(watchlist);
        }
        return watchlist;
    }

    public Watchlist removeStockAlertFromWatchlist(Watchlist watchlist, StockAlert stockAlert) {
        List<StockAlert> stockAlertList = watchlist.getStockAlert();
        if (stockAlertList.contains(stockAlert)) {
            stockAlertList.remove(stockAlert);
            watchlist.setStockAlert(stockAlertList);
            watchlistRepository.save(watchlist);
        } else {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Stock alert not found in watchlist");
        }
        return watchlist;
    }
}
