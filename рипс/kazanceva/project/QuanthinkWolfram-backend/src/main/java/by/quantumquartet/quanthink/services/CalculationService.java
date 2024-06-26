package by.quantumquartet.quanthink.services;

import static by.quantumquartet.quanthink.services.AppLogger.logError;

import by.quantumquartet.quanthink.cmath.MatrixC;
import by.quantumquartet.quanthink.cmath.NativeMath;
import by.quantumquartet.quanthink.cmath.TimeStruct;
import by.quantumquartet.quanthink.math.Equations;
import by.quantumquartet.quanthink.math.Matrix;
import by.quantumquartet.quanthink.math.MatrixOperations;
import by.quantumquartet.quanthink.math.BasicArithmetic;
import by.quantumquartet.quanthink.models.Calculation;
import by.quantumquartet.quanthink.models.ECalculation;
import by.quantumquartet.quanthink.models.User;
import by.quantumquartet.quanthink.repositories.CalculationRepository;
import by.quantumquartet.quanthink.repositories.UserRepository;
import by.quantumquartet.quanthink.rest.requests.calculations.*;
import by.quantumquartet.quanthink.rest.responses.calculations.CalculationDto;
import by.quantumquartet.quanthink.rest.responses.calculations.CalculationResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class CalculationService {
    private final int ROWS_INDEX = 0;
    private final int COLS_INDEX = 1;

    private final CalculationRepository calculationRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public CalculationService(CalculationRepository calculationRepository, UserRepository userRepository,
                              ObjectMapper objectMapper) {
        this.calculationRepository = calculationRepository;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    public List<CalculationDto> getAllCalculations() {
        List<Calculation> calculations = calculationRepository.findAll();
        List<CalculationDto> calculationsDto = new ArrayList<>();

        for (Calculation calculation : calculations) {
            CalculationDto calculationDto = convertToDto(calculation);
            calculationsDto.add(calculationDto);
        }

        return calculationsDto;
    }

    public Optional<CalculationDto> getCalculationById(long id) {
        Optional<Calculation> calculationData = calculationRepository.findById(id);
        return calculationData.map(this::convertToDto);
    }

    public List<CalculationDto> getCalculationsByUserId(long userId) {
        List<Calculation> calculations = calculationRepository.findCalculationsByUserId(userId);
        List<CalculationDto> calculationsDto = new ArrayList<>();

        for (Calculation calculation : calculations) {
            CalculationDto calculationDto = convertToDto(calculation);
            calculationsDto.add(calculationDto);
        }

        return calculationsDto;
    }

    public void deleteCalculation(long id) {
        calculationRepository.deleteById(id);
    }

    @Transactional
    public void deleteCalculationsByUserId(long userId) {
        calculationRepository.deleteCalculationsByUserId(userId);
    }

    public CalculationResult<String> solveBasicArithmetic(BasicArithmeticRequest basicArithmeticRequest) {
        String expression = basicArithmeticRequest.getExpression();

        String result;
        long elapsedTime;
        try {
            switch (basicArithmeticRequest.getLibrary()) {
                case JAVA -> {
                    long startTime = System.currentTimeMillis();
                    result = BasicArithmetic.solveExpression(expression);
                    long endTime = System.currentTimeMillis();
                    elapsedTime = endTime - startTime;
                }
                case C_PLUS_PLUS -> {
                    TimeStruct struct = NativeMath.solveExpressionCStub(expression);
                    result = struct.getStringResult();
                    elapsedTime = struct.getTime();
                }
                default -> throw new IllegalArgumentException("Not a valid library");
            }
        } catch (IllegalArgumentException e) {
            logError(CalculationService.class, e.getMessage());
            throw e;
        }

        Optional<User> userData = userRepository.findById(basicArithmeticRequest.getUserId());
        if (userData.isPresent()) {
            final int THREADS_USED = 1;
            Calculation newCalculation = new Calculation(
                    ECalculation.BASIC_ARITHMETIC,
                    expression,
                    result,
                    elapsedTime,
                    new Timestamp(System.currentTimeMillis()),
                    basicArithmeticRequest.getLibrary(),
                    THREADS_USED,
                    userData.get()
            );
            calculationRepository.save(newCalculation);
        }

        return new CalculationResult<>(result, elapsedTime);
    }

    public CalculationResult<String> solveEquation(EquationRequest equationRequest) {
        String equation = equationRequest.getEquation();

        String result;
        long elapsedTime;
        try {
            switch (equationRequest.getLibrary()) {
                case JAVA -> {
                    long startTime = System.currentTimeMillis();
                    result = Equations.SolveEquation(equation);
                    long endTime = System.currentTimeMillis();
                    elapsedTime = endTime - startTime;
                }
                case C_PLUS_PLUS -> {
                    TimeStruct struct = NativeMath.solveEquationCStub(equation);
                    result = struct.getStringResult();
                    elapsedTime = struct.getTime();
                }
                default -> throw new IllegalArgumentException("Not a valid library");
            }
        } catch (IllegalArgumentException e) {
            logError(CalculationService.class, e.getMessage());
            throw e;
        }

        Optional<User> userData = userRepository.findById(equationRequest.getUserId());
        if (userData.isPresent()) {
            final int THREADS_USED = 1;
            Calculation newCalculation = new Calculation(
                    ECalculation.EQUATION,
                    equation,
                    result,
                    elapsedTime,
                    new Timestamp(System.currentTimeMillis()),
                    equationRequest.getLibrary(),
                    THREADS_USED,
                    userData.get()
            );
            calculationRepository.save(newCalculation);
        }

        return new CalculationResult<>(result, elapsedTime);
    }

    public CalculationResult<Matrix> solveMatrixSum(MatrixSumRequest matrixSumRequest) throws JsonProcessingException {
        Matrix result;
        long elapsedTime;
        try {
            switch (matrixSumRequest.getLibrary()) {
                case JAVA -> {
                    long startTime = System.currentTimeMillis();
                    result = MatrixOperations.MatrixSum(
                            matrixSumRequest.getMatrix1(),
                            matrixSumRequest.getMatrix2(),
                            matrixSumRequest.getThreadsUsed()
                    );
                    long endTime = System.currentTimeMillis();
                    elapsedTime = endTime - startTime;
                }
                case C_PLUS_PLUS -> {
                    TimeStruct struct = NativeMath.MatrixSumC(
                            convertToCppMatrix(matrixSumRequest.getMatrix1()),
                            convertToCppMatrix(matrixSumRequest.getMatrix2()),
                            matrixSumRequest.getThreadsUsed()
                    );
                    result = convertToJavaMatrix(struct.getMatrixResult());
                    elapsedTime = struct.getTime();
                }
                default -> throw new IllegalArgumentException("Not a valid library");
            }
        } catch (IllegalArgumentException e) {
            logError(CalculationService.class, e.getMessage());
            throw e;
        }

        String jsonInputData = objectMapper.writeValueAsString(matrixSumRequest.getMatrix1().getData())
                + " " + objectMapper.writeValueAsString(matrixSumRequest.getMatrix2().getData());
        String jsonResult = objectMapper.writeValueAsString(result.getData());

        Optional<User> userData = userRepository.findById(matrixSumRequest.getUserId());
        if (userData.isPresent()) {
            Calculation newCalculation = new Calculation(
                    ECalculation.MATRIX_SUM,
                    jsonInputData,
                    jsonResult,
                    elapsedTime,
                    new Timestamp(System.currentTimeMillis()),
                    matrixSumRequest.getLibrary(),
                    matrixSumRequest.getThreadsUsed(),
                    userData.get()
            );
            calculationRepository.save(newCalculation);
        }

        return new CalculationResult<>(result, elapsedTime);
    }

    public CalculationResult<Matrix> solveMatrixSub(MatrixSubRequest matrixSubRequest) throws JsonProcessingException {
        Matrix result;
        long elapsedTime;
        try {
            switch (matrixSubRequest.getLibrary()) {
                case JAVA -> {
                    long startTime = System.currentTimeMillis();
                    result = MatrixOperations.MatrixSub(
                            matrixSubRequest.getMatrix1(),
                            matrixSubRequest.getMatrix2(),
                            matrixSubRequest.getThreadsUsed()
                    );
                    long endTime = System.currentTimeMillis();
                    elapsedTime = endTime - startTime;
                }
                case C_PLUS_PLUS -> {
                    TimeStruct struct = NativeMath.MatrixSubC(
                            convertToCppMatrix(matrixSubRequest.getMatrix1()),
                            convertToCppMatrix(matrixSubRequest.getMatrix2()),
                            matrixSubRequest.getThreadsUsed()
                    );
                    result = convertToJavaMatrix(struct.getMatrixResult());
                    elapsedTime = struct.getTime();
                }
                default -> throw new IllegalArgumentException("Not a valid library");
            }
        } catch (IllegalArgumentException e) {
            logError(CalculationService.class, e.getMessage());
            throw e;
        }

        String jsonInputData = objectMapper.writeValueAsString(matrixSubRequest.getMatrix1().getData())
                + " " + objectMapper.writeValueAsString(matrixSubRequest.getMatrix2().getData());
        String jsonResult = objectMapper.writeValueAsString(result.getData());

        Optional<User> userData = userRepository.findById(matrixSubRequest.getUserId());
        if (userData.isPresent()) {
            Calculation newCalculation = new Calculation(
                    ECalculation.MATRIX_SUB,
                    jsonInputData,
                    jsonResult,
                    elapsedTime,
                    new Timestamp(System.currentTimeMillis()),
                    matrixSubRequest.getLibrary(),
                    matrixSubRequest.getThreadsUsed(),
                    userData.get()
            );
            calculationRepository.save(newCalculation);
        }

        return new CalculationResult<>(result, elapsedTime);
    }

    public CalculationResult<Matrix> solveMatrixMul(MatrixMulRequest matrixMulRequest) throws JsonProcessingException {
        Matrix result;
        long elapsedTime;
        try {
            switch (matrixMulRequest.getLibrary()) {
                case JAVA -> {
                    long startTime = System.currentTimeMillis();
                    result = MatrixOperations.MatrixMul(
                            matrixMulRequest.getMatrix1(),
                            matrixMulRequest.getMatrix2(),
                            matrixMulRequest.getThreadsUsed()
                    );
                    long endTime = System.currentTimeMillis();
                    elapsedTime = endTime - startTime;
                }
                case C_PLUS_PLUS -> {
                    TimeStruct struct = NativeMath.MatrixMulC(
                            convertToCppMatrix(matrixMulRequest.getMatrix1()),
                            convertToCppMatrix(matrixMulRequest.getMatrix2()),
                            matrixMulRequest.getThreadsUsed()
                    );
                    result = convertToJavaMatrix(struct.getMatrixResult());
                    elapsedTime = struct.getTime();
                }
                default -> throw new IllegalArgumentException("Not a valid library");
            }
        } catch (IllegalArgumentException e) {
            logError(CalculationService.class, e.getMessage());
            throw e;
        }

        String jsonInputData = objectMapper.writeValueAsString(matrixMulRequest.getMatrix1().getData())
                + " " + objectMapper.writeValueAsString(matrixMulRequest.getMatrix2().getData());
        String jsonResult = objectMapper.writeValueAsString(result.getData());

        Optional<User> userData = userRepository.findById(matrixMulRequest.getUserId());
        if (userData.isPresent()) {
            Calculation newCalculation = new Calculation(
                    ECalculation.MATRIX_MUL,
                    jsonInputData,
                    jsonResult,
                    elapsedTime,
                    new Timestamp(System.currentTimeMillis()),
                    matrixMulRequest.getLibrary(),
                    matrixMulRequest.getThreadsUsed(),
                    userData.get()
            );
            calculationRepository.save(newCalculation);
        }

        return new CalculationResult<>(result, elapsedTime);
    }

    public CalculationResult<Matrix> solveMatrixMulByNum(MatrixMulByNumRequest matrixMulByNumRequest)
            throws JsonProcessingException {
        Matrix result;
        long elapsedTime;
        try {
            switch (matrixMulByNumRequest.getLibrary()) {
                case JAVA -> {
                    long startTime = System.currentTimeMillis();
                    result = MatrixOperations.MatrixMul(
                            matrixMulByNumRequest.getMatrix(),
                            matrixMulByNumRequest.getNumber(),
                            matrixMulByNumRequest.getThreadsUsed()
                    );
                    long endTime = System.currentTimeMillis();
                    elapsedTime = endTime - startTime;
                }
                case C_PLUS_PLUS -> {
                    TimeStruct struct = NativeMath.MatrixMulC(
                            convertToCppMatrix(matrixMulByNumRequest.getMatrix()),
                            matrixMulByNumRequest.getNumber(),
                            matrixMulByNumRequest.getThreadsUsed()
                    );
                    result = convertToJavaMatrix(struct.getMatrixResult());
                    elapsedTime = struct.getTime();
                }
                default -> throw new IllegalArgumentException("Not a valid library");
            }
        } catch (IllegalArgumentException e) {
            logError(CalculationService.class, e.getMessage());
            throw e;
        }

        String jsonInputData = objectMapper.writeValueAsString(matrixMulByNumRequest.getMatrix().getData())
                + " " + objectMapper.writeValueAsString(matrixMulByNumRequest.getNumber());
        String jsonResult = objectMapper.writeValueAsString(result.getData());

        Optional<User> userData = userRepository.findById(matrixMulByNumRequest.getUserId());
        if (userData.isPresent()) {
            Calculation newCalculation = new Calculation(
                    ECalculation.MATRIX_MUL_BY_NUM,
                    jsonInputData,
                    jsonResult,
                    elapsedTime,
                    new Timestamp(System.currentTimeMillis()),
                    matrixMulByNumRequest.getLibrary(),
                    matrixMulByNumRequest.getThreadsUsed(),
                    userData.get()
            );
            calculationRepository.save(newCalculation);
        }

        return new CalculationResult<>(result, elapsedTime);
    }

    public CalculationResult<Matrix> solveMatrixTranspose(MatrixTransposeRequest matrixTransposeRequest)
            throws JsonProcessingException {
        Matrix result;
        long elapsedTime;
        try {
            switch (matrixTransposeRequest.getLibrary()) {
                case JAVA -> {
                    long startTime = System.currentTimeMillis();
                    result = MatrixOperations.GetTransposeMatrix(matrixTransposeRequest.getMatrix());
                    long endTime = System.currentTimeMillis();
                    elapsedTime = endTime - startTime;
                }
                case C_PLUS_PLUS -> {
                    TimeStruct struct = NativeMath.GetTransposeMatrixC(
                            convertToCppMatrix(matrixTransposeRequest.getMatrix())
                    );
                    result = convertToJavaMatrix(struct.getMatrixResult());
                    elapsedTime = struct.getTime();
                }
                default -> throw new IllegalArgumentException("Not a valid library");
            }
        } catch (IllegalArgumentException e) {
            logError(CalculationService.class, e.getMessage());
            throw e;
        }

        String jsonInputData = objectMapper.writeValueAsString(matrixTransposeRequest.getMatrix().getData());
        String jsonResult = objectMapper.writeValueAsString(result.getData());

        Optional<User> userData = userRepository.findById(matrixTransposeRequest.getUserId());
        if (userData.isPresent()) {
            final int THREADS_USED = 1;
            Calculation newCalculation = new Calculation(
                    ECalculation.MATRIX_TRANSPOSE,
                    jsonInputData,
                    jsonResult,
                    elapsedTime,
                    new Timestamp(System.currentTimeMillis()),
                    matrixTransposeRequest.getLibrary(),
                    THREADS_USED,
                    userData.get()
            );
            calculationRepository.save(newCalculation);
        }

        return new CalculationResult<>(result, elapsedTime);
    }

    public CalculationResult<Matrix> solveMatrixReverse(MatrixReverseRequest matrixReverseRequest)
            throws JsonProcessingException {
        Matrix result;
        long elapsedTime;
        try {
            switch (matrixReverseRequest.getLibrary()) {
                case JAVA -> {
                    long startTime = System.currentTimeMillis();
                    result = MatrixOperations.GetReverseMatrix(matrixReverseRequest.getMatrix());
                    long endTime = System.currentTimeMillis();
                    elapsedTime = endTime - startTime;
                }
                case C_PLUS_PLUS -> {
                    TimeStruct struct = NativeMath.GetReverseMatrixC(
                            convertToCppMatrix(matrixReverseRequest.getMatrix())
                    );
                    result = convertToJavaMatrix(struct.getMatrixResult());
                    elapsedTime = struct.getTime();
                }
                default -> throw new IllegalArgumentException("Not a valid library");
            }
        } catch (IllegalArgumentException e) {
            logError(CalculationService.class, e.getMessage());
            throw e;
        }

        String jsonInputData = objectMapper.writeValueAsString(matrixReverseRequest.getMatrix().getData());
        String jsonResult = objectMapper.writeValueAsString(result.getData());

        Optional<User> userData = userRepository.findById(matrixReverseRequest.getUserId());
        if (userData.isPresent()) {
            final int THREADS_USED = 1;
            Calculation newCalculation = new Calculation(
                    ECalculation.MATRIX_REVERSE,
                    jsonInputData,
                    jsonResult,
                    elapsedTime,
                    new Timestamp(System.currentTimeMillis()),
                    matrixReverseRequest.getLibrary(),
                    THREADS_USED,
                    userData.get()
            );
            calculationRepository.save(newCalculation);
        }

        return new CalculationResult<>(result, elapsedTime);
    }

    public CalculationResult<Double> solveMatrixDeterminant(MatrixDeterminantRequest matrixDeterminantRequest)
            throws JsonProcessingException {
        double result;
        long elapsedTime;
        try {
            switch (matrixDeterminantRequest.getLibrary()) {
                case JAVA -> {
                    long startTime = System.currentTimeMillis();
                    result = MatrixOperations.GetDeterminant(
                            matrixDeterminantRequest.getMatrix(),
                            matrixDeterminantRequest.getThreadsUsed()
                    );
                    long endTime = System.currentTimeMillis();
                    elapsedTime = endTime - startTime;
                }
                case C_PLUS_PLUS -> {
                    TimeStruct struct = NativeMath.GetDeterminantC(
                            convertToCppMatrix(matrixDeterminantRequest.getMatrix()),
                            matrixDeterminantRequest.getThreadsUsed()
                    );
                    result = struct.getDoubleResult();
                    elapsedTime = struct.getTime();
                }
                default -> throw new IllegalArgumentException("Not a valid library");
            }
        } catch (IllegalArgumentException e) {
            logError(CalculationService.class, e.getMessage());
            throw e;
        }

        String jsonInputData = objectMapper.writeValueAsString(matrixDeterminantRequest.getMatrix().getData());
        String jsonResult = objectMapper.writeValueAsString(result);

        Optional<User> userData = userRepository.findById(matrixDeterminantRequest.getUserId());
        if (userData.isPresent()) {
            Calculation newCalculation = new Calculation(
                    ECalculation.MATRIX_DETERMINANT,
                    jsonInputData,
                    jsonResult,
                    elapsedTime,
                    new Timestamp(System.currentTimeMillis()),
                    matrixDeterminantRequest.getLibrary(),
                    matrixDeterminantRequest.getThreadsUsed(),
                    userData.get()
            );
            calculationRepository.save(newCalculation);
        }

        return new CalculationResult<>(result, elapsedTime);
    }

    public CalculationResult<String> solveSystem(MatrixSystemRequest matrixSystemRequest)
            throws JsonProcessingException {
        String result;
        long elapsedTime;
        try {
            switch (matrixSystemRequest.getLibrary()) {
                case JAVA -> {
                    long startTime = System.currentTimeMillis();
                    result = MatrixOperations.SolveSystem(
                            matrixSystemRequest.getMatrix1(),
                            matrixSystemRequest.getMatrix2(),
                            matrixSystemRequest.getThreadsUsed()
                    );
                    long endTime = System.currentTimeMillis();
                    elapsedTime = endTime - startTime;
                }
                case C_PLUS_PLUS -> {
                    TimeStruct struct = NativeMath.SolveSystemC(
                            convertToCppMatrix(matrixSystemRequest.getMatrix1()),
                            convertToCppMatrix(matrixSystemRequest.getMatrix2()),
                            matrixSystemRequest.getThreadsUsed()
                    );
                    result = struct.getStringResult();
                    elapsedTime = struct.getTime();
                }
                default -> throw new IllegalArgumentException("Not a valid library");
            }
        } catch (IllegalArgumentException e) {
            logError(CalculationService.class, e.getMessage());
            throw e;
        }

        String jsonInputData = objectMapper.writeValueAsString(matrixSystemRequest.getMatrix1().getData())
                + " " + objectMapper.writeValueAsString(matrixSystemRequest.getMatrix2().getData());
        String jsonResult = objectMapper.writeValueAsString(result);

        Optional<User> userData = userRepository.findById(matrixSystemRequest.getUserId());
        if (userData.isPresent()) {
            Calculation newCalculation = new Calculation(
                    ECalculation.MATRIX_SYSTEM,
                    jsonInputData,
                    jsonResult,
                    elapsedTime,
                    new Timestamp(System.currentTimeMillis()),
                    matrixSystemRequest.getLibrary(),
                    matrixSystemRequest.getThreadsUsed(),
                    userData.get()
            );
            calculationRepository.save(newCalculation);
        }

        return new CalculationResult<>(result, elapsedTime);
    }

    private MatrixC convertToCppMatrix(Matrix matrix) {
        int rows = matrix.getSize()[ROWS_INDEX];
        int cols = matrix.getSize()[COLS_INDEX];
        double[][] data = matrix.getData();
        return new MatrixC(rows, cols, data);
    }

    private Matrix convertToJavaMatrix(MatrixC matrixC) {
        int rows = matrixC.getSize()[ROWS_INDEX];
        int cols = matrixC.getSize()[COLS_INDEX];
        double[][] data = matrixC.getData();
        return new Matrix(rows, cols, data);
    }

    private CalculationDto convertToDto(Calculation calculation) {
        CalculationDto calculationDto = new CalculationDto();
        calculationDto.setId(calculation.getId());
        calculationDto.setType(calculation.getType());
        calculationDto.setInputData(calculation.getInputData());
        calculationDto.setResult(calculation.getResult());
        calculationDto.setTime(calculation.getTime());
        calculationDto.setDate((calculation.getDate()).toString());
        calculationDto.setLibrary(calculation.getLibrary());
        calculationDto.setThreadsUsed(calculation.getThreadsUsed());
        calculationDto.setUserId(calculation.getUser().getId());
        return calculationDto;
    }
}
