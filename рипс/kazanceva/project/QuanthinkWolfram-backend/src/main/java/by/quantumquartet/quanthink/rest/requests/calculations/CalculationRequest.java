package by.quantumquartet.quanthink.rest.requests.calculations;

import by.quantumquartet.quanthink.models.ELibrary;

public class CalculationRequest {
    private long userId;
    private ELibrary library;

    public CalculationRequest(long userId, ELibrary library) {
        this.userId = userId;
        this.library = library;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public ELibrary getLibrary() {
        return library;
    }

    public void setLibrary(ELibrary library) {
        this.library = library;
    }
}
