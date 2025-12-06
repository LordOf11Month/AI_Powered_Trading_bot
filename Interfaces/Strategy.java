package Interfaces;

/*takes kline or klines data and calculates a decimal signal value between [-1,1]. 1 meaning strong buy -1 meaning strong sell. 0 means hold. 
 */
import Modules.Kline;
import java.util.List;
import Modules.Signal;

public interface Strategy {
    Signal makeDecision(List<Kline> klines);
}
