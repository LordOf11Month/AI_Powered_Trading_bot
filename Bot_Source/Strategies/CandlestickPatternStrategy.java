package Bot_Source.Strategies;

import Interfaces.Strategy;
import Modules.Kline;
import Modules.Signal;
import Modules.Direction;
import java.util.List;

/**
 * Candlestick Pattern Strategy
 * Recognizes candlestick patterns to generate trading signals
 */
public class CandlestickPatternStrategy implements Strategy {

    @Override
    public Signal makeDecision(List<Kline> klines) {
        if (klines.size() < 3) return Signal.neutral();

        Kline latest = klines.get(klines.size() - 1);
        Kline previous = klines.get(klines.size() - 2);

        double range = latest.getRange();
        if (range == 0) return Signal.neutral();

        double bodySize = latest.getBodySize();
        double upperWick = latest.getUpperWickSize();
        double lowerWick = latest.getLowerWickSize();
        double bodyRatio = bodySize / range;

        Signal s = new Signal();

        // Check multi-candle patterns first (higher priority)
        Signal multiPattern = analyzeMultiCandlePatterns(klines);
        if (multiPattern.direction != Direction.FLAT) {
            return multiPattern;
        }

        // Marubozu (long body, minimal wicks)
        if (bodyRatio > 0.85) {
            s.weight = bodySize;
            s.confidence = 0.7;
            s.direction = latest.isBullish() ? Direction.LONG : Direction.SHORT;
            return s;
        }

        // Hammer (long lower wick, small upper wick)
        if (lowerWick > bodySize * 2.5 && upperWick < bodySize * 0.5) {
            s.direction = Direction.LONG;
            s.weight = lowerWick;
            s.confidence = 0.6;
            return s;
        }

        // Shooting star (long upper wick, small lower wick)
        if (upperWick > bodySize * 2.5 && lowerWick < bodySize * 0.5) {
            s.direction = Direction.SHORT;
            s.weight = upperWick;
            s.confidence = 0.6;
            return s;
        }

        // Doji patterns
        if (bodyRatio < 0.15) {
            // Dragonfly doji (bullish)
            if (lowerWick > range * 0.6) {
                s.direction = Direction.LONG;
                s.weight = lowerWick;
                s.confidence = 0.4;
                return s;
            }
            // Gravestone doji (bearish)
            if (upperWick > range * 0.6) {
                s.direction = Direction.SHORT;
                s.weight = upperWick;
                s.confidence = 0.4;
                return s;
            }
            // Standard doji - indecision
            return Signal.neutral();
        }

        // Context continuation/reversal
        if (latest.isBullish() && previous.isBearish() && bodyRatio > 0.5) {
            s.direction = Direction.LONG;
            s.weight = bodySize;
            s.confidence = 0.4;
            return s;
        } else if (latest.isBearish() && previous.isBullish() && bodyRatio > 0.5) {
            s.direction = Direction.SHORT;
            s.weight = bodySize;
            s.confidence = 0.4;
            return s;
        }

        return Signal.neutral();
    }

    private Signal analyzeMultiCandlePatterns(List<Kline> klines) {
        int size = klines.size();
        Kline c1 = klines.get(size - 3);
        Kline c2 = klines.get(size - 2);
        Kline c3 = klines.get(size - 1);

        Signal s = new Signal();

        // Bullish engulfing
        if (c2.isBearish() && c3.isBullish() &&
            c3.getOpenPrice() <= c2.getClosePrice() &&
            c3.getClosePrice() >= c2.getOpenPrice()) {
            s.direction = Direction.LONG;
            s.weight = c3.getBodySize();
            s.confidence = 0.75;
            return s;
        }

        // Bearish engulfing
        if (c2.isBullish() && c3.isBearish() &&
            c3.getOpenPrice() >= c2.getClosePrice() &&
            c3.getClosePrice() <= c2.getOpenPrice()) {
            s.direction = Direction.SHORT;
            s.weight = c3.getBodySize();
            s.confidence = 0.75;
            return s;
        }

        // Three white soldiers
        if (c1.isBullish() && c2.isBullish() && c3.isBullish() &&
            c3.getClosePrice() > c2.getClosePrice() && 
            c2.getClosePrice() > c1.getClosePrice()) {
            s.direction = Direction.LONG;
            s.weight = c3.getClosePrice() - c1.getOpenPrice();
            s.confidence = 0.8;
            return s;
        }

        // Three black crows
        if (c1.isBearish() && c2.isBearish() && c3.isBearish() &&
            c3.getClosePrice() < c2.getClosePrice() && 
            c2.getClosePrice() < c1.getClosePrice()) {
            s.direction = Direction.SHORT;
            s.weight = c1.getOpenPrice() - c3.getClosePrice();
            s.confidence = 0.8;
            return s;
        }

        // Morning star
        if (c1.isBearish() && isSmallBody(c2) && c3.isBullish() &&
            c3.getClosePrice() > (c1.getOpenPrice() + c1.getClosePrice()) / 2) {
            s.direction = Direction.LONG;
            s.weight = c3.getBodySize();
            s.confidence = 0.7;
            return s;
        }

        // Evening star
        if (c1.isBullish() && isSmallBody(c2) && c3.isBearish() &&
            c3.getClosePrice() < (c1.getOpenPrice() + c1.getClosePrice()) / 2) {
            s.direction = Direction.SHORT;
            s.weight = c3.getBodySize();
            s.confidence = 0.7;
            return s;
        }

        return Signal.neutral();
    }

    private boolean isSmallBody(Kline kline) {
        double range = kline.getRange();
        if (range == 0) return true;
        return kline.getBodySize() / range < 0.3;
    }
}
