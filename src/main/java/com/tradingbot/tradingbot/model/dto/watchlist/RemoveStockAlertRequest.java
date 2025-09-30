package com.tradingbot.tradingbot.model.dto.watchlist;

public class RemoveStockAlertRequest {
    private Long stockAlertId;

    public Long getStockAlertId() {
        return stockAlertId;
    }

    public void setStockAlertId(Long stockAlertId) {
        this.stockAlertId = stockAlertId;
    }
}
