package Bot_Source.Strategies;

import Interfaces.Strategy;
import Modules.Kline;
import Modules.Signal;
import Modules.Direction;
import java.util.List;

/**
 * Mean Reversion Strategy
 * Prices tend to revert to their mean - trade against extreme deviations
 */
public class MeanReversionStrategy implements Strategy {

    private final int lookbackPeriod;
    private final double deviationThreshold;

    public MeanReversionStrategy() {
        this.lookbackPeriod = 20;
        this.deviationThreshold = 1.5;
    }

    public MeanReversionStrategy(int lookbackPeriod, double deviationThreshold) {
        this.lookbackPeriod = lookbackPeriod;
        this.deviationThreshold = deviationThreshold;
    }

    @Override
    public Signal makeDecision(List<Kline> klines) {
        if (klines.size() < lookbackPeriod) return Signal.neutral();

        List<Double> closes = klines.subList(klines.size() - lookbackPeriod, klines.size())
                                    .stream()
                                    .map(Kline::getClosePrice)
                                    .toList();

        double mean = closes.stream().mapToDouble(v -> v).average().orElse(0);
        double stdDev = stddev(closes, mean);

        if (stdDev == 0) return Signal.neutral();

        double currentPrice = klines.get(klines.size() - 1).getClosePrice();
        double zScore = (currentPrice - mean) / stdDev;

        // Not enough deviation
        if (Math.abs(zScore) < deviationThreshold) return Signal.neutral();

        Signal s = new Signal();
        s.weight = Math.abs(currentPrice - mean);
        s.confidence = Math.min(1.0, Math.abs(zScore) / (deviationThreshold * 2));

        // Price above mean = expect reversion down (short)
        // Price below mean = expect reversion up (long)
        if (zScore > 0) {
            s.direction = Direction.SHORT;
        } else {
            s.direction = Direction.LONG;
        }

        return s;
    }

    private double stddev(List<Double> values, double mean) {
        double sum = 0;
        for (double v : values) sum += Math.pow(v - mean, 2);
        return Math.sqrt(sum / values.size());
    }
}
