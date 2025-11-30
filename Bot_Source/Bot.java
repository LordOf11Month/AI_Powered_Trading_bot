package Bot_Source;

import Interfaces.ObserverPattern.Observer;
import Interfaces.ObserverPattern.Subject;
import Interfaces.Strategy;

public class Bot implements Observer {
    
    private Strategy strategy;
    private Subject subject;
    private RiskManager riskManager;
    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }
    
    @Override
    public void update() {
        var kindels = subject.pullData();
        var decision = strategy.makeDecision(kindels);
        riskManager.manageRisk(decision);
    }
    
}
