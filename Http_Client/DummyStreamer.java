package Http_Client;

import Interfaces.ObserverPattern.Subject;
import Interfaces.ObserverPattern.Observer;

public class DummyStreamer implements Subject {

    @Override
    public void notifyObservers() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'notifyObservers'");
    }

    @Override
    public void addObserver(Observer observer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addObserver'");
    }

    @Override
    public void removeObserver(Observer observer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeObserver'");
    }
    
    @Override
    public Object pullData() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'pullData'");
    }
}
