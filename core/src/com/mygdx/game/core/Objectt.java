package com.mygdx.game.core;

import java.util.ArrayList;
import java.util.List;

class Objectt {
    private int number;
    private List<Float> x = new ArrayList<>();
    private List<Float> y = new ArrayList<>();

    public Objectt(int number, float x, float y) {
        this.number = number;
        this.x.add(x);
        this.y.add(y);
    }

    public void addCoordinates(float x, float y) {
        this.x.add(x);
        this.y.add(y);
    }

    public int getNumber() {
        return number;
    }

    public float getX(int index) {
        if (x.size() > index) return x.get(index);
        else return x.getLast();
    }

    public float getY(int index) {
        if (y.size() > index) return y.get(index);
        else return y.getLast();
    }
}
