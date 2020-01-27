package com.example.project;

import java.util.Observable;

public  class Flux extends Observable {
    private int someVariable = 0;
    static String id ;

    public void setSomeVariable(int someVariable) {
        synchronized (this) {
            this.someVariable = someVariable;
        }
        setChanged();
        notifyObservers();
    }

    public synchronized int getSomeVariable() {
        return someVariable;
    }
}