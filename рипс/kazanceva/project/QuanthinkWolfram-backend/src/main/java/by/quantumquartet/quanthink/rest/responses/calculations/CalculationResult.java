package by.quantumquartet.quanthink.rest.responses.calculations;

public class CalculationResult<T> {
    private T result;
    private long time;

    public CalculationResult(T result, long time) {
        this.result = result;
        this.time = time;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
