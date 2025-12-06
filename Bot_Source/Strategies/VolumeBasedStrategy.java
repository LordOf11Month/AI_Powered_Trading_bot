package Bot_Source.Strategies;

import Interfaces.Strategy;
import Modules.Kline;
import Modules.Signal;
import Modules.Direction;
import java.util.List;

/**
 * Volume-Based Strategy
 * Analyzes trading volume and buy/sell pressure to generate signals
 */
public class VolumeBasedStrategy implements Strategy {

    private final double volumeThreshold;
    private final double pressureThreshold;

    public VolumeBasedStrategy() {
        this.volumeThreshold = 1.5;
        this.pressureThreshold = 0.6;
    }

    public VolumeBasedStrategy(double volumeThreshold, double pressureThreshold) {
        this.volumeThreshold = volumeThreshold;
        this.pressureThreshold = pressureThreshold;
    }

    @Override
    public Signal makeDecision(List<Kline> klines) {
        if (klines.size() < 10) return Signal.neutral();

        // Calculate average volume (excluding latest)
        double avgVolume = klines.subList(0, klines.size() - 1)
                                 .stream()
                                 .mapToDouble(Kline::getBaseAssetVolume)
                                 .average().orElse(0);

        if (avgVolume == 0) return Signal.neutral();

        Kline latest = klines.get(klines.size() - 1);
        double currentVolume = latest.getBaseAssetVolume();
        double buyPressure = latest.getBuyPressure();
        double sellPressure = latest.getSellPressure();

        boolean highVolume = currentVolume > avgVolume * volumeThreshold;

        // Need high volume for strong signals
        if (!highVolume) {
            // Weak signals for moderate pressure without volume confirmation
            if (buyPressure > 0.58) {
                Signal s = new Signal();
                s.direction = Direction.LONG;
                s.weight = buyPressure - 0.5;
                s.confidence = 0.3;
                return s;
            } else if (sellPressure > 0.58) {
                Signal s = new Signal();
                s.direction = Direction.SHORT;
                s.weight = sellPressure - 0.5;
                s.confidence = 0.3;
                return s;
            }
            return Signal.neutral();
        }

        Signal s = new Signal();
        double volumeRatio = currentVolume / avgVolume;

        if (buyPressure > pressureThreshold) {
            s.direction = Direction.LONG;
            s.weight = currentVolume;
            s.confidence = Math.min(1.0, (buyPressure - 0.5) * 2 * (volumeRatio / 2));
        } else if (sellPressure > pressureThreshold) {
            s.direction = Direction.SHORT;
            s.weight = currentVolume;
            s.confidence = Math.min(1.0, (sellPressure - 0.5) * 2 * (volumeRatio / 2));
        } else {
            return Signal.neutral();
        }

        return s;
    }
}
