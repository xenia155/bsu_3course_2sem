package by.quantumquartet.quanthink.rest.requests.calculations;

import by.quantumquartet.quanthink.models.ELibrary;

public class BasicArithmeticRequest extends CalculationRequest {
    String expression;

    public BasicArithmeticRequest(long userId, ELibrary library, String expression) {
        super(userId, library);
        this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
