package Modules;

public class TradeIntent {

    String symbol;
    Order.Side side;
    double quantity;
    boolean isQuoteOrder; // true if the order is a quote order, false if it is a base order
    boolean isAggressive; // true if the order is aggressive, false if it is passive

    Double limitPrice;   // optional
    Double stopLoss;     // optional
    Double takeProfit;   // optional
    boolean veto; //raised by risk manager and cancels order.

    public TradeIntent(
            String symbol,
            Order.Side side,
            double quantity,
            boolean isQuoteOrder,
            boolean isAggressive,
            Double limitPrice,
            Double stopLoss,
            Double takeProfit,
            boolean veto
    ) {
        this.symbol = symbol;
        this.side = side;
        this.quantity = quantity;
        this.isQuoteOrder = isQuoteOrder;
        this.isAggressive = isAggressive;
        this.limitPrice = limitPrice;
        this.stopLoss = stopLoss;
        this.takeProfit = takeProfit;
        this.veto=veto;
    }

    // getters...
}
