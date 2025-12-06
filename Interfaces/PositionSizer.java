package Interfaces;

import Modules.TradeIntent;
import Modules.Signal;

public interface PositionSizer {
    TradeIntent sizePosition(Signal signal);

}
