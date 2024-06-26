package by.quantumquartet.quanthink.rest.requests.calculations;

import by.quantumquartet.quanthink.math.Matrix;
import by.quantumquartet.quanthink.models.ELibrary;

public class MatrixDeterminantRequest extends CalculationRequest {
    private Matrix matrix;
    private int threadsUsed;

    public MatrixDeterminantRequest(long userId, ELibrary library, Matrix matrix, int threadsUsed) {
        super(userId, library);
        this.matrix = matrix;
        this.threadsUsed = threadsUsed;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    public int getThreadsUsed() {
        return threadsUsed;
    }

    public void setThreadsUsed(int threadsUsed) {
        this.threadsUsed = threadsUsed;
    }
}
