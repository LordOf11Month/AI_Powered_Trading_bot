package Interfaces;

import Modules.Decision;

public interface Strategy {
        Decision makeDecision(Object data);
    }
