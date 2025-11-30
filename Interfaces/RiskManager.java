package Interfaces;

import Modules.TradeIntent;

public interface RiskManager {
    TradeIntent filter(TradeIntent tradeIntent);

}
