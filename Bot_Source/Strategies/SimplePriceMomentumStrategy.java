package Bot_Source.Strategies;

import Interfaces.Strategy;
import Modules.Kline;
import Modules.Signal;
import Modules.Direction;
import java.util.List;

/**
 * Simple Price Momentum Strategy
 * Analyzes recent price momentum to generate trading signals
 */
public class SimplePriceMomentumStrategy implements Strategy {

    private final int lookbackPeriod;

    public SimplePriceMomentumStrategy() {
        this.lookbackPeriod = 5;
    }

    public SimplePriceMomentumStrategy(int lookbackPeriod) {
        this.lookbackPeriod = lookbackPeriod;
    }

    @Override
    public Signal makeDecision(List<Kline> klines) {
        if (klines.size() < lookbackPeriod + 1) return Signal.neutral();

        int periods = Math.min(lookbackPeriod, klines.size());
        double totalChange = 0.0;
        int positiveChanges = 0;
        int negativeChanges = 0;

        for (int i = klines.size() - periods; i < klines.size() - 1; i++) {
            Kline current = klines.get(i);
            Kline next = klines.get(i + 1);

            double changePercent = (next.getClosePrice() - current.getClosePrice()) 
                                   / current.getClosePrice() * 100.0;
            totalChange += changePercent;

            if (changePercent > 0.1) positiveChanges++;
            else if (changePercent < -0.1) negativeChanges++;
        }

        double avgChange = totalChange / (periods - 1);

        // Too weak momentum
        if (Math.abs(avgChange) < 0.1) return Signal.neutral();

        Signal s = new Signal();
        s.weight = Math.abs(avgChange);
        s.confidence = Math.min(1.0, Math.abs(avgChange) / 3.0);

        // Boost confidence if consistent direction
        if (positiveChanges > negativeChanges * 2 && avgChange > 0) {
            s.confidence = Math.min(1.0, s.confidence + 0.2);
        } else if (negativeChanges > positiveChanges * 2 && avgChange < 0) {
            s.confidence = Math.min(1.0, s.confidence + 0.2);
        }

        s.direction = avgChange > 0 ? Direction.LONG : Direction.SHORT;
        return s;
    }
}
