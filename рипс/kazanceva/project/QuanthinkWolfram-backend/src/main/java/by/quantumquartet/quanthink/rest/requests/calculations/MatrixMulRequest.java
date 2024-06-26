package by.quantumquartet.quanthink.rest.requests.calculations;

import by.quantumquartet.quanthink.math.Matrix;
import by.quantumquartet.quanthink.models.ELibrary;

public class MatrixMulRequest extends CalculationRequest {
    private Matrix matrix1;
    private Matrix matrix2;
    private int threadsUsed;

    public MatrixMulRequest(long userId, ELibrary library, Matrix matrix1, Matrix matrix2, int threadsUsed) {
        super(userId, library);
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.threadsUsed = threadsUsed;
    }

    public Matrix getMatrix1() {
        return matrix1;
    }

    public void setMatrix1(Matrix matrix1) {
        this.matrix1 = matrix1;
    }

    public Matrix getMatrix2() {
        return matrix2;
    }

    public void setMatrix2(Matrix matrix2) {
        this.matrix2 = matrix2;
    }

    public int getThreadsUsed() {
        return threadsUsed;
    }

    public void setThreadsUsed(int threadsUsed) {
        this.threadsUsed = threadsUsed;
    }
}
