package Modules;

import java.util.HashMap;
import java.util.Map;


/**
 * Order class for Binance API
 * Represents a trading order with all necessary parameters
 * Uses enums for type safety
 */
public class Order {
    // Required fields
    private String symbol;              // Trading pair (e.g., "BTCUSDT")
    private OrderSide side;             // BUY or SELL enum
    private OrderType type;             // Order type enum
    private Double quantity;            // Order quantity
    private Long timestamp;             // Order timestamp
    
    // Optional fields
    private TimeInForce timeInForce;    // Time in force enum (GTC, IOC, FOK)
    private Double price;               // Price per unit (required for LIMIT orders)
    private Double stopPrice;           // Stop price (for stop orders)
    private Double icebergQty;          // Used with LIMIT, STOP_LOSS_LIMIT, and TAKE_PROFIT_LIMIT to create an iceberg order
    private String newClientOrderId;    // Unique order ID
    private Long recvWindow;            // Request timeout window in milliseconds
    
    // Order response fields
    private Long orderId;               // Order ID from Binance
    private OrderStatus status;         // Order status enum
    private Double executedQty;         // Executed quantity
    private Double cummulativeQuoteQty; // Cumulative quote quantity
    
/**
 * Order status enum for Binance API
 * Represents the current state of an order
 */
public enum OrderStatus {
    NEW,                // The order has been accepted by the engine
    PARTIALLY_FILLED,   // Part of the order has been filled
    FILLED,             // The order has been completely filled
    CANCELED,           // The order has been canceled by the user
    PENDING_CANCEL,     // Currently unused
    REJECTED,           // The order was not accepted by the engine
    EXPIRED             // The order was canceled according to the order type's rules (e.g., LIMIT FOK orders with no fill)
}

/**
 * Time in force enum for Binance API
 * Specifies how long an order will remain active before it is executed or expires
 */
public enum TimeInForce {
    GTC,  // Good Till Cancel - Order remains active until it is filled or canceled
    IOC,  // Immediate Or Cancel - Order must be filled immediately, any unfilled portion is canceled
    FOK   // Fill or Kill - Order must be filled in its entirety immediately, or it is canceled
}

