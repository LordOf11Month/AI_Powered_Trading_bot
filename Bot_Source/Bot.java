package Bot_Source;

import Interfaces.PositionSizer;
import Interfaces.RiskManager;
import Interfaces.ObserverPattern.Observer;
import Interfaces.ObserverPattern.Subject;
import Modules.Kline;
import Interfaces.Strategy;
import Modules.TradeIntent;
import Http_Client.DummyExchanger;
import Modules.Signal;
import java.util.List;
public class Bot implements Observer {
    
    private PositionSizer positionSizer;
    private Strategy strategy;
    private Subject subject;
    private RiskManager riskManager;
    private DummyExchanger exchanger;


    public Bot(PositionSizer positionSizer, Strategy strategy, Subject subject, RiskManager riskManager, DummyExchanger exchanger) {
        this.positionSizer = positionSizer;
        this.strategy = strategy;
        this.subject = subject;
        this.riskManager = riskManager;
        this.exchanger = exchanger;
    }
    @Override
    public void update() {
       
        Signal signal = strategy.makeDecision((List<Kline>)subject.pullData());
       
        TradeIntent tradeIntent = riskManager.filter(positionSizer.sizePosition(signal));

        exchanger.execute(tradeIntent);
    }
    
}
