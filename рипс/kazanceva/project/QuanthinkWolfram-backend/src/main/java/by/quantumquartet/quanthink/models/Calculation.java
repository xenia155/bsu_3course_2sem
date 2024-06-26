package by.quantumquartet.quanthink.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.sql.Timestamp;

@Entity
@Table(name = "calculation")
public class Calculation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ECalculation type;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String inputData;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String result;

    @NotBlank
    @Column(nullable = false)
    private long time;

    @NotBlank
    @Column(nullable = false)
    private Timestamp date;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ELibrary library;

    @NotBlank
    @Column(nullable = false)
    private int threadsUsed;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Calculation() {
    }

    public Calculation(ECalculation type, String inputData, String result, long time,
                       Timestamp date, ELibrary library, int threadsUsed, User user) {
        this.type = type;
        this.inputData = inputData;
        this.result = result;
        this.time = time;
        this.date = date;
        this.library = library;
        this.threadsUsed = threadsUsed;
        this.user = user;
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

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
