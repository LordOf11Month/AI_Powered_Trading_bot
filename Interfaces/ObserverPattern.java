package Interfaces;

public final class ObserverPattern {
    private ObserverPattern() {}

    public interface Subject {
        void addObserver(Observer observer);
        void removeObserver(Observer observer);
        void notifyObservers();
        Object pullData();
    }

    public interface Observer {
        void update();
    }
}
