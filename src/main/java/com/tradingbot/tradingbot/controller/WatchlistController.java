package com.tradingbot.tradingbot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tradingbot.tradingbot.model.User;
import com.tradingbot.tradingbot.model.Watchlist;
import com.tradingbot.tradingbot.model.dto.stock.StockAlertRequest;
import com.tradingbot.tradingbot.model.dto.watchlist.RemoveStockAlertRequest;
import com.tradingbot.tradingbot.model.dto.watchlist.WatchlistRequest;
import com.tradingbot.tradingbot.model.dto.watchlist.WatchlistResponse;
import com.tradingbot.tradingbot.service.WatchlistService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/watchlist")
public class WatchlistController {

    private WatchlistService watchlistService;

    public WatchlistController(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

    @PostMapping()
    @PreAuthorize("hasRole('USER')")
    public WatchlistResponse newWatchlist(@AuthenticationPrincipal User user, @RequestBody WatchlistRequest watchlistRequest) {
        Watchlist watchlist = watchlistService.createWatchlist(user, watchlistRequest.getName());
        return new WatchlistResponse(watchlist);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteWatchlist(@AuthenticationPrincipal User user, @PathVariable Long id) {
        watchlistService.deleteWatchlist(user, id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/add")
    @PreAuthorize("hasRole('USER')")
    public WatchlistResponse addToWatchlist(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestBody StockAlertRequest stockAlertRequest) {
        Watchlist updatedWatchlist = watchlistService.addStockAlertToWatchlist(user, id, stockAlertRequest);
        return new WatchlistResponse(updatedWatchlist);
    }

    @PostMapping("/{id}/removeStockAlert")
    public ResponseEntity<Void> removeStockAlert(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestBody RemoveStockAlertRequest removeStockAlertRequest) {
        watchlistService.removeStockAlertFromWatchlist(user, id, removeStockAlertRequest);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/get")
    public Page<WatchlistResponse> getUserWatchlist(@AuthenticationPrincipal User user, Pageable pageable) {
        return watchlistService.getWatchlistByUser(user, pageable);
    }
    
}