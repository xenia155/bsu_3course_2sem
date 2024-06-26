import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BasicArithmetic, Equation, MatrixDeterminant, MatrixMul, MatrixMulByNum, MatrixReverse, MatrixSub, MatrixSum, MatrixSystem, MatrixTranspose } from "src/app/interfaces/calculation";
import { Observable } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class CalculationService {
  private apiUrl = 'http://localhost:8080/calculations/';

  constructor(private http: HttpClient) { }

  createCalculationBasicArithmetic(calculationDetails: BasicArithmetic): Observable<any> {
    return this.http.post(this.apiUrl + "basicArithmetic", calculationDetails);
  }

  createCalculationEquation(calculationDetails: Equation): Observable<any> {
    return this.http.post(this.apiUrl + "equation", calculationDetails);
  }

  createCalculationMatrixSub(calculationDetails: MatrixSub): Observable<any> {
    return this.http.post(this.apiUrl + "matrixSub", calculationDetails);
  }

  createCalculationMatrixMul(calculationDetails: MatrixMul): Observable<any> {
    return this.http.post(this.apiUrl + "matrixMul", calculationDetails);
  }

  createCalculationMatrixMulByNum(calculationDetails: MatrixMulByNum): Observable<any> {
    return this.http.post(this.apiUrl + "matrixMulByNum", calculationDetails);
  }

  createCalculationMatrixTranspose(calculationDetails: MatrixTranspose): Observable<any> {
    return this.http.post(this.apiUrl + "matrixTranspose", calculationDetails);
  }

  createCalculationMatrixReverse(calculationDetails: MatrixReverse): Observable<any> {
    return this.http.post(this.apiUrl + "matrixReverse", calculationDetails);
  }

  createCalculationMatrixDeterminant(calculationDetails: MatrixDeterminant): Observable<any> {
    return this.http.post(this.apiUrl + "matrixDeterminant", calculationDetails);
  }

  createCalculationMatrixSystem(calculationDetails: MatrixSystem): Observable<any> {
    return this.http.post(this.apiUrl + "matrixSystem", calculationDetails);
  }

  createCalculationMatrixSum(calculationDetails: MatrixSum): Observable<any> {
    return this.http.post(this.apiUrl + "matrixSum", calculationDetails);
  }
}
