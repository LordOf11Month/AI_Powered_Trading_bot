package Bot_Source.Strategies;

import Interfaces.Strategy;
import Modules.Kline;
import java.util.List;
import Modules.Signal;
import Modules.Direction;

public class EmaTrendStrategy implements Strategy {

    @Override
    public Signal makeDecision(List<Kline> klines) {
        if (klines.size() < 22) return Signal.neutral();

        double ema9 = ema(klines, 9);
        double ema21 = ema(klines, 21);

        double diff = ema9 - ema21;

        Signal s = new Signal();

        if (diff > 0) {
            s.direction = Direction.LONG;
            s.weight = Math.abs(diff);
            s.confidence = Math.min(1.0, Math.abs(diff) / ema21);
        } else {
            s.direction = Direction.SHORT;
            s.weight = Math.abs(diff);
            s.confidence = Math.min(1.0, Math.abs(diff) / ema21);
        }

        return s;
    }

    private double ema(List<Kline> klines, int period) {
        double k = 2.0 / (period + 1);
        double ema = klines.get(klines.size() - period).getClosePrice();

        for (int i = klines.size() - period + 1; i < klines.size(); i++) {
            double price = klines.get(i).getClosePrice();
            ema = price * k + ema * (1 - k);
        }
        return ema;
    }
}
