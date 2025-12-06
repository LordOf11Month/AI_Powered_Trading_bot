package Modules;

public class Signal {
    public Direction direction;
    public double weight;
    public double confidence;
    public boolean kill;

    public static Signal neutral() {
        Signal s = new Signal();
        s.direction = Direction.FLAT;
        s.weight = 0;
        s.confidence = 0;
        s.kill = false;
        return s;
    }
    
}
