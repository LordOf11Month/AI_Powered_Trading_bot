package Modules;


public class MarketOrder extends Order {

    public MarketOrder(String symbol, Order.Side side,Double quantity,boolean isQuoteOrder) {
        super(symbol, side, Order.OrderType.MARKET);
        if (isQuoteOrder) {
            parameters.put("quoteOrderQty", quantity.toString());
        } else {
            parameters.put("quantity", quantity.toString());
        }
    }

}
