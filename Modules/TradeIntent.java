package Modules;

public class TradeIntent {

    final String symbol;
    final Order.Side side;
    final double quantity;
    final boolean isQuoteOrder; // true if the order is a quote order, false if it is a base order
    final boolean isAggressive; // true if the order is aggressive, false if it is passive

    final Double limitPrice;   // optional
    final Double stopLoss;     // optional
    final Double takeProfit;   // optional

    public TradeIntent(
            String symbol,
            Order.Side side,
            double quantity,
            boolean isQuoteOrder,
            boolean isAggressive,
            Double limitPrice,
            Double stopLoss,
            Double takeProfit
    ) {
        this.symbol = symbol;
        this.side = side;
        this.quantity = quantity;
        this.isQuoteOrder = isQuoteOrder;
        this.isAggressive = isAggressive;
        this.limitPrice = limitPrice;
        this.stopLoss = stopLoss;
        this.takeProfit = takeProfit;
    }

    // getters...
}
