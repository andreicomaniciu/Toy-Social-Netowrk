package com.example.curs9.ubbcluj.map.domain.MyUtils.MyObserver;

public interface Observable {
    void addObserver(Observer e);
    void removeObserver(Observer e);
    void notifyObservers();
}
