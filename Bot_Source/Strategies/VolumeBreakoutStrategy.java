package Bot_Source.Strategies;
import Interfaces.Strategy;
import Modules.Kline;
import java.util.List;
import Modules.Signal;
import Modules.Direction;

public class VolumeBreakoutStrategy implements Strategy {

    @Override
    public Signal makeDecision(List<Kline> klines) {
        if (klines.size() < 15) return Signal.neutral();

        double avgVol = klines.subList(0, klines.size() - 1)
                              .stream()
                              .mapToDouble(Kline::getBaseAssetVolume)
                              .average().orElse(0);

        Kline last = klines.get(klines.size() - 1);

        double vol = last.getBaseAssetVolume();
        double range = last.getRange();
        
        if (range == 0) return Signal.neutral();
        
        double close = last.getClosePrice();
        double upperBodyPos = (close - last.getLowPrice()) / range;

        Signal s = new Signal();

        if (vol > avgVol * 1.8 && upperBodyPos > 0.66) {
            s.direction = Direction.LONG;
            s.weight = range;
            s.confidence = Math.min(1.0, vol / (avgVol * 2));
        } else if (vol > avgVol * 1.8 && upperBodyPos < 0.33) {
            s.direction = Direction.SHORT;
            s.weight = range;
            s.confidence = Math.min(1.0, vol / (avgVol * 2));
        } else {
            return Signal.neutral();
        }

        return s;
    }
}
