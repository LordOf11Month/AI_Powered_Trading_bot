package Interfaces;

import Modules.TradeIntent;

public interface PositionSizer {
    TradeIntent sizePosition(double signal);

}
