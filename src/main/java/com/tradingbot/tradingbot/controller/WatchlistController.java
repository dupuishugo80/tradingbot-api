package com.tradingbot.tradingbot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tradingbot.tradingbot.config.ApiException;
import com.tradingbot.tradingbot.model.Stock;
import com.tradingbot.tradingbot.model.StockAlert;
import com.tradingbot.tradingbot.model.User;
import com.tradingbot.tradingbot.model.Watchlist;
import com.tradingbot.tradingbot.model.dto.stock.StockAlertRequest;
import com.tradingbot.tradingbot.model.dto.watchlist.RemoveStockAlertRequest;
import com.tradingbot.tradingbot.model.dto.watchlist.WatchlistRequest;
import com.tradingbot.tradingbot.model.dto.watchlist.WatchlistResponse;
import com.tradingbot.tradingbot.service.StockAlertService;
import com.tradingbot.tradingbot.service.StockService;
import com.tradingbot.tradingbot.service.WatchlistService;

import org.hibernate.Remove;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/watchlist")
public class WatchlistController {

    private WatchlistService watchlistService;
    private StockService stockService;
    private StockAlertService stockAlertService;

    public WatchlistController(WatchlistService watchlistService, StockService stockService, StockAlertService stockAlertService) {
        this.watchlistService = watchlistService;
        this.stockService = stockService;
        this.stockAlertService = stockAlertService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public WatchlistResponse newWatchlist(@AuthenticationPrincipal User user, @RequestBody WatchlistRequest watchlistRequest) {
        Watchlist watchlist = watchlistService.createWatchlist(user, watchlistRequest.getName());
        return new WatchlistResponse(watchlist);
    }

    @PostMapping("/{id}/add")
    @PreAuthorize("hasRole('USER')")
    public WatchlistResponse addToWatchlist(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestBody StockAlertRequest stockAlertRequest) {
        Watchlist watchlist = watchlistService.getWatchlist(id);
        if (watchlist == null || !watchlist.getUser().getId().equals(user.getId())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Watchlist not found or access denied");
        }
        Stock stock = stockService.getStock(stockAlertRequest.getStockId());
        if(watchlist.getStockAlert().stream().anyMatch(alert -> alert.getStock().getId() == stock.getId())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Stock already in watchlist");
        }
        StockAlert stockAlert = new StockAlert();
        stockAlert.setStock(stock);
        stockAlert.setTargetPrice(stockAlertRequest.getTargetPrice());
        stockAlert.setCondition(stockAlertRequest.getCondition());
        Watchlist updatedWatchlist = watchlistService.addStockAlertToWatchlist(watchlist, stockAlert);
        return new WatchlistResponse(updatedWatchlist);
    }

    @PostMapping("/{id}/removeStockAlert")
    public ResponseEntity<Void> removeStockAlert(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestBody RemoveStockAlertRequest removeStockAlertRequest) {
        Watchlist watchlist = watchlistService.getWatchlist(id);
        if (watchlist == null || !watchlist.getUser().getId().equals(user.getId())) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Watchlist not found or access denied");
        }
        StockAlert stockAlert = stockAlertService.getStockAlertById(removeStockAlertRequest.getStockAlertId());
        watchlistService.removeStockAlertFromWatchlist(watchlist, stockAlert);
        return ResponseEntity.noContent().build(); 
    }
    
}