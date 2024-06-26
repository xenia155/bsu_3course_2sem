package by.quantumquartet.quanthink.rest.requests.calculations;

import by.quantumquartet.quanthink.math.Matrix;
import by.quantumquartet.quanthink.models.ELibrary;

public class MatrixTransposeRequest extends CalculationRequest {
    private Matrix matrix;

    public MatrixTransposeRequest(long userId, ELibrary library, Matrix matrix) {
        super(userId, library);
        this.matrix = matrix;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }
}
