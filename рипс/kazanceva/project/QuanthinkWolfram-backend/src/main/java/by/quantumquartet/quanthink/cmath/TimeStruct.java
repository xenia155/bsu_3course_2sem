package by.quantumquartet.quanthink.cmath;

import by.quantumquartet.quanthink.cmath.MatrixC;

public class TimeStruct {

    // Время в миллисекундах
    private long time;
    private MatrixC resultMatrix;
    private String resultStr;

    private double resultDouble;

    public TimeStruct(MatrixC _m,long _time){
        this.resultMatrix = _m;
        this.resultStr = null;
        this.resultDouble = -1;
        this.time = _time;
    }

    public TimeStruct(String _s,long _time){
        this.resultMatrix = null;
        this.resultStr = _s;
        this.resultDouble = -1;
        this.time = _time;
    }

    public TimeStruct(double _d,long _time){
        this.resultMatrix = null;
        this.resultStr = null;
        this.resultDouble = _d;
        this.time = _time;
    }

    public MatrixC getMatrixResult(){
        return this.resultMatrix;
    }

    public String getStringResult(){
        return  this.resultStr;
    }

    public double getDoubleResult(){
        return  this.resultDouble;
    }

    public long getTime(){
        return  this.time;
    }
}
