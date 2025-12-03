package Modules;
public class LimitOrder extends Order {

   
    public enum TimeInForce { GTC, IOC, FOK }

    public LimitOrder(String symbol, Order.Side side,Double quantity,Double price,TimeInForce tif) {
        super(symbol,side,Order.OrderType.LIMIT);
        parameters.put("quantity", quantity.toString());
        parameters.put("price", price.toString());
        parameters.put("timeInForce", tif.name());
    }

    
}
