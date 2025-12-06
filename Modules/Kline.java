package Modules;

import java.util.Date;

/**
 * Kline (Candlestick) data from Binance WebSocket/API
 * Represents OHLCV (Open, High, Low, Close, Volume) data for a time period
 * 
 * Usage with JSON parsing (example with Gson):
 * <pre>
 * // Parse WebSocket message
 * JsonObject kData = jsonObject.getAsJsonObject("k");
 * 
 * // Create Kline directly
 * Kline kline = new Kline(
 *     kData.get("t").getAsLong(),                    // startTime
 *     kData.get("T").getAsLong(),                    // closeTime
 *     kData.get("s").getAsString(),                  // symbol
 *     kData.get("i").getAsString(),                  // interval
 *     kData.get("f").getAsLong(),                    // firstTradeId
 *     kData.get("L").getAsLong(),                    // lastTradeId
 *     Double.parseDouble(kData.get("o").getAsString()), // openPrice
 *     Double.parseDouble(kData.get("c").getAsString()), // closePrice
 *     Double.parseDouble(kData.get("h").getAsString()), // highPrice
 *     Double.parseDouble(kData.get("l").getAsString()), // lowPrice
 *     Double.parseDouble(kData.get("v").getAsString()), // baseAssetVolume
 *     kData.get("n").getAsInt(),                     // numberOfTrades
 *     kData.get("x").getAsBoolean(),                 // isClosed
 *     Double.parseDouble(kData.get("q").getAsString()), // quoteAssetVolume
 *     Double.parseDouble(kData.get("V").getAsString()), // takerBuyBaseVolume
 *     Double.parseDouble(kData.get("Q").getAsString())  // takerBuyQuoteVolume
 * );
 * 
 * // Use helper methods
 * if (kline.isBullish() && kline.getBuyPressure() > 0.6) {
 *     // Strong buying detected
 * }
 * </pre>
 */
public class Kline {
    private Long startTime;              // t - Kline start time (timestamp in milliseconds)
    //private Long closeTime;              // T - Kline close time (timestamp in milliseconds)
    //private String symbol;               // s - Trading pair symbol (e.g., "BNBBTC")
    //private String interval;             // i - Kline interval (e.g., "1m", "5m", "1h", "1d")
    //private Long firstTradeId;           // f - First trade ID in this kline
    //private Long lastTradeId;            // L - Last trade ID in this kline
    private Double openPrice;            // o - Open price (first trade price in the interval)
    private Double closePrice;           // c - Close price (last trade price in the interval)
    private Double highPrice;            // h - Highest price during the interval
    private Double lowPrice;             // l - Lowest price during the interval
    private Double baseAssetVolume;      // v - Base asset volume (total volume traded)
    private Integer numberOfTrades;      // n - Number of trades during the interval
    //private Boolean isClosed;            // x - Is this kline closed? (true when interval is complete)
    private Double quoteAssetVolume;     // q - Quote asset volume
    private Double takerBuyBaseVolume;   // V - Taker buy base asset volume
    private Double takerBuyQuoteVolume;   // Q - Taker buy quote asset volume
    public Kline(
        Long startTime,
        Double openPrice,
        Double closePrice,
        Double highPrice,
        Double lowPrice,
        Double baseAssetVolume,
        Integer numberOfTrades,
        Double quoteAssetVolume,
        Double takerBuyBaseVolume,
        Double takerBuyQuoteVolume
    ) {
        this.startTime = startTime;
        this.openPrice = openPrice;
        this.closePrice = closePrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.baseAssetVolume = baseAssetVolume;
        this.numberOfTrades = numberOfTrades;
        this.quoteAssetVolume = quoteAssetVolume;
        this.takerBuyBaseVolume = takerBuyBaseVolume;
        this.takerBuyQuoteVolume = takerBuyQuoteVolume;
    }
    /**
     * Get the kline start time as a Date object
     * @return Date representing the start time
     */
    public Date getStartDate() {
        return new Date(startTime);
    }
    
 
    /**
     * Calculate the price change during this kline
     * @return Price change (closePrice - openPrice)
     */
    public Double getPriceChange() {
        return closePrice - openPrice;
    }
    
    /**
     * Calculate the price change percentage
     * @return Percentage change from open to close
     */
    public Double getPriceChangePercent() {
        if (openPrice == 0) return 0.0;
        return ((closePrice - openPrice) / openPrice) * 100.0;
    }
    
    /**
     * Check if this is a bullish (green) candle
     * @return true if close price is higher than open price
     */
    public Boolean isBullish() {
        return closePrice > openPrice;
    }
    
    /**
     * Check if this is a bearish (red) candle
     * @return true if close price is lower than open price
     */
    public Boolean isBearish() {
        return closePrice < openPrice;
    }
    
    /**
     * Check if this is a doji (no change) candle
     * @return true if close price equals open price
     */
    public Boolean isDoji() {
        return closePrice.equals(openPrice);
    }
    
    /**
     * Get the body size (absolute difference between open and close)
     * @return Absolute price difference
     */
    public Double getBodySize() {
        return Math.abs(closePrice - openPrice);
    }
    
    /**
     * Get the upper wick size (distance from high to max of open/close)
     * @return Upper wick size
     */
    public Double getUpperWickSize() {
        return highPrice - Math.max(openPrice, closePrice);
    }
    
    /**
     * Get the lower wick size (distance from min of open/close to low)
     * @return Lower wick size
     */
    public Double getLowerWickSize() {
        return Math.min(openPrice, closePrice) - lowPrice;
    }
    
    /**
     * Get the total range (high - low)
     * @return Total price range
     */
    public Double getRange() {
        return highPrice - lowPrice;
    }
    
    /**
     * Get the typical price (average of high, low, close)
     * Used in technical analysis
     * @return Typical price
     */
    public Double getTypicalPrice() {
        return (highPrice + lowPrice + closePrice) / 3.0;
    }
    
    /**
     * Get the weighted close price (HLCC/4)
     * @return Weighted close price
     */
    public Double getWeightedClose() {
        return (highPrice + lowPrice + closePrice + closePrice) / 4.0;
    }
    
    /**
     * Calculate Volume Weighted Average Price (VWAP approximation)
     * @return Approximate VWAP
     */
    public Double getVWAP() {
        if (baseAssetVolume == 0) return 0.0;
        return quoteAssetVolume / baseAssetVolume;
    }
    
    /**
     * Calculate buy/sell pressure ratio
     * @return Ratio of taker buy volume to total volume (0-1)
     */
    public Double getBuyPressure() {
        if (baseAssetVolume == 0) return 0.0;
        return takerBuyBaseVolume / baseAssetVolume;
    }
    
    /**
     * Calculate sell pressure
     * @return Ratio of taker sell volume to total volume (0-1)
     */
    public Double getSellPressure() {
        return 1.0 - getBuyPressure();
    }

    // Getter methods for OHLC and volume data
    public Double getOpenPrice() {
        return openPrice;
    }

    public Double getClosePrice() {
        return closePrice;
    }

    public Double getHighPrice() {
        return highPrice;
    }

    public Double getLowPrice() {
        return lowPrice;
    }

    public Double getBaseAssetVolume() {
        return baseAssetVolume;
    }

    public Integer getNumberOfTrades() {
        return numberOfTrades;
    }

    public Double getQuoteAssetVolume() {
        return quoteAssetVolume;
    }

    public Double getTakerBuyBaseVolume() {
        return takerBuyBaseVolume;
    }

    public Double getTakerBuyQuoteVolume() {
        return takerBuyQuoteVolume;
    }
}