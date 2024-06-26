package by.quantumquartet.quanthink.rest.responses.calculations;

import by.quantumquartet.quanthink.models.ECalculation;
import by.quantumquartet.quanthink.models.ELibrary;

public class CalculationDto {
    private long id;
    private ECalculation type;
    private String inputData;
    private String result;
    private long time;
    private String date;
    private ELibrary library;
    private int threadsUsed;
    private long userId;

    public CalculationDto() {
    }

    public CalculationDto(long id, ECalculation type, String inputData, String result, long time,
                          String date, ELibrary library, int threadsUsed, long userId) {
        this.id = id;
        this.type = type;
        this.inputData = inputData;
        this.result = result;
        this.time = time;
        this.date = date;
        this.library = library;
        this.threadsUsed = threadsUsed;
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ECalculation getType() {
        return type;
    }

    public void setType(ECalculation type) {
        this.type = type;
    }

    public String getInputData() {
        return inputData;
    }

    public void setInputData(String inputData) {
        this.inputData = inputData;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ELibrary getLibrary() {
        return library;
    }

    public void setLibrary(ELibrary library) {
        this.library = library;
    }

    public int getThreadsUsed() {
        return threadsUsed;
    }

    public void setThreadsUsed(int threadsUsed) {
        this.threadsUsed = threadsUsed;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
