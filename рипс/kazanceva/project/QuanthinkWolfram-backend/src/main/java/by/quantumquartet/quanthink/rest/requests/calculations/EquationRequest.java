package by.quantumquartet.quanthink.rest.requests.calculations;

import by.quantumquartet.quanthink.models.ELibrary;

public class EquationRequest extends CalculationRequest {
    String equation;

    public EquationRequest(long userId, ELibrary library, String equation) {
        super(userId, library);
        this.equation = equation;
    }

    public String getEquation() {
        return equation;
    }

    public void setEquation(String equation) {
        this.equation = equation;
    }
}
