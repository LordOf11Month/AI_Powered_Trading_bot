package Bot_Source.Strategies;

import Interfaces.Strategy;
import Modules.Kline;
import java.util.List;
import Modules.Signal;
import Modules.Direction;

public class BollingerReversionStrategy implements Strategy {

    @Override
    public Signal makeDecision(List<Kline> klines) {
        if (klines.size() < 20) return Signal.neutral();

        int period = 20;
        List<Double> closes = klines.stream()
                                    .map(Kline::getClosePrice)
                                    .toList();

        double sma = closes.subList(closes.size() - period, closes.size())
                           .stream().mapToDouble(v -> v).average().orElse(0);

        double std = stddev(closes.subList(closes.size() - period, closes.size()), sma);
        double upper = sma + 2 * std;
        double lower = sma - 2 * std;

        double last = closes.get(closes.size() - 1);

        Signal s = new Signal();

        if (last > upper) {
            s.direction = Direction.SHORT;
            s.weight = (last - upper);
            s.confidence = Math.min(1.0, (last - upper) / (2 * std));
        } else if (last < lower) {
            s.direction = Direction.LONG;
            s.weight = (lower - last);
            s.confidence = Math.min(1.0, (lower - last) / (2 * std));
        } else {
            return Signal.neutral();
        }

        return s;
    }

    private double stddev(List<Double> values, double mean) {
        double sum = 0;
        for (double v : values) sum += Math.pow(v - mean, 2);
        return Math.sqrt(sum / values.size());
    }
}