    /**
     * Order side enum for Binance API
     */
    public enum OrderSide {
        BUY,
        SELL
    }
    /**
     * Order type enum for Binance API
     */
    public enum OrderType {
        LIMIT,
        MARKET,
        STOP_LOSS,
        STOP_LOSS_LIMIT,
        TAKE_PROFIT,
        TAKE_PROFIT_LIMIT,
        LIMIT_MAKER
    }
    /**
     * Default constructor
    */
   public Order() {
    this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * Constructor for market order
     */
    public Order(String symbol, OrderSide side, Double quantity) {
        this();
        this.symbol = symbol;
        this.side = side;
        this.type = OrderType.MARKET;
        this.quantity = quantity;
    }
    
    /**
     * Constructor for limit order
     */
    public Order(String symbol, OrderSide side, OrderType type, Double quantity, Double price) {
        this();
        this.symbol = symbol;
        this.side = side;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
        this.timeInForce = TimeInForce.GTC; // Default time in force
    }
    
    /**
     * Full constructor
     */
    public Order(String symbol, OrderSide side, OrderType type, Double quantity, Double price, 
                 TimeInForce timeInForce, Double stopPrice, String newClientOrderId) {
        this();
        this.symbol = symbol;
        this.side = side;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
        this.timeInForce = timeInForce;
        this.stopPrice = stopPrice;
        this.newClientOrderId = newClientOrderId;
    }
    
    /**
     * Convert order to API parameters map
     * @return Map of parameters for Binance API request
     */
    public Map<String, String> toApiParameters() {
        Map<String, String> params = new HashMap<>();
        
        if (symbol != null) params.put("symbol", symbol);
        if (side != null) params.put("side", side.name());
        if (type != null) params.put("type", type.name());
        if (quantity != null) params.put("quantity", String.valueOf(quantity));
        if (price != null) params.put("price", String.valueOf(price));
        if (timeInForce != null) params.put("timeInForce", timeInForce.name());
        if (stopPrice != null) params.put("stopPrice", String.valueOf(stopPrice));
        if (icebergQty != null) params.put("icebergQty", String.valueOf(icebergQty));
        if (newClientOrderId != null) params.put("newClientOrderId", newClientOrderId);
        if (recvWindow != null) params.put("recvWindow", String.valueOf(recvWindow));
        if (timestamp != null) params.put("timestamp", String.valueOf(timestamp));
        
        return params;
    }
    
    /**
     * Validate order parameters based on Binance API requirements
     * @return true if order is valid, false otherwise
     */
    public boolean isValid() {
        // Basic validation
        if (symbol == null || symbol.isEmpty()) return false;
        if (side == null) return false;
        if (type == null) return false;
        if (quantity == null || quantity <= 0) return false;
        
        // Validate price for LIMIT orders
        if ((type == OrderType.LIMIT || type == OrderType.STOP_LOSS_LIMIT || 
             type == OrderType.TAKE_PROFIT_LIMIT || type == OrderType.LIMIT_MAKER) && 
            (price == null || price <= 0)) {
            return false;
        }
        
        // Validate timeInForce for LIMIT orders
        if ((type == OrderType.LIMIT || type == OrderType.STOP_LOSS_LIMIT || 
             type == OrderType.TAKE_PROFIT_LIMIT) && timeInForce == null) {
            return false;
        }
        
        // Validate stopPrice for stop orders
        if ((type == OrderType.STOP_LOSS || type == OrderType.STOP_LOSS_LIMIT || 
             type == OrderType.TAKE_PROFIT || type == OrderType.TAKE_PROFIT_LIMIT) && 
            (stopPrice == null || stopPrice <= 0)) {
            return false;
        }
        
        return true;
    }
    
    // Getters and Setters
    public String getSymbol() {
        return symbol;
    }
    
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    
    public OrderSide getSide() {
        return side;
    }
    
    public void setSide(OrderSide side) {
        this.side = side;
    }
    
    public OrderType getType() {
        return type;
    }
    
    public void setType(OrderType type) {
        this.type = type;
    }
    
    public Double getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
    
    public TimeInForce getTimeInForce() {
        return timeInForce;
    }
    
    public void setTimeInForce(TimeInForce timeInForce) {
        this.timeInForce = timeInForce;
    }
    
    public Double getPrice() {
        return price;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }
    
    public Double getStopPrice() {
        return stopPrice;
    }
    
    public void setStopPrice(Double stopPrice) {
        this.stopPrice = stopPrice;
    }
    
    public Double getIcebergQty() {
        return icebergQty;
    }
    
    public void setIcebergQty(Double icebergQty) {
        this.icebergQty = icebergQty;
    }
    
    public String getNewClientOrderId() {
        return newClientOrderId;
    }
    
    public void setNewClientOrderId(String newClientOrderId) {
        this.newClientOrderId = newClientOrderId;
    }
    
    public Long getRecvWindow() {
        return recvWindow;
    }
    
    public void setRecvWindow(Long recvWindow) {
        this.recvWindow = recvWindow;
    }
    
    public Long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
    
    public Long getOrderId() {
        return orderId;
    }
    
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    
    public OrderStatus getStatus() {
        return status;
    }
    
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    
    public Double getExecutedQty() {
        return executedQty;
    }
    
    public void setExecutedQty(Double executedQty) {
        this.executedQty = executedQty;
    }
    
    public Double getCummulativeQuoteQty() {
        return cummulativeQuoteQty;
    }
    
    public void setCummulativeQuoteQty(Double cummulativeQuoteQty) {
        this.cummulativeQuoteQty = cummulativeQuoteQty;
    }
    
    // Helper methods
    
    /**
     * Check if this is a buy order
     * @return true if side is BUY
     */
    public boolean isBuyOrder() {
        return side == OrderSide.BUY;
    }
    
    /**
     * Check if this is a sell order
     * @return true if side is SELL
     */
    public boolean isSellOrder() {
        return side == OrderSide.SELL;
    }
    
    /**
     * Check if this is a market order
     * @return true if type is MARKET
     */
    public boolean isMarketOrder() {
        return type == OrderType.MARKET;
    }
    
    /**
     * Check if this is a limit order
     * @return true if type is LIMIT
     */
    public boolean isLimitOrder() {
        return type == OrderType.LIMIT;
    }
    
    /**
     * Calculate total value of the order (price * quantity)
     * @return total value or null if price is not set
     */
    public Double getTotalValue() {
        if (price != null && quantity != null) {
            return price * quantity;
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "Order{" +
                "symbol='" + symbol + '\'' +
                ", side=" + side +
                ", type=" + type +
                ", quantity=" + quantity +
                ", price=" + price +
                ", timeInForce=" + timeInForce +
                ", stopPrice=" + stopPrice +
                ", orderId=" + orderId +
                ", status=" + status +
                ", executedQty=" + executedQty +
                '}';
    }
    
    /*
     * Builder pattern for creating Order objects
     * Implements the generic Builder interface
     */
    public static class Builder implements Interfaces.Builder<Order> {
        private Order order;
        
        public Builder() {
            order = new Order();
        }
        
        public Builder symbol(String symbol) {
            order.symbol = symbol;
            return this;
        }
        
        public Builder side(OrderSide side) {
            order.side = side;
            return this;
        }
        
        public Builder type(OrderType type) {
            order.type = type;
            return this;
        }
        
        public Builder quantity(Double quantity) {
            order.quantity = quantity;
            return this;
        }
        
        public Builder price(Double price) {
            order.price = price;
            return this;
        }
        
        public Builder timeInForce(TimeInForce timeInForce) {
            order.timeInForce = timeInForce;
            return this;
        }
        
        public Builder stopPrice(Double stopPrice) {
            order.stopPrice = stopPrice;
            return this;
        }
        
        public Builder icebergQty(Double icebergQty) {
            order.icebergQty = icebergQty;
            return this;
        }
        
        public Builder newClientOrderId(String newClientOrderId) {
            order.newClientOrderId = newClientOrderId;
            return this;
        }
        
        public Builder recvWindow(Long recvWindow) {
            order.recvWindow = recvWindow;
            return this;
        }
        
        @Override
        public Order build() {
            return order;
        }
        
        @Override
        public boolean isValid() {
            return order.isValid();
        }
        
        @Override
        public Builder reset() {
            order = new Order();
            return this;
        }
    }
}
