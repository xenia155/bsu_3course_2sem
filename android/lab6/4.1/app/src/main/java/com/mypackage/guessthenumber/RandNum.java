package com.mypackage.guessthenumber;

import java.util.Random;

public class RandNum {

    private final Random random;
    private int number;
    private int bottom;
    private int top;
    public RandNum(int _bottom, int _top){
        this.random = new Random();
        setBorders(_bottom, _top);
    }

    public void generateRandNum(){
        while (true) {
            int num = this.getBottom() + random.nextInt(this.getTop());
            if (number != num) {
                this.number = num;
                break;
            }
        }
    }

    public void setBorders(int _bottom, int _top){
        if (_top <= _bottom)
            throw new IllegalArgumentException("Invalid range");
        this.bottom = _bottom;
        this.top = _top;
        this.generateRandNum();
    }

    public int getTop(){
        return this.top;
    }

    public int getNumber(){
        return this.number;
    }
    public int getBottom(){
        return this.bottom;
    }
}
