package com.tradingbot.tradingbot.model.dto.watchlist;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.tradingbot.tradingbot.model.Watchlist;
import com.tradingbot.tradingbot.model.dto.auth.ProfileResponse;
import com.tradingbot.tradingbot.model.dto.stock.StockAlertResponse;

public class WatchlistResponse {
    private Long id;
    private String name;
    private List<StockAlertResponse> stockAlerts;
    private ProfileResponse user;

    public WatchlistResponse(Watchlist watchlist) {
        this.id = watchlist.getId();
        this.name = watchlist.getName();
        this.stockAlerts = Optional.ofNullable(watchlist.getStockAlert())
                .orElse(Collections.emptyList())
                .stream()
                .map(StockAlertResponse::new)
                .toList();
        this.user = new ProfileResponse(watchlist.getUser());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StockAlertResponse> getStockAlerts() {
        return stockAlerts;
    }

    public void setStockAlerts(List<StockAlertResponse> stockAlerts) {
        this.stockAlerts = stockAlerts;
    }

    public ProfileResponse getUser() {
        return user;
    }

    public void setUser(ProfileResponse user) {
        this.user = user;
    }
}
