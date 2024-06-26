package by.quantumquartet.quanthink.rest.requests.calculations;

import by.quantumquartet.quanthink.math.Matrix;
import by.quantumquartet.quanthink.models.ELibrary;

public class MatrixMulByNumRequest extends CalculationRequest {
    private Matrix matrix;
    private double number;
    private int threadsUsed;

    public MatrixMulByNumRequest(long userId, ELibrary library, Matrix matrix, double number, int threadsUsed) {
        super(userId, library);
        this.matrix = matrix;
        this.number = number;
        this.threadsUsed = threadsUsed;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    public double getNumber() {
        return number;
    }

    public void setNumber(double number) {
        this.number = number;
    }

    public int getThreadsUsed() {
        return threadsUsed;
    }

    public void setThreadsUsed(int threadsUsed) {
        this.threadsUsed = threadsUsed;
    }
}
