package by.quantumquartet.quanthink.controllers;

import static by.quantumquartet.quanthink.services.AppLogger.*;

import by.quantumquartet.quanthink.math.Matrix;
import by.quantumquartet.quanthink.rest.requests.calculations.*;
import by.quantumquartet.quanthink.rest.responses.ErrorResponse;
import by.quantumquartet.quanthink.rest.responses.SuccessResponse;
import by.quantumquartet.quanthink.rest.responses.calculations.CalculationDto;
import by.quantumquartet.quanthink.rest.responses.calculations.CalculationResult;
import by.quantumquartet.quanthink.services.CalculationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/calculations")
public class CalculationController {
    private final CalculationService calculationService;

    @Autowired
    public CalculationController(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @GetMapping
    public ResponseEntity<?> getAllCalculations() {
        List<CalculationDto> calculations = calculationService.getAllCalculations();
        if (calculations.isEmpty()) {
            logWarn(CalculationController.class, "No calculations found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("No calculations found"));
        }
        logInfo(CalculationController.class, "Calculations retrieved successfully");
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse<>("Calculations retrieved successfully", calculations));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCalculationById(@PathVariable("id") long id) {
        Optional<CalculationDto> calculationData = calculationService.getCalculationById(id);
        if (calculationData.isEmpty()) {
            logWarn(CalculationController.class, "Calculation with id = " + id + " not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Calculation not found"));
        }
        logInfo(CalculationController.class, "Calculation with id = " + id + " retrieved successfully");
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse<>("Calculation retrieved successfully", calculationData.get()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getCalculationsByUserId(@PathVariable("userId") long userId) {
        List<CalculationDto> calculations = calculationService.getCalculationsByUserId(userId);
        if (calculations.isEmpty()) {
            logWarn(CalculationController.class, "No calculations found for user with id = " + userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("No calculations found"));
        }
        logInfo(CalculationController.class,
                "Calculations for user with id = " + userId + " retrieved successfully");
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse<>("User calculations retrieved successfully", calculations));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCalculation(@PathVariable("id") long id) {
        Optional<CalculationDto> calculationData = calculationService.getCalculationById(id);
        if (calculationData.isEmpty()) {
            logWarn(CalculationController.class, "Calculation with id = " + id + " not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Calculation not found"));
        }
        try {
            calculationService.deleteCalculation(id);
            logInfo(CalculationController.class, "Calculation with id = " + id + " deleted successfully");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new SuccessResponse<>("Calculation deleted successfully", id));
        } catch (Exception e) {
            logError(CalculationController.class, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<?> deleteCalculationsByUserId(@PathVariable("userId") long userId) {
        List<CalculationDto> calculations = calculationService.getCalculationsByUserId(userId);
        if (calculations.isEmpty()) {
            logWarn(CalculationController.class, "No calculations found for user with id = " + userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("No calculations found"));
        }
        try {
            calculationService.deleteCalculationsByUserId(userId);
            logInfo(CalculationController.class,
                    "Calculations for user with id = " + userId + " deleted successfully");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new SuccessResponse<>("User calculations deleted successfully", userId));
        } catch (Exception e) {
            logError(CalculationController.class, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    @PostMapping("/basicArithmetic")
    public ResponseEntity<?> solveBasicArithmetic(@Valid @RequestBody BasicArithmeticRequest basicArithmeticRequest) {
        try {
            CalculationResult<String> result = calculationService.solveBasicArithmetic(basicArithmeticRequest);
            logInfo(CalculationController.class, "Basic arithmetic calculation performed successfully");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new SuccessResponse<>("Calculation performed successfully", result));
        } catch (IllegalArgumentException e) {
            logError(CalculationController.class, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            logError(CalculationController.class, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    @PostMapping("/equation")
    public ResponseEntity<?> solveEquation(@Valid @RequestBody EquationRequest equationRequest) {
        try {
            CalculationResult<String> result = calculationService.solveEquation(equationRequest);
            logInfo(CalculationController.class, "Equation calculation performed successfully");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new SuccessResponse<>("Calculation performed successfully", result));
        } catch (IllegalArgumentException e) {
            logError(CalculationController.class, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            logError(CalculationController.class, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    @PostMapping("/matrixSum")
    public ResponseEntity<?> solveMatrixSum(@Valid @RequestBody MatrixSumRequest matrixSumRequest) {
        try {
            CalculationResult<Matrix> result = calculationService.solveMatrixSum(matrixSumRequest);
            logInfo(CalculationController.class, "Matrix sum calculation performed successfully");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new SuccessResponse<>("Calculation performed successfully", result));
        } catch (IllegalArgumentException e) {
            logError(CalculationController.class, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            logError(CalculationController.class, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    @PostMapping("/matrixSub")
    public ResponseEntity<?> solveMatrixSub(@Valid @RequestBody MatrixSubRequest matrixSubRequest) {
        try {
            CalculationResult<Matrix> result = calculationService.solveMatrixSub(matrixSubRequest);
            logInfo(CalculationController.class, "Matrix subtraction calculation performed successfully");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new SuccessResponse<>("Calculation performed successfully", result));
        } catch (IllegalArgumentException e) {
            logError(CalculationController.class, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            logError(CalculationController.class, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    @PostMapping("/matrixMul")
    public ResponseEntity<?> solveMatrixMul(@Valid @RequestBody MatrixMulRequest matrixMulRequest) {
        try {
            CalculationResult<Matrix> result = calculationService.solveMatrixMul(matrixMulRequest);
            logInfo(CalculationController.class, "Matrix multiplication calculation performed successfully");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new SuccessResponse<>("Calculation performed successfully", result));
        } catch (IllegalArgumentException e) {
            logError(CalculationController.class, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            logError(CalculationController.class, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    @PostMapping("/matrixMulByNum")
    public ResponseEntity<?> solveMatrixMulByNum(@Valid @RequestBody MatrixMulByNumRequest matrixMulByNumRequest) {
        try {
            CalculationResult<Matrix> result = calculationService.solveMatrixMulByNum(matrixMulByNumRequest);
            logInfo(CalculationController.class,
                    "Matrix multiplication by number calculation performed successfully");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new SuccessResponse<>("Calculation performed successfully", result));
        } catch (IllegalArgumentException e) {
            logError(CalculationController.class, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            logError(CalculationController.class, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    @PostMapping("/matrixTranspose")
    public ResponseEntity<?> solveMatrixTranspose(@Valid @RequestBody MatrixTransposeRequest matrixTransposeRequest) {
        try {
            CalculationResult<Matrix> result = calculationService.solveMatrixTranspose(matrixTransposeRequest);
            logInfo(CalculationController.class, "Matrix transpose calculation performed successfully");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new SuccessResponse<>("Calculation performed successfully", result));
        } catch (IllegalArgumentException e) {
            logError(CalculationController.class, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            logError(CalculationController.class, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    @PostMapping("/matrixReverse")
    public ResponseEntity<?> solveMatrixReverse(@Valid @RequestBody MatrixReverseRequest matrixReverseRequest) {
        try {
            CalculationResult<Matrix> result = calculationService.solveMatrixReverse(matrixReverseRequest);
            logInfo(CalculationController.class, "Matrix reverse calculation performed successfully");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new SuccessResponse<>("Calculation performed successfully", result));
        } catch (IllegalArgumentException e) {
            logError(CalculationController.class, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            logError(CalculationController.class, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    @PostMapping("/matrixDeterminant")
    public ResponseEntity<?> solveMatrixDeterminant(@Valid @RequestBody MatrixDeterminantRequest
                                                            matrixDeterminantRequest) {
        try {
            CalculationResult<Double> result = calculationService.solveMatrixDeterminant(matrixDeterminantRequest);
            logInfo(CalculationController.class, "Matrix determinant calculation performed successfully");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new SuccessResponse<>("Calculation performed successfully", result));
        } catch (IllegalArgumentException e) {
            logError(CalculationController.class, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            logError(CalculationController.class, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }

    @PostMapping("/matrixSystem")
    public ResponseEntity<?> solveSystem(@Valid @RequestBody MatrixSystemRequest matrixSystemRequest) {
        try {
            CalculationResult<String> result = calculationService.solveSystem(matrixSystemRequest);
            logInfo(CalculationController.class, "Matrix system calculation performed successfully");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new SuccessResponse<>("Calculation performed successfully", result));
        } catch (IllegalArgumentException e) {
            logError(CalculationController.class, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            logError(CalculationController.class, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error"));
        }
    }
}
