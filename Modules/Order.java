package Modules;

import java.util.HashMap;
import java.util.Map;
public abstract class Order {
    protected final HashMap<String, String> parameters;
    protected Order(String symbol, Side side, OrderType type) {
        this.parameters = new HashMap<>();
        this.parameters.put("symbol", symbol);
        this.parameters.put("side", side.name());
        this.parameters.put("type", type.name());
    }


    public enum Side { BUY, SELL }
    public enum OrderType {
        MARKET, LIMIT,
        LIMIT_MAKER, STOP_LOSS_LIMIT, TAKE_PROFIT_LIMIT //not implemented at the moment
    }

    public Map<String, String> toApiParameters(){return parameters;}

    
    public Order addParam(String name,String value){
        parameters.put(name,value);
        return this;
    }
}
